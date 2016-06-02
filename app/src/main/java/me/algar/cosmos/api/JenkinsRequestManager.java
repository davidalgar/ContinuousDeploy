package me.algar.cosmos.api;


import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import me.algar.cosmos.data.Build;
import me.algar.cosmos.data.JobStorage;
import me.algar.cosmos.data.Job;
import me.algar.cosmos.util.RxErrorBus;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class JenkinsRequestManager {
    private static JenkinsRequestManager sInstance;
    private JobStorage db;

    public static JenkinsRequestManager getInstance(Context context){
        if(sInstance == null){
            sInstance = new JenkinsRequestManager(context);
        }
        return sInstance;
    }

    public JenkinsRequestManager(Context context) {
        db = new JobStorage(context.getApplicationContext());
    }

    //Convenience unwrapper - this is the more useful form of data, since we don't care about the Job object
    public Observable<List<Build>> getBuildsForJob(String jobName, long jobId, int startIndex){
        Observable<List<Build>> cachedBuilds = db.getBuilds(jobId, JenkinsService.BUILDS_PER_REQUEST, startIndex);

        db.isBuildCacheCurrent(jobId, JenkinsService.BUILDS_PER_REQUEST, startIndex)
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        //do nothing
                    }

                    @Override
                    public void onError(Throwable e) {
                        makeBuildsNetworkCall(jobName, jobId, startIndex);
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if(!aBoolean){
                            Timber.d("Cache expired for start="+startIndex);
                            makeBuildsNetworkCall(jobName, jobId, startIndex);
                        }else{
                            Timber.d("Cache hit for start="+startIndex);
                        }
                    }
                });

        return cachedBuilds;
    }

    private void makeBuildsNetworkCall(String jobName, long jobId, int startIndex){
        new JenkinsService().getJob(jobName, startIndex)
                .map(job -> {
                    List<Build> builds = job.builds;
                    for(Build build : builds){
                        build.jobId = jobId;
                        build.generateResponsible();
                    }
                    return builds;
                })
                .map(this::notNull)
                .subscribe(new Subscriber<List<Build>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        RxErrorBus.getInstance().errorHappened(new RxErrorBus.Error("Could not load builds",e));
                    }

                    @Override
                    public void onNext(List<Build> builds) {
                        db.insertBuilds(builds);
                    }
                });
    }

    private <T> List<T> notNull(List<T> list){
        List<T> result = new ArrayList<T>();
        for(T item : list){
            if(item != null){
                result.add(item);
            }
        }
        return result;
    }

    public Observable<Job> getJobById(Long jobId){
        return db.getJob(jobId);
    }

    public Observable<List<Job>> getJobs(int startIndex) {
        int endIndex = JenkinsService.getJobEndIndex(startIndex);
        Observable<List<Job>> observable = db.getJobs(startIndex, endIndex);

        db.isJobCacheCurrent(startIndex, endIndex)
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        //do nothing
                    }

                    @Override
                    public void onError(Throwable e) {
                        makeJobsNetworkCall(startIndex, endIndex);
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if(!aBoolean){
                            makeJobsNetworkCall(startIndex, endIndex);
                        }
                    }
                });

        return observable;
    }

    private void makeJobsNetworkCall(int startIndex, int endIndex){
        new JenkinsService().getJobs(startIndex, endIndex)
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
    }

    public void clearJobCache() {
        db.clearJobs().observeOn(Schedulers.computation()).subscribe();
    }

    public Observable<Integer> clearBuildCache(long jobId) {
        return db.clearBuildsForJob(jobId).observeOn(Schedulers.computation());
    }
}
