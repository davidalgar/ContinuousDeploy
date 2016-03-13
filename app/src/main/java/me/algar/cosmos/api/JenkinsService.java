package me.algar.cosmos.api;

import java.util.List;

import me.algar.cosmos.api.models.Job;
import retrofit2.Retrofit;
import rx.Observable;

public class JenkinsService {
    static final int ITEMS_PER_REQUEST = 5;

    public Observable<Job> getJob(String jobName, int startIndex){
        Retrofit retrofit = Network.buildRetrofit();

        JenkinsApi jenkinsService = retrofit.create(JenkinsApi.class);
        return jenkinsService.getJob(jobName, startIndex, startIndex + ITEMS_PER_REQUEST);
    }

    public Observable<List<Job>> getJobs(int startIndex){
        Retrofit retrofit = Network.buildRetrofit();

        JenkinsApi jenkinsService = retrofit.create(JenkinsApi.class);
        return jenkinsService.getJobs(startIndex, startIndex + ITEMS_PER_REQUEST);
    }

}
