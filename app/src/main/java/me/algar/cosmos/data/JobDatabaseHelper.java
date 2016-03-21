package me.algar.cosmos.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static me.algar.cosmos.data.JobModel.*;

public class JobDatabaseHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "jobs";
    private static final int DB_VERSION = 1;

    public JobDatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO proper migration
        db.execSQL("DROP TABLE "+TABLE_NAME);
        onCreate(db);
    }
}
