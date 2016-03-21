package me.algar.cosmos.data;

import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
public abstract class Jobvm implements JobModel {
    public static final Mapper<Jobvm> MAPPER = new Mapper<>(new Mapper.Creator<Jobvm>() {
        @Override
        public Jobvm create(long _id, String name, String color) {
            return new AutoValue_Jobvm(_id, name, color);
        }
    });

    public static final class Marshal extends JobMarshal<Marshal> {
        public Marshal() {
            super();
        }
    }
}
