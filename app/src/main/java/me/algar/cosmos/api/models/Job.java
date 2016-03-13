
package me.algar.cosmos.api.models;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;


public class Job {
    public List<Object> actions = new ArrayList<>();
    public String description;
    public String displayName;
    public Object displayNameOrNull;
    public String name;
    public String url;
    public Boolean buildable;
    public List<Build> builds = new ArrayList<>();
    public String color;
    public Build firstBuild;
    public List<HealthReport> healthReport = new ArrayList<>();
    public Boolean inQueue;
    public Boolean keepDependencies;
    public Build lastBuild;
    public Build lastCompletedBuild;
    public Build lastFailedBuild;
    public Build lastStableBuild;
    public Build lastSuccessfulBuild;
    public Build lastUnstableBuild;
    public Build lastUnsuccessfulBuild;
    public Integer nextBuildNumber;
    public List<Property> property = new ArrayList<>();
    @Nullable
    public Build queueItem;
    public Boolean concurrentBuild;
    public List<Object> downstreamProjects = new ArrayList<>();
    public Scm scm;
    public List<Object> upstreamProjects = new ArrayList<>();

}
