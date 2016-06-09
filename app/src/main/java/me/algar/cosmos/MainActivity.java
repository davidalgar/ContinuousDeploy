package me.algar.cosmos;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import me.algar.cosmos.ui.BuildListFragment;
import me.algar.cosmos.ui.JobListFragment;
import me.algar.cosmos.ui.SearchFragment;
import me.algar.cosmos.util.RxErrorBus;
import rx.Subscriber;
import rx.plugins.RxJavaErrorHandler;
import rx.plugins.RxJavaPlugins;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity
                implements JobListFragment.JobSelectedListener {

    private static final String TAG_SEARCH = "search";
    private static final String TAG_DETAIL = "search";
    private static final String TAG_LIST = "search";

    private JobListFragment listFragment;
    private BuildListFragment detailFragment;
    private SearchFragment searchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View coordinatorLayout = findViewById(R.id.fragment_container);

        listFragment = (JobListFragment) getSupportFragmentManager().findFragmentByTag(TAG_LIST);
        if(listFragment == null) {
            listFragment = new JobListFragment();
        }

        searchFragment = (SearchFragment) getSupportFragmentManager().findFragmentByTag(TAG_SEARCH);
        if(searchFragment == null){
            searchFragment = new SearchFragment();
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, listFragment, TAG_LIST)
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_search:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, searchFragment, TAG_SEARCH)
                        .addToBackStack("stack")
                        .commit();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onJobSelected(long jobId) {
        detailFragment = (BuildListFragment) getSupportFragmentManager().findFragmentByTag(TAG_DETAIL);
        if(detailFragment == null){
            detailFragment = BuildListFragment.create(jobId);
        }else{
            detailFragment.setJob(jobId);
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, detailFragment, TAG_DETAIL)
                .addToBackStack("stack")
                .commit();
    }
}