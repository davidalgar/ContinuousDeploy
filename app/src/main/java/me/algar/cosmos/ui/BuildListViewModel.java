package me.algar.cosmos.ui;

import java.util.ArrayList;
import java.util.List;

import me.algar.cosmos.api.JenkinsRequestManager;
import me.algar.cosmos.api.models.Build;
import me.algar.cosmos.data.Job;
import rx.Observable;
import rx.schedulers.Schedulers;

public class BuildListViewModel {

    public interface DataReceivedListener{
        void onBuildsReceived(List<Build> builds);
    }

    private JenkinsRequestManager requestManager;
    private List<Build> builds = new ArrayList<>();
    private String jobName;

    private DataReceivedListener listener;

    public BuildListViewModel(JenkinsRequestManager manager){
        requestManager = manager;
    }

    public Observable<List<Build>> loadBuilds(){
        return requestManager
                .getJob(jobName, builds.size())
                .map(Job::getBuilds)
                .map(builds-> {
                    List<Build> newList = new ArrayList<>();
                    for (Build build : builds) {
                        if(build!=null){
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
    }

    public void addBuilds(List<Build> builds) {
        this.builds.addAll(builds);
    }

    public int getLastBuildPosition() {
        return builds.size();
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
