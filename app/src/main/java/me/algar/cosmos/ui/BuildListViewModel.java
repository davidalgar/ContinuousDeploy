package me.algar.cosmos.ui;

import java.util.ArrayList;
import java.util.List;

import me.algar.cosmos.api.JenkinsRequestManager;
import me.algar.cosmos.data.Build;
import me.algar.cosmos.data.Job;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class BuildListViewModel {
    private IBuildListView view;
    private String jobName = "**FAILURE**";

    public interface IBuildListView {
        // triggered when there are new builds to show
        void showBuilds(int rangeStart, int rangeEnd);
        void stopRefreshing();
        void startRefreshing();
    }

    private JenkinsRequestManager requestManager;
    private List<Build> builds = new ArrayList<>();

    public BuildListViewModel(JenkinsRequestManager manager, IBuildListView view){
        this.requestManager = manager;
        this.view = view;

        view.startRefreshing();
    }

    public void loadBuilds() {
         requestManager
                .getBuildsForJob(jobName, builds.size())
                 .observeOn(AndroidSchedulers.mainThread())
                 .subscribe(new BuildSubscriber());
    }

    public List<Build> getBuilds() {
        return builds;
    }

    public void refreshing() {
        builds.clear();

        loadBuilds();
    }

    public void addBuilds(List<Build> builds) {
        this.builds.addAll(builds);
    }

    public int getLastBuildPosition() {
        return builds.size();
    }

    public void destroy() {
        this.view = null;
    }

    public class BuildSubscriber extends Subscriber<List<Build>> {

        @Override
        public void onCompleted() {
            Timber.d("onCompleted()");
            view.stopRefreshing();
        }

        @Override
        public void onError(Throwable e) {
            Timber.d("onError()");
            view.stopRefreshing();

            e.printStackTrace();
        }

        @Override
        public void onNext(List<Build> builds) {
            Timber.d("onNext()");
            view.stopRefreshing();

            int start = getBuilds().size();
            addBuilds(builds);
            int end = getBuilds().size();

            view.showBuilds(start, end);
        }
    }

    public void setJob(Long jobId) {
        requestManager.getJobById(jobId)
                .subscribeOn(Schedulers.io())
                .subscribe(job -> {
                    if (job != null) {
                        this.jobName = job.name;
                    }
                    loadBuilds();
                });
    }
}
