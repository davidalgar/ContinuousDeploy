package me.algar.cosmos.api;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by David on 3/10/16.
 */
public interface JenkinsApi {
    String JENKINS = "http://127.0.0.1:8080/";

    //TODO parameterize jobname
    //http://127.0.0.1:8080/job/Cosmos%20-%20development/api/json?pretty=true
    @GET("job/Cosmos%20-%20development/api/json?pretty=true")
    Observable<Job> getJob();
}