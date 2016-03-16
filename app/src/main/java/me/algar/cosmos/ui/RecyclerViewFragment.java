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
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import me.algar.cosmos.R;

/**
 * Created by david.algar on 3/14/16.
 */
public class RecyclerViewFragment extends Fragment {
    @Bind(R.id.list)
    protected RecyclerView recyclerView;
    @Bind(R.id.swipe_refresh_container)
    protected SwipeRefreshLayout swipeRefreshLayout;

    protected LinearLayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        ButterKnife.bind(this, rootView);

        setupRecyclerView();

        return rootView;
    }

    protected void setupRecyclerView(){
        //choose some defaults
        recyclerView.setItemAnimator(new SlideInUpAnimator());
        recyclerView.setHasFixedSize(true);


        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = createAdapter();
        recyclerView.setAdapter(adapter);
    }

    //TODO override
    protected RecyclerView.Adapter createAdapter(){
        return null;
    }
}
