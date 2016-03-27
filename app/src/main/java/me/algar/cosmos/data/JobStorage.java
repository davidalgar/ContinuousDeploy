package me.algar.cosmos.data;

import android.content.Context;
import android.database.Cursor;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class JobStorage {
    private BriteDatabase db;

    public JobStorage(Context context) {
        SqlBrite sqlBrite = SqlBrite.create();
        JobDatabaseHelper openHelper = new JobDatabaseHelper(context);
        db = sqlBrite.wrapDatabaseHelper(openHelper, Schedulers.io());
    }

    public Observable<List<Job>> getJobs(int startIndex, int endIndex) {
        Observable<SqlBrite.Query> jobs
                = db.createQuery(JobDatabaseHelper.TABLE_NAME,
                JobModel.SELECT_RANGE,
                "" + startIndex, "" + endIndex);

        return jobs.map((SqlBrite.Query query) -> {
            Cursor cursor = query.run();
            List<Job> jobList = new ArrayList<>();
            if (cursor == null) {
                return jobList;
            }
            while (cursor.moveToNext()) {
                jobList.add(Job.MAPPER.map(cursor));
            }
            return jobList;
        }).subscribeOn(Schedulers.computation());
    }

    public Boolean insertJobs(List<Job> jobs) {
        boolean success;
        try (BriteDatabase.Transaction transaction = db.newTransaction()) {
            for (Job job : jobs) {
                db.insert(JobModel.TABLE_NAME, new Job.Marshal()
                        .color(job.color)
                        .name(job.name)
                        .asContentValues());
            }
            transaction.markSuccessful();
            success = true;
        }catch (Exception e){
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    public Observable<Integer> clearJobs() {
        return makeObservable(() ->
                    db.delete(JobModel.TABLE_NAME, "1=1"))
                    .subscribeOn(Schedulers.computation()
                );
    }

    private static <T> Observable<T> makeObservable(final Callable<T> func) {
        return Observable.create(
                new Observable.OnSubscribe<T>() {
                    @Override
                    public void call(Subscriber<? super T> subscriber) {
                        try {
                            subscriber.onNext(func.call());
                        } catch (Exception ex) {
                            Timber.e(ex, "Error reading from the database");
                        }
                    }
                });
    }
}
