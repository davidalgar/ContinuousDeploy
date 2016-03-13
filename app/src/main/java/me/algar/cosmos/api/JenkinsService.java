package me.algar.cosmos.api;

import me.algar.cosmos.api.models.Job;
import retrofit2.Retrofit;
import rx.Observable;

public class JenkinsService {
    public Observable<Job> getJob(String jobName){
        Retrofit retrofit = Network.buildRetrofit();

        JenkinsApi jenkinsService = retrofit.create(JenkinsApi.class);
        return jenkinsService.getJob(jobName);
    }

}
