package me.algar.cosmos.api;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import me.algar.cosmos.api.models.Build;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by David on 3/3/16.
 */
public class JenkinsModel {
    public static List<Build> createMockBuilds(){
        List<Build> list = new ArrayList<>();

        list.add(new Build(1, "", Build.STATUS_SUCCESS, new ArrayList<String>()));
        list.add(new Build(2, "", Build.STATUS_SUCCESS, new ArrayList<String>()));
        list.add(new Build(3, "", Build.STATUS_FAILURE, new ArrayList<String>()));
        list.add(new Build(4, "", Build.STATUS_SUCCESS, new ArrayList<String>()));
        list.add(new Build(5, "", Build.STATUS_FAILURE, new ArrayList<String>()));
        list.add(new Build(6, "", Build.STATUS_FAILURE, new ArrayList<String>()));
        list.add(new Build(7, "", Build.STATUS_SUCCESS, new ArrayList<String>()));
        list.add(new Build(8, "", Build.STATUS_SUCCESS, new ArrayList<String>()));
        list.add(new Build(9, "", Build.STATUS_SUCCESS, new ArrayList<String>()));
        list.add(new Build(10, "", Build.STATUS_FAILURE, new ArrayList<String>()));
        list.add(new Build(11, "", Build.STATUS_SUCCESS, new ArrayList<String>()));
        list.add(new Build(12, "", Build.STATUS_SUCCESS, new ArrayList<String>()));
        list.add(new Build(13, "", Build.STATUS_FAILURE, new ArrayList<String>()));

        return list;
    }

    public void getBuilds(){

    }
}
