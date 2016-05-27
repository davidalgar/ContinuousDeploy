package me.algar.cosmos.util;

import java.util.ArrayList;
import java.util.List;

public class StreamUtils {
    public static <T> List<T> notNull(List<T> list){
        List<T> result = new ArrayList<T>();
        for(T item : list){
            if(item != null){
                result.add(item);
            }
        }
        return result;
    }
}
