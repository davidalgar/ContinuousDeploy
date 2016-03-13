package me.algar.cosmos.api;


import me.algar.cosmos.api.models.Job;
import rx.Observable;

/**
 * Created by David on 3/10/16.
 */
public class JenkinsRequestManager {
    public Observable<Job> doStuff(String jobName){
        return new JenkinsService().getJob(jobName);
    }
}
