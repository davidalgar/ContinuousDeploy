package me.algar.cosmos.data;

import android.support.annotation.Nullable;

public class Build implements BuildModel {
    public static final String STATUS_FAILURE = "FAILURE";
    public static final String STATUS_SUCCESS = "SUCCESS";

    public long number;
    public String result;
    public String url;
    public String responsible; //Should always be the topmost committer from changeSet, if any exists
    public ChangeSet changeSet;

    public Build(long id, String url, String status, String responsible){
        this.number = id;
        if(url != null) {
            this.url = url;
        }else{
            this.url = "";
        }
        if(status != null) {
            this.result = status;
        }else{
            this.result = "";
        }
        if(responsible != null) {
            this.responsible = responsible;
        }else{
            this.responsible = "";
        }
    }

    public static final Mapper<Build> MAPPER = new Mapper<>((number1, status, url1, responsible1) -> new Build(number1, url1, status, responsible1));

    public String getBuildNumber() {
        return ""+number;
    }

    @Override
    public long number() {
        return number;
    }

    @Override
    public String status() {
        return result;
    }

    @Override
    public String url() {
        return url;
    }

    @Nullable
    @Override
    public String responsible() {
        return responsible;
    }

    public void generateResponsible() {
        if(changeSet != null){
            responsible = changeSet.getMostRecentAuthor();
        }else{
            responsible = Author.NONE;
        }
    }
}
