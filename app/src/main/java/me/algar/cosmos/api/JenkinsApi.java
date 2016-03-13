package me.algar.cosmos.api;

import java.util.List;

import me.algar.cosmos.api.models.Build;
import me.algar.cosmos.api.models.Job;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by David on 3/10/16.
 */
public interface JenkinsApi {
    String JENKINS = "http://10.0.2.2:8080/";
//    String JENKINS = "http://jsonplaceholder.typicode.com";

    @GET("api/json?tree=jobs[name,color]")
    Observable<List<Job>> getJobs();

    @GET("job/{job}/api/json") //{{start},{end}}
//    @GET("posts")
    Observable<Job> getJob(@Path("job") String job);

    @GET("job/{job}/{buildId}/api/json")
    Observable<Build> getBuild(@Path("job") String job, @Path("buildId") int buildId, @Query("tree") String tree);
}