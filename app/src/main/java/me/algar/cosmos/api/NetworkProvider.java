package me.algar.cosmos.api;

import android.content.Context;
import android.util.Log;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.io.File;
import java.util.concurrent.TimeUnit;

import me.algar.cosmos.CosmosApplication;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

/**
 * Created by david.algar on 3/12/16.
 */
public class NetworkProvider {
    public Context context;

    public static OkHttpClient getHttpClient(){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.d("API", message);
            }
        });
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        File cacheDirectory = CosmosApplication.appContext().getCacheDir();
        int cacheSize = 15 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(cacheDirectory, cacheSize);

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .cache(cache)
                .addNetworkInterceptor(new StethoInterceptor())
                .addNetworkInterceptor(logging)
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
