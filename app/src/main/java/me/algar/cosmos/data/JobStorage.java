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


/*
favorite = favorite_local || favorite

during sync:

if(favorite_local & ! favorite){
    // fire request to update
}

 */

public class JobStorage {
    private static final long MAX_AGE_MILLIS = 1000*60; //60s
    private BriteDatabase db;

    public JobStorage(Context context) {
        SqlBrite sqlBrite = SqlBrite.create();
        JobDatabaseHelper openHelper = new JobDatabaseHelper(context);
        db = sqlBrite.wrapDatabaseHelper(openHelper, Schedulers.io());
    }

    public Observable<List<Build>> getBuilds(long jobId, int limit, int startIndex){
        Observable<SqlBrite.Query> builds
                = db.createQuery(JobDatabaseHelper.TABLE_NAME_BUILDS,
                    BuildModel.SELECT_RANGE,
                    ""+jobId,
                    "" + limit,
                    "" + startIndex);

        return builds.map((SqlBrite.Query query) -> {
            Cursor cursor = query.run();
            List<Build> buildList = new ArrayList<>();
            if (cursor == null) {
                return buildList;
            }
            while (cursor.moveToNext()) {
                buildList.add(Build.MAPPER.map(cursor));
            }
            return buildList;
        }).subscribeOn(Schedulers.computation());
    }

    // 1) Do items exist in db?
    //   if no, check meta-data table for last request time
    //   if yes, check max age of data between indices
    public Observable<Boolean> isBuildCacheCurrent(long jobId, int limit, int startIndex){
        long maxAge = System.currentTimeMillis() - MAX_AGE_MILLIS;
        return
            db.createQuery(
                    JobDatabaseHelper.TABLE_NAME_BUILDS,
                    BuildModel.SELECT_MAX_AGE,
                    ""+jobId,
                    "" + limit,
                    "" + startIndex)
                    .map((SqlBrite.Query query) -> {
                        Cursor cursor = query.run();
                        if (cursor == null) {
                            return false;
                        }
                        if (cursor.moveToNext()) {
                            long oldestItem = cursor.getLong(0);
                            Timber.d("Oldest : "+oldestItem);
                            return oldestItem > maxAge;
                        }
                        return false;
                    });
    }

    public Observable<Boolean> isJobCacheCurrent(int startIndex, int endIndex){
        long maxAge = System.currentTimeMillis() - MAX_AGE_MILLIS;

        return db.createQuery(
                        JobDatabaseHelper.TABLE_NAME_JOBS,
                        JobModel.SELECT_MAX_AGE,
                        "" + endIndex,
                        "" + startIndex)
                        .map((SqlBrite.Query query) -> {
                            Cursor cursor = query.run();
                            if (cursor == null) {
                                return false;
                            }
                            if (cursor.moveToNext()) {
                                long oldestItem = cursor.getLong(0);
                                return oldestItem > maxAge;
                            }
                            return false;
                        });
    }

    public Observable<List<Job>> getJobs(int startIndex, int endIndex) {
        Observable<SqlBrite.Query> jobs
                = db.createQuery(JobDatabaseHelper.TABLE_NAME_JOBS,
                JobModel.SELECT_RANGE,
                "" + endIndex,
                "" + startIndex);

        return jobs.map((SqlBrite.Query query) -> {
            Cursor cursor = query.run();
            List<Job> jobList = new ArrayList<>();
            if (cursor == null || cursor.getCount() == 0) {
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
            long created = System.currentTimeMillis();
            for (Job job : jobs) {
                Cursor cursor = db.query(JobModel.SELECT_BY_NAME, job.name());

                //only insert if it doesn't exist in the db
                if(!cursor.moveToNext()) {
                    db.insert(JobModel.TABLE_NAME, new Job.Marshal()
                            .color(job.color)
                            .name(job.name)
                            .created(created)
                            .favorite(Job.NOT_FAVORITE)
                            .favorite_local(Job.NOT_FAVORITE)
                            .asContentValues());
                }
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

    public Observable<Integer> clearBuildsForJob(long jobId) {
        return makeObservable(() ->
                db.delete(BuildModel.TABLE_NAME, "jobId="+jobId))
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

    public Observable<Job> getJob(Long jobId) {
        Observable<SqlBrite.Query> jobs
                = db.createQuery(JobDatabaseHelper.TABLE_NAME_JOBS,
                                JobModel.SELECT_BY_ID, jobId.toString());
        return jobs.map((SqlBrite.Query query) -> {
            Cursor cursor = query.run();
            if (cursor == null) {
                return null;
            }
            if (cursor.moveToNext()) {
                return Job.MAPPER.map(cursor);
            }
            return null;
        });
    }

    public boolean insertBuilds(List<Build> builds) {
        boolean success;
        try (BriteDatabase.Transaction transaction = db.newTransaction()) {
            long created = System.currentTimeMillis();
            for (Build build : builds) {
                Cursor cursor = db.query(BuildModel.SELECT_BY_ID, build.getBuildNumber());
                //only insert if it doesn't exist in the db
                //TODO change this to instead remove the item, and replace with the new item
                if(!cursor.moveToNext()) {
                    db.insert(BuildModel.TABLE_NAME, new Build.Marshal()
                            .status(build.status())
                            .number(build.number())
                            .url(build.url())
                            .responsible(build.responsible())
                            .jobId(build.jobId)
                            .created(created)
                            .asContentValues());
                }
            }
            transaction.markSuccessful();
            success = true;
        }catch (Exception e){
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    public Observable<List<Job>> searchForJobs(String searchText) {

        Observable<SqlBrite.Query> jobs
                = db.createQuery(JobDatabaseHelper.TABLE_NAME_JOBS,
                JobModel.SEARCH_BY_NAME.replace("?",searchText));
        return jobs.map((SqlBrite.Query query) -> {
            Cursor cursor = query.run();
            if (cursor == null) {
                return null;
            }
            List<Job> results = new ArrayList<>();
            while (cursor.moveToNext()) {
                results.add(Job.MAPPER.map(cursor));
            }
            return results;
        });
    }

    public class ItemNotFoundException extends Exception {

    }
}
