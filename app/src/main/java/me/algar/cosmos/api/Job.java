
package me.algar.cosmos.api;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;



public class Job {

    public List<Object> actions = new ArrayList<Object>();
    public String description;
    public String displayName;
    public Object displayNameOrNull;
    public String name;
    public String url;
    public Boolean buildable;
    public List<Build> builds = new ArrayList<Build>();
    public String color;
    public Build firstBuild;
    public List<HealthReport> healthReport = new ArrayList<HealthReport>();
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
    public List<Property> property = new ArrayList<Property>();
    @Nullable
    public Build queueItem;
    public Boolean concurrentBuild;
    public List<Object> downstreamProjects = new ArrayList<Object>();
    public Scm scm;
    public List<Object> upstreamProjects = new ArrayList<Object>();

}
