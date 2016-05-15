package me.algar.cosmos.ui;

import java.util.ArrayList;
import java.util.List;

import me.algar.cosmos.api.JenkinsRequestManager;
import me.algar.cosmos.data.Build;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class BuildListViewModel {
    private IBuildListView view;
    private String jobName = "**FAILURE**";
    private Subscription subscription;
    private Subscription jobNameSubscription;

    public interface IBuildListView {
        // triggered when there are new builds to show
        void showBuilds(int rangeStart, int rangeEnd);
        void stopRefreshing();
        void startRefreshing();
    }

    private JenkinsRequestManager requestManager;
    private List<Build> builds = new ArrayList<>();

    public BuildListViewModel(JenkinsRequestManager manager){
        this.requestManager = manager;
    }

    public void loadBuilds(int page) {
        if(builds.size() < page * 15){
            return;
        }
         this.subscription = requestManager
                .getBuildsForJob(jobName, builds.size())
                 .observeOn(AndroidSchedulers.mainThread())
                 .subscribe(new BuildSubscriber());
    }

    public List<Build> getBuilds() {
        return builds;
    }

    public void refreshing() {
        builds.clear();

        loadBuilds(0);
    }

    public void addBuilds(List<Build> builds) {
        this.builds.addAll(builds);
    }

    public void clearBuilds(){
        this.builds.clear();
    }

    public int getLastBuildPosition() {
        return builds.size();
    }

    public void destroy() {
        Timber.d("destroy() " + this);
        if(subscription != null) {
            subscription.unsubscribe();
            jobNameSubscription.unsubscribe();
            view = null;
            subscription = null;
        }
    }

    public class BuildSubscriber extends Subscriber<List<Build>> {

        @Override
        public void onCompleted() {
            Timber.d("onCompleted()");
            view.stopRefreshing();
        }

        @Override
        public void onError(Throwable e) {
            Timber.d("onError() - " + BuildListViewModel.this);
            view.stopRefreshing();

            e.printStackTrace();
        }

        @Override
        public void onNext(List<Build> builds) {
            Timber.d("onNext()" + BuildListViewModel.this);
            view.stopRefreshing();

            int start = getBuilds().size();
            addBuilds(builds);
            int end = getBuilds().size();

            view.showBuilds(start, end);
        }
    }

    public void setJob(Long jobId, IBuildListView view) {
        Timber.d("setJob(" + jobId + ")");
        this.view = view;
        view.startRefreshing();
        clearBuilds();
        jobNameSubscription = requestManager.getJobById(jobId)
                .subscribeOn(Schedulers.io())
                .subscribe(job -> {
                    if (job != null) {
                        this.jobName = job.name;
                    }
                    loadBuilds(0);
                });
    }
}
