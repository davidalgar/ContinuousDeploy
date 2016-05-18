package me.algar.cosmos.api;

import java.util.ArrayList;
import java.util.List;

import me.algar.cosmos.data.Build;

/**
 * Created by David on 3/3/16.
 */
public class JenkinsModel {
    public static List<Build> createMockBuilds(){
        List<Build> list = new ArrayList<>();

        long now = System.currentTimeMillis();

        list.add(new Build(1, "", Build.STATUS_SUCCESS, "David", now));
        list.add(new Build(2, "", Build.STATUS_SUCCESS, "David", now));
        list.add(new Build(3, "", Build.STATUS_FAILURE, "David", now));
        list.add(new Build(4, "", Build.STATUS_SUCCESS, "David", now));
        list.add(new Build(5, "", Build.STATUS_FAILURE, "David", now));
        list.add(new Build(6, "", Build.STATUS_FAILURE, "David", now));
        list.add(new Build(7, "", Build.STATUS_SUCCESS, "David", now));
        list.add(new Build(8, "", Build.STATUS_SUCCESS, "David", now));
        list.add(new Build(9, "", Build.STATUS_SUCCESS, "David", now));
        list.add(new Build(10, "", Build.STATUS_FAILURE, "David", now));
        list.add(new Build(11, "", Build.STATUS_SUCCESS, "David", now));
        list.add(new Build(12, "", Build.STATUS_SUCCESS, "David", now));
        list.add(new Build(13, "", Build.STATUS_FAILURE, "David", now));

        return list;
    }

    public void getBuilds(){

    }
}
