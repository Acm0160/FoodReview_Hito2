package com.example.foodreview;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class ReviewDao {

    private final FoodReviewDbHelper helper;

    public ReviewDao(Context context) {
        this.helper = new FoodReviewDbHelper(context);
    }

    public long insert(Review r) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("restaurant_name", r.getRestaurantName());
        cv.put("rating", r.getRating());
        cv.put("comment", r.getComment());
        cv.put("cuisine_type", r.getCuisineType());
        cv.put("address", r.getAddress());
        cv.put("created_at", r.getCreatedAt());
        cv.put("updated_at", r.getUpdatedAt());
        return db.insert(FoodReviewDbHelper.TABLE_REVIEWS, null, cv);
    }

    public int update(Review r) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("restaurant_name", r.getRestaurantName());
        cv.put("rating", r.getRating());
        cv.put("comment", r.getComment());
        cv.put("cuisine_type", r.getCuisineType());
        cv.put("address", r.getAddress());
        cv.put("updated_at", r.getUpdatedAt());
        return db.update(
                FoodReviewDbHelper.TABLE_REVIEWS,
                cv,
                "id=?",
                new String[]{ String.valueOf(r.getId()) }
        );
    }

    public int delete(long id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        return db.delete(FoodReviewDbHelper.TABLE_REVIEWS, "id=?", new String[]{ String.valueOf(id) });
    }

    public Review getById(long id) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.query(
                FoodReviewDbHelper.TABLE_REVIEWS,
                null,
                "id=?",
                new String[]{ String.valueOf(id) },
                null, null, null
        );
        Review r = null;
        if (c.moveToFirst()) {
            r = fromCursor(c);
        }
        c.close();
        return r;
    }

    public List<Review> getAll() {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.query(
                FoodReviewDbHelper.TABLE_REVIEWS,
                null,
                null,
                null,
                null, null,
                "created_at DESC"
        );

        List<Review> list = new ArrayList<>();
        while (c.moveToNext()) {
            list.add(fromCursor(c));
        }
        c.close();
        return list;
    }

    private Review fromCursor(Cursor c) {
        long id = c.getLong(c.getColumnIndexOrThrow("id"));
        String restaurantName = c.getString(c.getColumnIndexOrThrow("restaurant_name"));
        int rating = c.getInt(c.getColumnIndexOrThrow("rating"));
        String comment = c.getString(c.getColumnIndexOrThrow("comment"));
        String cuisineType = c.getString(c.getColumnIndexOrThrow("cuisine_type"));
        String address = c.getString(c.getColumnIndexOrThrow("address"));
        long createdAt = c.getLong(c.getColumnIndexOrThrow("created_at"));
        long updatedAt = c.getLong(c.getColumnIndexOrThrow("updated_at"));

        return new Review(id, restaurantName, rating, comment, cuisineType, address, createdAt, updatedAt);
    }
}
