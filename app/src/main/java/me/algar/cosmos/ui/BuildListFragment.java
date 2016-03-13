package me.algar.cosmos.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import me.algar.cosmos.R;
import me.algar.cosmos.api.JenkinsModel;
import me.algar.cosmos.api.JenkinsRequestManager;
import me.algar.cosmos.api.models.Build;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class BuildListFragment extends BaseFragment {
    //region Views
    @Bind(R.id.build_list)
    RecyclerView buildListView;
    @Bind(R.id.swipe_refresh_container)
    SwipeRefreshLayout swipeRefreshLayout;
    //endregion

    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private Subscription buildSubscription;

    private BuildViewModel viewModel;
    private List<Build> builds;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.viewModel = new BuildViewModel(new JenkinsRequestManager());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_build_list, container, false);
        ButterKnife.bind(this, rootView);

        this.builds = JenkinsModel.createMockBuilds();

        setupRecyclerView();

        setupRefresh();

        return rootView;
    }

    private void setupRecyclerView(){
        buildListView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        buildListView.setLayoutManager(layoutManager);

        adapter = new BuildListAdapter(builds, getContext());
        buildListView.setAdapter(adapter);
        buildListView.setItemAnimator(new SlideInUpAnimator());
    }

    private void setupRefresh(){
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.loadBuilds();
            }
        });
    }

    @Override
    protected void subscribe() {
        buildSubscription = viewModel.getBuildDataSubject()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BuildSubscriber());
    }

    private class BuildSubscriber extends Subscriber<Object> {

        @Override
        public void onCompleted() {
            swipeRefreshLayout.setRefreshing(false);

            subscribe();
        }

        @Override
        public void onError(Throwable e) {
            swipeRefreshLayout.setRefreshing(false);

            subscribe();

            showMessage("Error");
        }

        @Override
        public void onNext(Object userDataResponse) {
            showMessage(getContext().getString(R.string.load_complete));
        }
    }
}
