package me.algar.cosmos.api;

import me.algar.cosmos.api.models.Build;
import me.algar.cosmos.api.models.Job;
import me.algar.cosmos.api.models.JobCollection;
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

    @GET("job/{job}/api/json") //{{start},{end}}
//    @GET("posts")
    Observable<Job> getJob(@Path("job") String job);

    @GET("job/{job}/{buildId}/api/json")
    Observable<Build> getBuild(@Path("job") String job, @Path("buildId") int buildId, @Query("tree") String tree);
}