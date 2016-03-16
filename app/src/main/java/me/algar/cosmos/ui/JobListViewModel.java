package me.algar.cosmos.ui;

import me.algar.cosmos.api.JenkinsRequestManager;

/**
 * Created by david.algar on 3/12/16.
 */
public class JobListViewModel {
    private final JenkinsRequestManager requestManager;

    public JobListViewModel(JenkinsRequestManager manager) {
        this.requestManager = manager;
    }

    public void loadJobs(int startIndex) {

    }
}
