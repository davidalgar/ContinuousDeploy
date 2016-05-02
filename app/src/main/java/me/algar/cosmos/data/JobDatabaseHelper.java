package me.algar.cosmos.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import me.algar.cosmos.data.JobModel;

public class JobDatabaseHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME_JOBS = "jobs";
    private static final int DB_VERSION = 1;

    public JobDatabaseHelper(Context context) {
        super(context, TABLE_NAME_JOBS, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(JobModel.CREATE_TABLE);
//        db.execSQL(BuildModel.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO proper migration
        db.execSQL("DROP TABLE "+ TABLE_NAME_JOBS);
        onCreate(db);
    }
}
