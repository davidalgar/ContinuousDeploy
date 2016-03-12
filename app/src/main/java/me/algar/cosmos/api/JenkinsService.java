package me.algar.cosmos.api;

import android.util.Log;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class JenkinsService {
    public Observable<Job> getJob(){
        Retrofit retrofit = new Retrofit.Builder()
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(JenkinsApi.JENKINS)
            .build();

        JenkinsService jenkinsService = retrofit.create(JenkinsService.class);
        return jenkinsService.getJob();
    }

}
