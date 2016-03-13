package me.algar.cosmos.api;

import me.algar.cosmos.api.models.Job;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by David on 3/10/16.
 */
public interface JenkinsApi {
    String JENKINS = "http://127.0.0.1:8080/";

    @GET("job/{job}/api/json?pretty=true")
    Observable<Job> getJob(@Path("job") String job);
}