package me.algar.cosmos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import me.algar.cosmos.ui.BuildListFragment;
import me.algar.cosmos.ui.JobListFragment;
import rx.plugins.RxJavaErrorHandler;
import rx.plugins.RxJavaPlugins;

public class MainActivity extends AppCompatActivity
                implements JobListFragment.JobSelectedListener {

    private JobListFragment listFragment;
    private BuildListFragment detailFragment;

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

        listFragment = new JobListFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, listFragment)
                .commitAllowingStateLoss();
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