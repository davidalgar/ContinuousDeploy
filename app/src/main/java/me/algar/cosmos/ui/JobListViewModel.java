package me.algar.cosmos.ui;

import android.support.annotation.NonNull;

import com.jakewharton.rxrelay.ReplayRelay;

import java.util.ArrayList;
import java.util.List;

import me.algar.cosmos.api.JenkinsRequestManager;
import me.algar.cosmos.data.Job;
import rx.Subscription;

public class JobListViewModel {
    private final JenkinsRequestManager requestManager;
    private List<Job> jobs = new ArrayList<>();

    private Subscription subscription;
    private ReplayRelay<List<Job>> jobDataSubject;

    public JobListViewModel(@NonNull JenkinsRequestManager manager) {
        this.requestManager = manager;
        this.jobDataSubject = ReplayRelay.create();

        loadJobs();
    }

    public ReplayRelay<List<Job>> getJobDataSubject() {
        return jobDataSubject;
    }

    public void loadJobs() {
        if(subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        subscription = requestManager
                .getJobs(jobs.size())
                .subscribe(jobDataSubject);
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public void refresh() {
        jobs.clear();
        requestManager.clearJobCache();
        loadJobs();
    }

    public void addJobs(List<Job> userDataResponse) {
        jobs.addAll(userDataResponse);
    }

    public int getLastJobPosition() {
        return jobs.size();
    }
}
