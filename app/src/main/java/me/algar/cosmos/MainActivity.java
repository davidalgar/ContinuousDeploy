package me.algar.cosmos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import rx.plugins.RxJavaErrorHandler;
import rx.plugins.RxJavaPlugins;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RxJavaPlugins.getInstance().registerErrorHandler(new RxJavaErrorHandler() {
            @Override
            public void handleError(Throwable e) {
                super.handleError(e);
                e.printStackTrace();
            }
        });
    }
}