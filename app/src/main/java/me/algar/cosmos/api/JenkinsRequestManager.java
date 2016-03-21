package me.algar.cosmos.api;


import android.content.Context;

import java.util.List;

import me.algar.cosmos.api.models.Job;
import me.algar.cosmos.data.JobStorage;
import me.algar.cosmos.data.Jobvm;
import rx.Observable;
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

    public Observable<List<Jobvm>> getJobs(int startIndex) {
        // return database observable
        Observable<List<Jobvm>> observable = db.getJobs(startIndex, startIndex + ITEMS_PER_REQUEST);

        // then subscribe to the Service observable to update the DB when api response is received
        new JenkinsService().getJobs(startIndex)
                .subscribeOn(Schedulers.io())
                .subscribe(jobs -> {
                    db.insertJobs(jobs);
                });

        return observable;
    }

    public void clearCache() {
        db.clearJobs().observeOn(Schedulers.computation()).subscribe();
    }
}
