package me.algar.cosmos.data;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Job implements JobModel {
    public static final String FAILURE_COLOR = "red";
    public static final String SUCCESS_COLOR = "blue";
    public String name;
    public String color;
    public List<Build> builds = new ArrayList<>();
    public long created = 0;
    public long favorite = 0;
    public long favorite_local = 0;
    public static long NOT_FAVORITE = 0;


    public static final Mapper<Job> MAPPER = new Mapper<>(
            Job::new);

    public List<Build> getBuilds() {
        return builds;
    }

    public static final class Marshal extends JobMarshal<Marshal> {
        public Marshal() {
            super();
        }
    }

    private long id;
    public Job(long id, String name, String color, long created, long favorite, long favorite_local) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.created = created;
        this.favorite = favorite;
        this.favorite_local = favorite_local;
    }

    @Override
    public long _id() {
        return id;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String color() {
        return color;
    }

    @Override
    public long created() {
        return created;
    }

    @Nullable
    @Override
    public long favorite() {
        return favorite;
    }

    @Nullable
    @Override
    public long favorite_local() {
        return favorite_local;
    }
}
