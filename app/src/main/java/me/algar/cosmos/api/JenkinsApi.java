package me.algar.cosmos.api;

import java.util.List;

import me.algar.cosmos.api.models.Build;
import me.algar.cosmos.api.models.Job;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by David on 3/10/16.
 */
public interface JenkinsApi {
    String JENKINS = "http://127.0.0.1:8080/";

    @GET("api/json?tree=jobs[name,color]{{start},{end}}")
    Observable<List<Job>> getJobs(@Path("start") int startIndex, @Path("end") int endIndex);

    //https://builds.apache.org/job/Abdera-trunk/api/json?tree=builds[number]&pretty=true
    @GET("job/{job}/api/json?tree=jobs{{start},{end}}")
    Observable<Job> getJob(@Path("job") String job, @Path("start") int startIndex, @Path("end") int endIndex);

    @GET("job/{job}/{buildId}/api/json?tree=artifacts[*],changeSet[items[msg,author[fullName]]],result{{start},{end}}")
    Observable<Build> getBuild(@Path("job") String job, @Path("buildId") int buildId);
}