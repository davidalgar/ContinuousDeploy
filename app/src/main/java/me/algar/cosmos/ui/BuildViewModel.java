package me.algar.cosmos.ui;

import me.algar.cosmos.api.JenkinsRequestManager;
import rx.subjects.AsyncSubject;

public class BuildViewModel {
    private final AsyncSubject<Object> buildDataSubject;
    private JenkinsRequestManager requestManager;

    public BuildViewModel(JenkinsRequestManager manager){
        requestManager = manager;
        this.buildDataSubject = AsyncSubject.create();
    }

    public AsyncSubject<Object> getBuildDataSubject() {
        return buildDataSubject;
    }

    public void loadBuilds(){
        requestManager.doStuff("Cosmos%20-%20development").subscribe(buildDataSubject);
    }

}
