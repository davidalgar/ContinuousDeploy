package me.algar.cosmos;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import me.algar.cosmos.ui.BuildListFragment;
import me.algar.cosmos.ui.JobListFragment;
import me.algar.cosmos.util.RxErrorBus;
import rx.Subscriber;
import rx.plugins.RxJavaErrorHandler;
import rx.plugins.RxJavaPlugins;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity
                implements JobListFragment.JobSelectedListener {

    private JobListFragment listFragment;
    private BuildListFragment detailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View coordinatorLayout = findViewById(R.id.fragment_container);
//
//        RxJavaPlugins.getInstance().registerErrorHandler(new RxJavaErrorHandler() {
//            @Override
//            public void handleError(Throwable e) {
//                super.handleError(e);
//                e.printStackTrace();
//            }
//        });

        if(listFragment == null) {
            listFragment = new JobListFragment();
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, listFragment)
                .commitAllowingStateLoss();


        RxErrorBus.getInstance().observeErrors().subscribe(new Subscriber<RxErrorBus.Error>() {
            @Override
            public void onCompleted() {
                // do nothing
            }

            @Override
            public void onError(Throwable e) {
                // hah
            }

            @Override
            public void onNext(RxErrorBus.Error error) {
                Snackbar.make(coordinatorLayout, error.getMessage(), Snackbar.LENGTH_LONG).show();
                Timber.d(error.getThrowable().toString() + "   -  " + error.getMessage());
            }
        });
    }

    @Override
    public void onJobSelected(long jobId) {
        if(detailFragment == null){
            detailFragment = BuildListFragment.create(jobId);
        }else{
            detailFragment.setJob(jobId);
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, detailFragment)
                .addToBackStack("stack")
                .commit();
    }
}