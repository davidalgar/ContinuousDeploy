package me.algar.cosmos.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import me.algar.cosmos.R;

/**
 * Created by david.algar on 3/14/16.
 *
 */
public class RecyclerViewFragment<T extends RecyclerView.ViewHolder>
        extends BaseFragment
        implements SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.list)
    protected RecyclerView recyclerView;
    @Bind(R.id.swipe_refresh_container)
    protected SwipeRefreshLayout swipeRefreshLayout;

    protected LinearLayoutManager layoutManager;
    protected RecyclerView.Adapter<T> adapter;


    protected RecyclerView.Adapter<T> createAdapter(){
        throw new UnsupportedOperationException("You must override createAdapter()");
    }

    @Override
    public void onRefresh() {
        throw new UnsupportedOperationException("You must override onRefresh()");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        ButterKnife.bind(this, rootView);

        setupRecyclerView();

        swipeRefreshLayout.setColorSchemeColors(
                R.color.red,
                R.color.orange,
                R.color.yellow,
                R.color.green,
                R.color.blue,
                R.color.indigo,
                R.color.violet);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = createAdapter();        recyclerView.setAdapter(adapter);


    }

    public void stopRefreshing(){
        swipeRefreshLayout.setRefreshing(false);
    }

    public void startRefreshing(){
        swipeRefreshLayout.setRefreshing(true);
    }

    protected void setupRecyclerView(){
        //choose some defaults
        recyclerView.setItemAnimator(new SlideInRightAnimator());
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        swipeRefreshLayout.setOnRefreshListener(this);
    }
}
