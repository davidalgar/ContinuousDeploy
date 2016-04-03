package me.algar.cosmos.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import icepick.Icepick;
import icepick.State;
import me.algar.cosmos.R;
import me.algar.cosmos.api.JenkinsRequestManager;
import me.algar.cosmos.api.models.Build;
import rx.Subscriber;
import timber.log.Timber;

public class BuildListFragment extends RecyclerViewFragment<BuildListAdapter.ViewHolder> {
    private static final String ARG_JOB_ID = "job_id";
    private BuildListViewModel viewModel;
    @Nullable
    Long jobId;


    public static BuildListFragment create(long jobId) {
        Bundle args = new Bundle();
        args.putLong(ARG_JOB_ID, jobId);
        BuildListFragment frag = new BuildListFragment();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        jobId = b.getLong(ARG_JOB_ID);
        this.viewModel = new BuildListViewModel(new JenkinsRequestManager(getContext()));
        viewModel.setJob(jobId);
//        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        setupRefresh();
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Timber.d("Page: " + page + "  totalItems: " + totalItemsCount);
                viewModel.loadBuilds();
            }
        });

        return rootView;
    }

    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void setupRefresh() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            viewModel.refreshing();
            viewModel.loadBuilds();
        });
    }

    @Override
    public void onRefresh() {
        viewModel.refreshing();
        viewModel.loadBuilds();
    }

    @Override
    protected void subscribe() {
        subscription = viewModel.loadBuilds()
                .subscribe(new BuildSubscriber());
    }

    @Override
    protected RecyclerView.Adapter<BuildListAdapter.ViewHolder> createAdapter() {
        return new BuildListAdapter(viewModel.getBuilds());
    }

    private class BuildSubscriber extends Subscriber<List<Build>> {

        @Override
        public void onCompleted() {
            stopRefreshing();

            Timber.d("onCompleted()");
        }

        @Override
        public void onError(Throwable e) {
            stopRefreshing();

            Timber.d("onError()");
            e.printStackTrace();

            showMessage("Error");
        }

        @Override
        public void onNext(List<Build> userDataResponse) {
            stopRefreshing();
            Timber.d("got " + userDataResponse.size() + " builds");
            int rangeStart = adapter.getItemCount();
            viewModel.addBuilds(userDataResponse);
            adapter.notifyItemRangeInserted(rangeStart, viewModel.getLastBuildPosition());

            showMessage(getContext().getString(R.string.load_complete));
        }
    }
}
