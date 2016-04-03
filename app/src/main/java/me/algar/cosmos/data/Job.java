package me.algar.cosmos.data;

import java.util.ArrayList;
import java.util.List;

import me.algar.cosmos.api.models.Build;

public class Job implements JobModel {
    public static final String FAILURE_COLOR = "red";
    public static final String SUCCESS_COLOR = "blue";
    public String name;
    public String color;
    public List<Build> builds = new ArrayList<>();


    public static final Mapper<Job> MAPPER = new Mapper<>(new Mapper.Creator<Job>() {
        @Override
        public Job create(long _id, String name, String color) {
            return new Job(_id, name, color);
        }
    });

    public List<Build> getBuilds() {
        return builds;
    }

    public static final class Marshal extends JobMarshal<Marshal> {
        public Marshal() {
            super();
        }
    }

    private long id;
    public Job(long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
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
}
