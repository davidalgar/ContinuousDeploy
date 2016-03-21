package me.algar.cosmos.ui;

import java.util.ArrayList;
import java.util.List;

import me.algar.cosmos.api.JenkinsRequestManager;
import me.algar.cosmos.api.models.Build;
import me.algar.cosmos.api.models.Job;
import rx.subjects.AsyncSubject;

public class BuildListViewModel {
    private final AsyncSubject<Job> buildDataSubject;
    private JenkinsRequestManager requestManager;
    private List<Build> builds = new ArrayList<>();

    public BuildListViewModel(JenkinsRequestManager manager){
        requestManager = manager;
        this.buildDataSubject = AsyncSubject.create();
    }

    public AsyncSubject<Job> getBuildDataSubject() {
        return buildDataSubject;
    }

    public void loadBuilds(){
        requestManager.getJob("Cosmos - development", builds.size()).subscribe(buildDataSubject);
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
}
