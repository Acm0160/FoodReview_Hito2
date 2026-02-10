package com.example.foodreview;

import android.app.AlertDialog;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.VH> {

    public interface Listener {
        void onEdit(Review review);
        void onDelete(Review review);
    }

    private final Listener listener;
    private final List<Review> data = new ArrayList<>();

    public ReviewsAdapter(Listener listener) {
        this.listener = listener;
    }

    public void setData(List<Review> reviews) {
        data.clear();
        if (reviews != null) data.addAll(reviews);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Review r = data.get(position);

        h.tvRestaurant.setText(r.getRestaurantName());
        h.rbRating.setRating(r.getRating());
        h.tvCommentPreview.setText(r.getComment());

        // Miniatura foto
        if (r.getPhotoUri() != null && !r.getPhotoUri().trim().isEmpty()) {
            h.ivThumb.setVisibility(View.VISIBLE);
            h.ivThumb.setImageURI(Uri.parse(r.getPhotoUri()));
        } else {
            h.ivThumb.setVisibility(View.GONE);
        }

        h.btnEdit.setOnClickListener(v -> listener.onEdit(r));

        h.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(v.getContext())
                    .setTitle("Borrar reseña")
                    .setMessage("¿Seguro que quieres borrar esta reseña?")
                    .setPositiveButton("Borrar", (d, which) -> listener.onDelete(r))
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

        h.itemView.setOnClickListener(v -> listener.onEdit(r));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        ImageView ivThumb;
        TextView tvRestaurant, tvCommentPreview;
        RatingBar rbRating;
        MaterialButton btnEdit, btnDelete;

        VH(@NonNull View itemView) {
            super(itemView);
            ivThumb = itemView.findViewById(R.id.ivThumb);
            tvRestaurant = itemView.findViewById(R.id.tvRestaurant);
            rbRating = itemView.findViewById(R.id.rbRating);
            tvCommentPreview = itemView.findViewById(R.id.tvCommentPreview);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
