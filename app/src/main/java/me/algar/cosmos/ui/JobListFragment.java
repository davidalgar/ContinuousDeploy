package me.algar.cosmos.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import me.algar.cosmos.R;
import me.algar.cosmos.api.JenkinsRequestManager;
import me.algar.cosmos.data.Job;
import me.algar.cosmos.util.ItemClickSupport;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

public class JobListFragment extends RecyclerViewFragment<JobListAdapter.ViewHolder> {
    private JobListViewModel viewModel;

    //Implement this interface to be notified when a job is selected
    public interface JobSelectedListener {
        void onJobSelected(long jobId);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.viewModel = new JobListViewModel(new JenkinsRequestManager(getContext()));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Timber.d("Page: " + page + "  totalItems: " + totalItemsCount);
                viewModel.loadJobs();
            }
        });

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener((recyclerView1, position, v) -> {
            Timber.d("Clicked Item!" + position);
            Job item = ((JobListAdapter)adapter).getItem(position);
            if(item==null){
                return;
            }
            switchToJobDetail(item._id());
        });

        return rootView;
    }

    private void switchToJobDetail(long id){
        if(getActivity() instanceof JobSelectedListener){
            ((JobSelectedListener)getActivity()).onJobSelected(id);
        }
    }

    @Override
    public void onRefresh() {
        viewModel.refresh();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void subscribe() {
        subscription = viewModel.getJobDataSubject()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new JobSubscriber());
    }

    @Override
    protected RecyclerView.Adapter<JobListAdapter.ViewHolder> createAdapter() {
        return new JobListAdapter(viewModel.getJobs());
    }

    public class JobSubscriber extends Subscriber<List<Job>> {
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
        public void onNext(List<Job> userDataResponse) {
            stopRefreshing();
            Timber.d("got " + userDataResponse.size());
            int rangeStart = viewModel.getLastJobPosition();
            viewModel.addJobs(userDataResponse);
            adapter.notifyItemRangeInserted(rangeStart, viewModel.getLastJobPosition());
            showMessage(getContext().getString(R.string.load_complete));
        }
    }
}
