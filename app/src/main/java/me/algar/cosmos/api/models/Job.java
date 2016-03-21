
package me.algar.cosmos.api.models;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;


public class Job {
    public String name;
    public String color;
    public List<Build> builds = new ArrayList<>();
//    public String displayName;
    public Build lastBuild;

//
//    //TODO remove the rest of the properties, not needed after all
//    public List<Object> actions = new ArrayList<>();
//    public String description;
//    public Object displayNameOrNull;
//    public String url;
//    public Boolean buildable;
//    public Build firstBuild;
//    public List<HealthReport> healthReport = new ArrayList<>();
//    public Boolean inQueue;
//    public Boolean keepDependencies;
//    public Build lastCompletedBuild;
//    public Build lastFailedBuild;
//    public Build lastStableBuild;
//    public Build lastSuccessfulBuild;
//    public Build lastUnstableBuild;
//    public Build lastUnsuccessfulBuild;
//    public Integer nextBuildNumber;
//    public List<Property> property = new ArrayList<>();
//    @Nullable
//    public Build queueItem;
//    public Boolean concurrentBuild;
//    public List<Object> downstreamProjects = new ArrayList<>();
//    public Scm scm;
//    public List<Object> upstreamProjects = new ArrayList<>();

    public String getName() {
        return name;
    }

    public String getStatus() {
        return color; //TODO 
    }
}
