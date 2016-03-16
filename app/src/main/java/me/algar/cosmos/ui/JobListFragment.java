package me.algar.cosmos.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import me.algar.cosmos.api.JenkinsRequestManager;
import me.algar.cosmos.api.models.Job;

public class JobListFragment extends RecyclerViewFragment {
    private List<Job> jobs;
    private int startIndex = 0;
    private JobListViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.viewModel = new JobListViewModel(new JenkinsRequestManager());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        setupRefresh();

        return rootView;
    }

    private void setupRefresh(){
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startIndex = 0;
                viewModel.loadJobs(startIndex);
            }
        });

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager){
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                //TODO
//                viewModel.loadBuilds(totalItemsCount);
            }
        });
    }
}
