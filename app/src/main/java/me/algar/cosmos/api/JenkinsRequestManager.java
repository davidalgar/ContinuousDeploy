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

    private static final long MAX_CACHE_AGE = 1000*60*5;    //5mins

    private long last_job_req = 0;
    private long last_build_req = 0;

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

    private boolean cacheExpired(long lastRequest){
        long now = System.currentTimeMillis();

        if((now - lastRequest) > MAX_CACHE_AGE){
            return true;
        }

        return false;
    }

    //Convenience unwrapper - this is the more useful form of data, since we don't care about the Job object
    public Observable<List<Build>> getBuildsForJob(String jobName, int startIndex){
        Observable<List<Build>> observable = db.getBuilds(startIndex, startIndex + JenkinsService.BUILDS_PER_REQUEST);

        if(cacheExpired(last_build_req)){
            new JenkinsService().getJob(jobName, startIndex)
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
                    })
                .subscribe(new Subscriber<List<Build>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<Build> builds) {
                        db.insertBuilds(builds);
                        if(startIndex == 0){
                            last_build_req = System.currentTimeMillis();
                        }
                    }
                });
        }


        return observable;
    }

    public Observable<Job> getJobById(Long jobId){
        return db.getJob(jobId);
    }

    public Observable<List<Job>> getJobs(int startIndex) {
        // return database observable
        Observable<List<Job>> observable = db.getJobs(startIndex, startIndex + JenkinsService.JOBS_PER_REQUEST);

        if(cacheExpired(last_job_req)) {
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
                            if (startIndex == 0) {
                                last_job_req = System.currentTimeMillis();
                            }
                        }
                    });
        }

        return observable;
    }

    public void clearCache() {
        db.clearJobs().observeOn(Schedulers.computation()).subscribe();
    }
}
