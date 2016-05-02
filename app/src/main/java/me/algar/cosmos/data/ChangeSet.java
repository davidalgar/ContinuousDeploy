package me.algar.cosmos.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 5/1/16.
 */
public class ChangeSet {
    public List<Item> items;

    public ChangeSet(){
        items = new ArrayList<>();
    }

    public String getMostRecentAuthor(){
        if(items.size() == 0){
            return Author.NONE;
        }
        return items.get(0).getAuthor();
    }

    public String getMostRecentCommitMessage(){
        if(items.size() == 0){
            return "";
        }
        return items.get(0).getMessage();
    }
}
