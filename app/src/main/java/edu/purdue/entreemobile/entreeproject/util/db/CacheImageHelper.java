package edu.purdue.entreemobile.entreeproject.util.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kane on 3/9/15.
 */
public class CacheImageHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "cache.db";

    public static final int DATABASE_ID_IMAGE_CACHE = 1;
    public static final int DATABASE_ID_QUERY_CACHE = 2;

    public static final String IMAGE_CACHE_TABLE_NAME = "CacheImage";
    public static final String IMAGE_CACHE_COLUMN_URL = "url";
    public static final String IMAGE_CACHE_COLUMN_FILENAME = "filename";
    public static final String IMAGE_CACHE_COLUMN_TIME = "time";

    public static final String QUERY_RESULT_TABLE_NAME = "CacheQuery";
    public static final String QUERY_RESULT_COLUMN_QUERYURL = "queryurl";
    public static final String QUERY_RESULT_COLUMN_RESULT = "result";
    public static final String QUERY_RESULT_COLUMN_TIME = "time";

    private static final String IMAGE_CACHE_CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS "+IMAGE_CACHE_TABLE_NAME+"("+
                    "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    IMAGE_CACHE_COLUMN_URL+" VARCHAR,"+
                    IMAGE_CACHE_COLUMN_FILENAME+" VARCHAR,"+
                    IMAGE_CACHE_COLUMN_TIME+" TIMESTAMP DEFAULT CURRENT_TIMESTAMP);";

    private static final String IMAGE_CACHE_DELETE_TABLE =
            "DROP TABLE IF EXISTS "+IMAGE_CACHE_TABLE_NAME;

    private static final String QUERY_CACHE_CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS "+QUERY_RESULT_TABLE_NAME+"("+
                    "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    QUERY_RESULT_COLUMN_QUERYURL+" VARCHAR,"+
                    QUERY_RESULT_COLUMN_RESULT+" VARCHAR,"+
                    QUERY_RESULT_COLUMN_TIME+" TIMESTAMP DEFAULT CURRENT_TIMESTAMP);";

    private static final String QUERY_CACHE_DELETE_TABLE =
            "DROP TABLE IF EXISTS "+QUERY_RESULT_TABLE_NAME;

    private int databaseId;

    public CacheImageHelper(Context context, int databaseId) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.databaseId = databaseId;
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(IMAGE_CACHE_CREATE_TABLE);
        db.execSQL(QUERY_CACHE_CREATE_TABLE);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(IMAGE_CACHE_DELETE_TABLE);
        db.execSQL(QUERY_CACHE_DELETE_TABLE);

        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}