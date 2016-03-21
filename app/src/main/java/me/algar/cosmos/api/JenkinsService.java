package me.algar.cosmos.api;

import java.util.List;

import me.algar.cosmos.api.models.Build;
import me.algar.cosmos.api.models.Job;
import me.algar.cosmos.api.models.JobCollection;
import retrofit2.Retrofit;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class JenkinsService {

    static final int ITEMS_PER_REQUEST = 50;

    public Observable<Job> getJob(String jobName, int startIndex){
        Retrofit retrofit = NetworkProvider.buildRetrofit();

        JenkinsApi jenkins = retrofit.create(JenkinsApi.class);
        return jenkins.getJob(jobName).subscribeOn(Schedulers.io());//jobName);
    }

    public Observable<List<Job>> getJobs(int startIndex){
        Retrofit retrofit = NetworkProvider.buildRetrofit();

        String path = buildTreeParam(startIndex);

        JenkinsApi jenkinsService = retrofit.create(JenkinsApi.class);
        return jenkinsService
                .getJobs(path)
                .map(JobCollection::getList)
                .subscribeOn(Schedulers.io());
    }

    private String buildTreeParam(int startIndex){
        return "jobs[name,color]{" + (startIndex) + "," + (startIndex + ITEMS_PER_REQUEST) + "}";
    }

    public Observable<Build> getBuild(String jobName, int build, int startIndex){
        Retrofit retrofit = NetworkProvider.buildRetrofit();
        String tree = "artifacts[*],changeSet[items[msg,author[fullName]]],result{"+startIndex + ","+(startIndex+ITEMS_PER_REQUEST)+"}";

        JenkinsApi jenkinsService = retrofit.create(JenkinsApi.class);
        return jenkinsService.getBuild(jobName, build, tree);
    }
}
