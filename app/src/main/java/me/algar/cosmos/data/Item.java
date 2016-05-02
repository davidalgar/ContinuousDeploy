package me.algar.cosmos.data;

/**
 * Created by David on 5/1/16.
 */
public class Item {
    Author author;
    String msg;

    public String getMessage(){
        return msg;
    }

    public String getAuthor(){
        return author.fullName;
    }
}
