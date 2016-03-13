package me.algar.cosmos.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by david.algar on 3/12/16.
 */
public class Network {
    public static OkHttpClient getHttpClient(){
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();

        return okHttpClient;
    }

    public static Retrofit buildRetrofit(){
        Retrofit retrofit = new Retrofit.Builder()
                .client(getHttpClient())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(JenkinsApi.JENKINS)
                .build();

        return retrofit;
    }
}
