package com.example.foodreview;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FoodReviewDbHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "foodreview.db";
    public static final int DB_VERSION = 1;

    public static final String TABLE_REVIEWS = "reviews";

    public FoodReviewDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_REVIEWS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "restaurant_name TEXT NOT NULL, " +
                "rating INTEGER NOT NULL, " +
                "comment TEXT NOT NULL, " +
                "cuisine_type TEXT, " +
                "address TEXT, " +
                "created_at INTEGER NOT NULL, " +
                "updated_at INTEGER NOT NULL" +
                ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Hito 1: simple
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REVIEWS);
        onCreate(db);
    }
}
