package me.algar.cosmos.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import butterknife.Bind;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import me.algar.cosmos.R;
import me.algar.cosmos.api.JenkinsRequestManager;
import me.algar.cosmos.data.Job;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

public class BuildListFragment extends RecyclerViewFragment<BuildListAdapter.ViewHolder>
        implements BuildListViewModel.IBuildListView {
    private static final String ARG_JOB_ID = "job_id";

    private BuildListViewModel viewModel;
    @Nullable
    Long jobId;

    @State
    Long autoJobId;

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
        setRetainInstance(true);
        Bundle b = getArguments();
        jobId = b.getLong(ARG_JOB_ID);
//        setRetainInstance(true);
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        this.viewModel = new BuildListViewModel(new JenkinsRequestManager(getContext()), this);
        viewModel.setJob(jobId);


        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Timber.d("Page: " + page + "  totalItems: " + totalItemsCount);

                viewModel.loadBuilds();
            }
        });

        return rootView;
    }

    protected BuildListAdapter createAdapter(){
        return new BuildListAdapter(viewModel.getBuilds());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public void onDestroyView(){
        viewModel.destroy();
        super.onDestroyView();
    }

    @Override
    public void onRefresh() {
        viewModel.refreshing();
    }

    /*** ViewModel hooks (start/stop refreshing are inherited from parent) */
    @Override
    public void showBuilds(int rangeStart, int rangeEnd) {
        adapter.notifyItemRangeInserted(rangeStart, rangeEnd);
    }

    public void setJob(Long job) {
        this.jobId = job;
        viewModel.setJob(job);
    }
}
