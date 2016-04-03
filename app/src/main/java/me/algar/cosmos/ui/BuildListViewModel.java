package me.algar.cosmos.ui;

import java.util.ArrayList;
import java.util.List;

import me.algar.cosmos.api.JenkinsRequestManager;
import me.algar.cosmos.api.models.Build;
import me.algar.cosmos.data.Job;
import rx.Subscriber;
import rx.subjects.AsyncSubject;
import rx.Observable;
import rx.schedulers.Schedulers;

public class BuildListViewModel {
    private IBuildListView view;
    private String jobName = "Continuous Deploy - Development";

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
    public Observable<List<Build>> loadBuilds() {
        return requestManager
                .getJob(jobName, builds.size())
                .map(Job::getBuilds)
                .map(builds -> {
                    List<Build> newList = new ArrayList<>();
                    for (Build build : builds) {
                        if (build != null) {
                            newList.add(build);
                        }
                    }
                    return newList;
                });
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
            view.stopRefreshing();
        }

        @Override
        public void onError(Throwable e) {
            view.stopRefreshing();

            e.printStackTrace();
        }

        @Override
        public void onNext(List<Build> builds) {
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
                });
    }
}
