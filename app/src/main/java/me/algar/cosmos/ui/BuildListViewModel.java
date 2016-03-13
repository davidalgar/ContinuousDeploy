package me.algar.cosmos.ui;

import me.algar.cosmos.api.JenkinsRequestManager;
import me.algar.cosmos.api.models.Job;
import rx.subjects.AsyncSubject;

public class BuildListViewModel {
    private final AsyncSubject<Job> buildDataSubject;
    private JenkinsRequestManager requestManager;

    public BuildListViewModel(JenkinsRequestManager manager){
        requestManager = manager;
        this.buildDataSubject = AsyncSubject.create();
    }

    public AsyncSubject<Job> getBuildDataSubject() {
        return buildDataSubject;
    }

    public void loadBuilds(int startIndex){
        requestManager.getJob("Cosmos - development", startIndex).subscribe(buildDataSubject);
    }
}
