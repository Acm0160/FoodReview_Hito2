package com.example.foodreview;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReviewRepository {

    public interface Callback<T> { void onResult(T data); }

    private final ReviewDao dao;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public ReviewRepository(Context context) {
        this.dao = new ReviewDao(context);
    }

    public void getAll(Callback<List<Review>> cb) {
        executor.execute(() -> {
            List<Review> list = dao.getAll();
            mainHandler.post(() -> cb.onResult(list));
        });
    }

    public void getById(long id, Callback<Review> cb) {
        executor.execute(() -> {
            Review r = dao.getById(id);
            mainHandler.post(() -> cb.onResult(r));
        });
    }

    public void insert(Review r, Callback<Long> cb) {
        executor.execute(() -> {
            long newId = dao.insert(r);
            mainHandler.post(() -> cb.onResult(newId));
        });
    }

    public void update(Review r, Callback<Integer> cb) {
        executor.execute(() -> {
            int rows = dao.update(r);
            mainHandler.post(() -> cb.onResult(rows));
        });
    }

    public void delete(long id, Callback<Integer> cb) {
        executor.execute(() -> {
            int rows = dao.delete(id);
            mainHandler.post(() -> cb.onResult(rows));
        });
    }
}
