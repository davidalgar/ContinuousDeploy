package me.algar.cosmos;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by David on 3/13/16.
 */
public class CosmosApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new Timber.DebugTree());
    }
}
