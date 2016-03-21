package me.algar.cosmos;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;

import timber.log.Timber;

/**
 * Created by David on 3/13/16.
 *
 * TODO
 *  - unit tests for viewmodel
 *  - unit test for rxjava (testsubscriber)
 *  - 
 *
 */
public class CosmosApplication extends Application {
    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new Timber.DebugTree());
        Stetho.initializeWithDefaults(this);

        appContext = this;
    }

    public static Context appContext(){
        return appContext;
    }
}
