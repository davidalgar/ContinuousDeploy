package me.algar.cosmos;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import me.algar.cosmos.api.Build;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.build_list)
    RecyclerView buildListView;
    @Bind(R.id.swipe_refresh_container)
    SwipeRefreshLayout swipeRefreshLayout;

    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private List<Build> builds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        buildListView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        buildListView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        adapter = new BuildListAdapter(builds, this);
        buildListView.setAdapter(adapter);
        buildListView.setHasFixedSize(true);
        buildListView.setItemAnimator(new SlideInUpAnimator());

        builds.addAll(createMockBuilds());
        adapter.notifyDataSetChanged();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //TODO
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private List<Build> createMockBuilds(){
        List<Build> list = new ArrayList<>();

        list.add(new Build(1, "", Build.STATUS_SUCCESS, new ArrayList<String>()));
        list.add(new Build(2, "", Build.STATUS_SUCCESS, new ArrayList<String>()));
        list.add(new Build(3, "", Build.STATUS_FAILURE, new ArrayList<String>()));
        list.add(new Build(4, "", Build.STATUS_SUCCESS, new ArrayList<String>()));
        list.add(new Build(5, "", Build.STATUS_FAILURE, new ArrayList<String>()));
        list.add(new Build(6, "", Build.STATUS_FAILURE, new ArrayList<String>()));
        list.add(new Build(7, "", Build.STATUS_SUCCESS, new ArrayList<String>()));
        list.add(new Build(8, "", Build.STATUS_SUCCESS, new ArrayList<String>()));
        list.add(new Build(9, "", Build.STATUS_SUCCESS, new ArrayList<String>()));
        list.add(new Build(10, "", Build.STATUS_FAILURE, new ArrayList<String>()));
        list.add(new Build(11, "", Build.STATUS_SUCCESS, new ArrayList<String>()));
        list.add(new Build(12, "", Build.STATUS_SUCCESS, new ArrayList<String>()));
        list.add(new Build(13, "", Build.STATUS_FAILURE, new ArrayList<String>()));

        return list;
    }

}