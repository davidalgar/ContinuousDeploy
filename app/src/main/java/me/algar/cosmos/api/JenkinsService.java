package me.algar.cosmos.api;

import java.util.List;

import me.algar.cosmos.api.models.JobCollection;
import me.algar.cosmos.data.Job;
import retrofit2.Retrofit;
import rx.Observable;
import rx.schedulers.Schedulers;

public class JenkinsService {

    private static final int JOBS_PER_REQUEST = 50;
    public static final int BUILDS_PER_REQUEST = 15;

    public Observable<Job> getJob(String jobName, int startIndex){
        Retrofit retrofit = NetworkProvider.buildRetrofit();

        String path = createSingleJobTreeParam(startIndex);

        JenkinsApi jenkinsService = retrofit.create(JenkinsApi.class);
        return jenkinsService
                .getJob(jobName, path)
                .subscribeOn(Schedulers.io());
    }

    public Observable<List<Job>> getJobs(int startIndex, int endIndex){
        Retrofit retrofit = NetworkProvider.buildRetrofit();

        String path = createJobsTreeParam(startIndex, endIndex);

        JenkinsApi jenkinsService = retrofit.create(JenkinsApi.class);
        return jenkinsService
                .getJobs(path)
                .map(JobCollection::getList)
                .subscribeOn(Schedulers.io());
    }

    private String createJobsTreeParam(int startIndex, int endIndex){
        return "jobs[name,color]{" + (startIndex) + "," + (endIndex) + "}";
    }

    private String createSingleJobTreeParam(int startIndex){
        String tree = "builds[number,url,result,artifacts[*],changeSet[items[msg,author[fullName]]]]";
        return tree+"{" + (startIndex) + "," + (startIndex + BUILDS_PER_REQUEST) + "}";
    }

    public static int getJobEndIndex(int startIndex) {
        return startIndex + JenkinsService.JOBS_PER_REQUEST;
    }

    public static int getBuildEndIndex(int startIndex) {
        return startIndex + JenkinsService.BUILDS_PER_REQUEST;
    }


    /*
    Unused code for getting single build data
     */
//    private String createBuildTreeParam(){
//        return  "artifacts[*],changeSet[items[msg,author[fullName]]],result";
//    }
//
//    public Observable<Build> getBuild(String jobName, int build, int startIndex){
//        Retrofit retrofit = NetworkProvider.buildRetrofit();
//
//        String tree = createBuildTreeParam();
//
//        JenkinsApi jenkinsService = retrofit.create(JenkinsApi.class);
//        return jenkinsService.getBuild(jobName, build, tree);
//    }
}
