package me.algar.cosmos.api;

import me.algar.cosmos.data.Build;
import me.algar.cosmos.api.models.JobCollection;
import me.algar.cosmos.data.Job;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface JenkinsApi {
    //TODO pick up from sharedPref, allow user to change it
//    String JENKINS = "http://10.0.2.2:8080/";
    String JENKINS = "https://builds.apache.org/";

    @GET("api/json")
    Observable<JobCollection> getJobs(@Query("tree") String treeParameter);

    @GET("job/{job}/api/json")
    Observable<Job> getJob(@Path("job") String job, @Query("tree") String treeParameter);

    @GET("job/{job}/{buildId}/api/json")
    Observable<Build> getBuild(@Path("job") String job, @Path("buildId") int buildId, @Query("tree") String tree);
}