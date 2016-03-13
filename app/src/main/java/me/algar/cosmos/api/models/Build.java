
package me.algar.cosmos.api.models;


import java.util.ArrayList;
import java.util.List;

public class Build {
    public static final int STATUS_FAILURE = 0;
    public static final int STATUS_SUCCESS = 1;

    public Integer number;
    public int status;
    public String url;
    public List<String> commits = new ArrayList<>();

    public Build(int id, String url, int status, List<String> commits){
        this.number = id;
        this.url = url;
        this.commits.addAll(commits);
    }

    public String getResponsible(){
        //TODO
        if(commits.isEmpty()){
            return "No Changes";
        }
        return commits.get(0);
    }

    public String getBuildNumber() {
        return ""+number;
    }

    public String getStatus() {
        return status == STATUS_SUCCESS ? "success" : "failure";
    }
}
