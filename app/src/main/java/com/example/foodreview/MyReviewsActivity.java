package com.example.foodreview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MyReviewsActivity extends AppCompatActivity {

    private ReviewRepository repository;
    private ReviewsAdapter adapter;
    private TextView tvEmpty;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reviews);

        repository = new ReviewRepository(this);

        tvEmpty = findViewById(R.id.tvEmpty);
        rv = findViewById(R.id.rvReviews);

        adapter = new ReviewsAdapter(new ReviewsAdapter.Listener() {
            @Override
            public void onEdit(Review review) {
                Intent i = new Intent(MyReviewsActivity.this, AddEditReviewActivity.class);
                i.putExtra(AddEditReviewActivity.EXTRA_REVIEW_ID, review.getId());
                startActivity(i);
            }

            @Override
            public void onDelete(Review review) {
                repository.delete(review.getId(), rows -> loadReviews());
            }
        });

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fabAdd);
        fab.setOnClickListener(v -> {
            Intent i = new Intent(MyReviewsActivity.this, AddEditReviewActivity.class);
            startActivity(i);
        });

        FloatingActionButton fabChat = findViewById(R.id.Chat);
        fabChat.setOnClickListener(v -> {
            Intent intent = new Intent(MyReviewsActivity.this, ChatActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadReviews();
    }

    private void loadReviews() {
        repository.getAll(this::renderReviews);
    }

    private void renderReviews(List<Review> reviews) {
        adapter.setData(reviews);

        boolean empty = (reviews == null || reviews.isEmpty());
        tvEmpty.setVisibility(empty ? View.VISIBLE : View.GONE);
        rv.setVisibility(empty ? View.GONE : View.VISIBLE);
    }
}
