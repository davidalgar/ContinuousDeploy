package me.algar.cosmos.api;

import android.util.Log;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by David on 3/10/16.
 */
public class JenkinsRequestManager {


    public void doStuff(){
        new JenkinsService().getJob().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Job>() {
                    @Override
                    public void call(Job current) {
                        Log.e("Latest Job", current.lastBuild.number.toString());
                    }
                });
    }
}
