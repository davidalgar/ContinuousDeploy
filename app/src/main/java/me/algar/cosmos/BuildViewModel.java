package me.algar.cosmos;

import me.algar.cosmos.api.JenkinsRequestManager;

/**
 * Created by David on 3/10/16.
 */
public class BuildViewModel {
    public BuildViewModel(){
        loadBuilds();
    }

    public void loadBuilds(){
        new JenkinsRequestManager().doStuff();
    }
}
