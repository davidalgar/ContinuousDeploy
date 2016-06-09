package me.algar.cosmos.ui;

import android.support.annotation.Nullable;

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
    @Nullable
    private IBuildListView view;
    private String jobName = "**FAILURE**";
    private long jobId = -1;
    @Nullable
    private Subscription jobNameSubscription;
    @Nullable
    private Subscription singleSubscription;

    private boolean allBuildsLoaded = false;

    public interface IBuildListView {
        // triggered when there are new(or updated) builds to show
        void showBuilds(int rangeStart, int rangeEnd);
        void stopRefreshing();
        void startRefreshing();
    }

    private JenkinsRequestManager requestManager;
    private List<Build> builds = new ArrayList<>();

    public BuildListViewModel(JenkinsRequestManager manager){
        this.requestManager = manager;
    }

    public void loadMoreBuilds() {
        if(allBuildsLoaded){
            return;
        }
        Subscriber<List<Build>> subscriber = new BuildSubscriber();


        // don't need the old subscription any more, since we're paging
        // (if not paging you'd only have a single subscription)
        if(singleSubscription != null) {
            singleSubscription.unsubscribe();
        }

        // re-subscribe, but to larger size query ?
        singleSubscription = requestManager
                .getBuildsForJob(jobName, jobId, builds.size())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
//        subscriptions.add(subscription);
    }

    public List<Build> getBuilds() {
        return builds;
    }

    public boolean addOrUpdateBuilds(List<Build> newBuilds) {
        return this.builds.addAll(newBuilds);
    }

    public void clearBuilds(){
        this.builds.clear();
    }

    public void destroy() {
        Timber.d("destroy() " + this);
        if(singleSubscription != null){
            singleSubscription.unsubscribe();
        }
        view = null;

        if (jobNameSubscription != null) {
            jobNameSubscription.unsubscribe();
        }
    }

    public class BuildSubscriber extends Subscriber<List<Build>> {

        public BuildSubscriber() {
        }

        @Override
        public void onNext(List<Build> builds) {
            Timber.d("Adding builds: "+builds.size());
            if (view != null) {
                view.stopRefreshing();
            }

            if(builds.isEmpty()){
                allBuildsLoaded = true;
            }

            if(addOrUpdateBuilds(builds)) {
                // find range we inserted/updated so we can notify recyclerview
                int start = getBuilds().indexOf(builds.get(0));
                int end = getBuilds().indexOf(builds.get(builds.size()-1));
                view.showBuilds(start, end);
            }
        }

        @Override
        public void onCompleted() {
            Timber.d("onCompleted()");
            if (view != null) {
                view.stopRefreshing();
            }
        }

        @Override
        public void onError(Throwable e) {
            Timber.d("onError() - " + BuildListViewModel.this);
            if (view != null) {
                view.stopRefreshing();
            }

            e.printStackTrace();
        }
    }

    public void setJob(Long jobId, IBuildListView view) {
        Timber.d("setJob(" + jobId + ")");
        this.view = view;
        view.startRefreshing();
        clearBuilds();
        this.jobId = jobId;
        jobNameSubscription = requestManager.getJobById(jobId)
                .subscribeOn(Schedulers.io())
                .subscribe(job -> {
                    if (job != null) {
                        this.jobName = job.name;
                    }
                    allBuildsLoaded = false;
                    loadMoreBuilds();
                });
    }

    public void refresh() {
        builds.clear();
        requestManager.clearBuildCache(jobId).doOnNext(x -> loadMoreBuilds());
    }
}
