package me.algar.cosmos.api;


import java.util.List;

import me.algar.cosmos.api.models.Job;
import rx.Observable;

/**
 * Created by David on 3/10/16.
 */
public class JenkinsRequestManager {
    public Observable<Job> getJob(String jobName, int startIndex){
        return new JenkinsService().getJob(jobName, startIndex);
    }

    public Observable<List<Job>> getJobs(int startIndex){
        return new JenkinsService().getJobs(startIndex);
    }
}
