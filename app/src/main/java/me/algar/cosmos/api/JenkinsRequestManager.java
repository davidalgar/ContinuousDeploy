package me.algar.cosmos.api;


import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import me.algar.cosmos.data.Build;
import me.algar.cosmos.data.JobStorage;
import me.algar.cosmos.data.Job;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class JenkinsRequestManager {
    private static JenkinsRequestManager sInstance;
    private JobStorage db;
    static final int ITEMS_PER_REQUEST = 30;

    public static JenkinsRequestManager getInstance(Context context){
        if(sInstance == null){
            sInstance = new JenkinsRequestManager(context);
        }
        return sInstance;
    }

    public JenkinsRequestManager(Context context) {
        db = new JobStorage(context.getApplicationContext());
    }

    public Observable<Job> getJob(String jobName, int startIndex) {
        return new JenkinsService().getJob(jobName, startIndex);
    }

    //Convenience unwrapper - this is the more useful form of data, since we don't care about the Job object
    public Observable<List<Build>> getBuildsForJob(String jobName, int startIndex){
        return new JenkinsService().getJob(jobName, startIndex)
                .map(Job::getBuilds)
                .map(builds -> {
                    List<Build> newList = new ArrayList<>();
                    for (Build build : builds) {
                        if (build != null) {
                            build.generateResponsible();
                            newList.add(build);
                        }
                    }
                    return newList;
                });
    }

    public Observable<Job> getJobById(Long jobId){
        return db.getJob(jobId);
    }

    public Observable<List<Job>> getJobs(int startIndex) {
        // return database observable
        Observable<List<Job>> observable = db.getJobs(startIndex, startIndex + JenkinsService.JOBS_PER_REQUEST);

        //TODO
            // ideally if data exists, we would avoid the network call

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
