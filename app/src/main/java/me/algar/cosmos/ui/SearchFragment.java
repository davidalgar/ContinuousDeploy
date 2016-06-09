package me.algar.cosmos.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListAdapter;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.algar.cosmos.R;
import me.algar.cosmos.api.JenkinsRequestManager;
import me.algar.cosmos.data.Job;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class SearchFragment extends DialogFragment {

    @Bind(R.id.search_text)
    protected EditText searchText;
    @Bind(R.id.recyclerView)
    protected RecyclerView recyclerView;

    @Nullable
    private Subscription subscription;
    @Nullable
    private Subscription searchSubscription;
    @Nullable
    private JobListAdapter adapter;

    public SearchFragment(){
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, root);

        subscription = RxTextView.textChangeEvents(searchText)
                .debounce(50, TimeUnit.MILLISECONDS)
                .filter(changes -> searchText.getText() != null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SearchQueryObserver());

        //setup adapter
        adapter = createAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        return root;
    }

    @NonNull
    protected JobListAdapter createAdapter() {
        return new JobListAdapter(new ArrayList<>());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        subscription.unsubscribe();
    }

    private class SearchQueryObserver implements Observer<TextViewTextChangeEvent> {
        @Override
        public void onCompleted() {
            Timber.d("onCompleted()");
        }

        @Override
        public void onError(Throwable e) {
            Timber.d("OnError()");
            e.printStackTrace();
        }

        @Override
        public void onNext(TextViewTextChangeEvent textViewTextChangeEvent) {
            String searchText = textViewTextChangeEvent.text().toString();
            if(searchText.isEmpty()){
                Timber.d("empty search");
                if (adapter != null) {
                    adapter.animateTo(new ArrayList<>());
                }
                return;
            }
            Timber.d("Search text: %s", searchText);

            if (searchSubscription != null) {
                searchSubscription.unsubscribe();
            }

            searchSubscription = JenkinsRequestManager.getInstance(getContext())
                    .searchForJobs(searchText)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(jobs -> {
                        Timber.d("Got jobs: "+ jobs.size());
                        if (adapter != null) {
                            adapter.animateTo(jobs);
                        }
                    });
        }
    }
}
