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
    private long jobId = -1;
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
                .getBuildsForJob(jobName, jobId, builds.size())
                 .observeOn(AndroidSchedulers.mainThread())
                 .subscribe(new BuildSubscriber(builds.size()));
    }

    public List<Build> getBuilds() {
        return builds;
    }

    public void addBuilds(List<Build> newBuilds, int startIndex) {
        // Remove current page (or partial page, if its the final partial page)
        int endIndex = startIndex + newBuilds.size();
        for (int buildIndex = startIndex; buildIndex < builds.size() && buildIndex < endIndex; buildIndex++){
            this.builds.remove(buildIndex);
        }

        int buildIndex = startIndex;
        for(Build newBuild : newBuilds){
            this.builds.add(buildIndex++, newBuild);
        }
//        if(this.builds.size() >  startIndex){
//            int buildIndex = startIndex;
//            for(int i=0; i<newBuilds.size(); i++){
//                this.builds.remove(buildIndex++);
//                this.builds.add(buildIndex, newBuilds.get(i));
//            }
//        }else {
//            this.builds.addAll(newBuilds);
//        }
    }

    public void clearBuilds(){
        this.builds.clear();
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
        private final int startIndex;

        public BuildSubscriber(int startIndex) {
            this.startIndex = startIndex;
        }

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

            addBuilds(builds, startIndex);
            int end = getBuilds().size();

            view.showBuilds(startIndex, end);
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
                    loadBuilds(0);
                });
    }

    public void refresh() {
        builds.clear();
        requestManager.clearBuildCache(jobId).doOnNext(x -> loadBuilds(0));
    }
}
