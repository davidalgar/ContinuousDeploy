package me.algar.cosmos.api;


import android.content.Context;

import java.util.List;

import me.algar.cosmos.data.JobStorage;
import me.algar.cosmos.data.Job;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class JenkinsRequestManager {
    private JobStorage db;
    static final int ITEMS_PER_REQUEST = 30;

    public JenkinsRequestManager(Context context) {
        db = new JobStorage(context.getApplicationContext());
    }

    public Observable<Job> getJob(String jobName, int startIndex) {
        return new JenkinsService().getJob(jobName, startIndex);
    }

    public Observable<List<Job>> getJobs(int startIndex) {
        // return database observable
        Observable<List<Job>> observable = db.getJobs(startIndex, startIndex + ITEMS_PER_REQUEST);

        // then subscribe to the Service observable to update the DB when api response is received
        new JenkinsService().getJobs(startIndex)
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<Job>>() {
                    @Override
                    public void onCompleted() {
                        // Do nothing?
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<Job> jobs) {
                        db.insertJobs(jobs);
                    }
                });

        return observable;
    }

    public void clearCache() {
        db.clearJobs().observeOn(Schedulers.computation()).subscribe();
    }
}
