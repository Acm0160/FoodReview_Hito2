package com.example.foodreview;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class AddEditReviewActivity extends AppCompatActivity {

    public static final String EXTRA_REVIEW_ID = "extra_review_id";

    private EditText etRestaurant, etCuisine, etAddress, etComment;
    private RatingBar ratingBar;
    private MaterialButton btnSave;

    private ReviewRepository repository;
    private long editingId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_review);

        repository = new ReviewRepository(this);

        etRestaurant = findViewById(R.id.etRestaurant);
        etCuisine = findViewById(R.id.etCuisine);
        etAddress = findViewById(R.id.etAddress);
        etComment = findViewById(R.id.etComment);
        ratingBar = findViewById(R.id.rbRating);
        btnSave = findViewById(R.id.btnSave);

        // Modo edición si viene un id
        editingId = getIntent().getLongExtra(EXTRA_REVIEW_ID, -1);
        if (editingId != -1) {
            setTitle("Editar reseña");
            repository.getById(editingId, review -> {
                if (review == null) return;
                etRestaurant.setText(review.getRestaurantName());
                etCuisine.setText(review.getCuisineType());
                etAddress.setText(review.getAddress());
                etComment.setText(review.getComment());
                ratingBar.setRating(review.getRating());
            });
        } else {
            setTitle("Nueva reseña");
        }

        btnSave.setOnClickListener(v -> onSave());
    }

    private void onSave() {
        String restaurant = etRestaurant.getText().toString().trim();
        String cuisine = etCuisine.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String comment = etComment.getText().toString().trim();
        int rating = (int) ratingBar.getRating();

        // Validaciones mínimas Hito 1
        if (TextUtils.isEmpty(restaurant)) {
            etRestaurant.setError("Escribe el nombre del restaurante");
            return;
        }
        if (rating < 1) {
            Toast.makeText(this, "Pon una puntuación (1 a 5)", Toast.LENGTH_SHORT).show();
            return;
        }
        if (comment.length() < 5) {
            etComment.setError("Escribe un comentario un poco más largo");
            return;
        }

        long now = System.currentTimeMillis();

        if (editingId == -1) {
            Review r = new Review(
                    0,
                    restaurant,
                    rating,
                    comment,
                    cuisine,
                    address,
                    now,
                    now
            );
            repository.insert(r, newId -> finish());
        } else {
            repository.getById(editingId, existing -> {
                if (existing == null) return;
                existing.setRestaurantName(restaurant);
                existing.setRating(rating);
                existing.setComment(comment);
                existing.setCuisineType(cuisine);
                existing.setAddress(address);
                existing.setUpdatedAt(now);

                repository.update(existing, rows -> finish());
            });
        }
    }
}
