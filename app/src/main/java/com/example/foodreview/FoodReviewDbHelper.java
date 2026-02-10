package com.example.foodreview;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FoodReviewDbHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "foodreview.db";
    public static final int DB_VERSION = 2;

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
                "photo_uri TEXT, " +
                "audio_path TEXT, " +
                "lat REAL, " +
                "lng REAL, " +
                "created_at INTEGER NOT NULL, " +
                "updated_at INTEGER NOT NULL" +
                ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Migración de v1 -> v2
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_REVIEWS + " ADD COLUMN photo_uri TEXT");
            db.execSQL("ALTER TABLE " + TABLE_REVIEWS + " ADD COLUMN audio_path TEXT");
            db.execSQL("ALTER TABLE " + TABLE_REVIEWS + " ADD COLUMN lat REAL");
            db.execSQL("ALTER TABLE " + TABLE_REVIEWS + " ADD COLUMN lng REAL");
        }
    }
}
