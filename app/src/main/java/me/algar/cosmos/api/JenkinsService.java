package me.algar.cosmos.api;

import java.util.List;

import me.algar.cosmos.api.models.Build;
import me.algar.cosmos.api.models.Job;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Schedulers;

public class JenkinsService {
    static final int ITEMS_PER_REQUEST = 5;

    public Observable<Job> getJob(String jobName, int startIndex){
        Retrofit retrofit = Network.buildRetrofit();

        JenkinsApi jenkins = retrofit.create(JenkinsApi.class);
        return jenkins.getJob(jobName).subscribeOn(Schedulers.io());//jobName);
    }

    public Observable<List<Job>> getJobs(int startIndex){
        Retrofit retrofit = Network.buildRetrofit();

        JenkinsApi jenkinsService = retrofit.create(JenkinsApi.class);
        return jenkinsService.getJobs();
    }

    public Observable<Build> getBuild(String jobName, int build, int startIndex){
        Retrofit retrofit = Network.buildRetrofit();
        String tree = "artifacts[*],changeSet[items[msg,author[fullName]]],result{"+startIndex + ","+(startIndex+ITEMS_PER_REQUEST)+"}";

        JenkinsApi jenkinsService = retrofit.create(JenkinsApi.class);
        return jenkinsService.getBuild(jobName, build, tree);
    }
}
