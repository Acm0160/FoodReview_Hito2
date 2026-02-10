package com.example.foodreview;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.io.IOException;

public class AddEditReviewActivity extends AppCompatActivity {

    public static final String EXTRA_REVIEW_ID = "extra_review_id";

    private EditText etRestaurant, etCuisine, etAddress, etComment;
    private RatingBar ratingBar;
    private MaterialButton btnSave;

    // Foto
    private MaterialButton btnAddPhoto;
    private ImageView ivPhotoPreview;
    private Uri currentPhotoUri = null;

    // Audio
    private MaterialButton btnRecord, btnStop, btnPlay;
    private MediaRecorder recorder = null;
    private MediaPlayer player = null;
    private String currentAudioPath = null;

    // Ubicación
    private MaterialButton btnPickLocation;
    private TextView tvLocationInfo;
    private Double currentLat = null, currentLng = null;

    private ReviewRepository repository;
    private long editingId = -1;

    private ActivityResultLauncher<String> requestCameraPermissionLauncher;
    private ActivityResultLauncher<Uri> takePictureLauncher;

    private ActivityResultLauncher<String> requestAudioPermissionLauncher;

    private ActivityResultLauncher<Intent> pickLocationLauncher;

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

        btnAddPhoto = findViewById(R.id.btnAddPhoto);
        ivPhotoPreview = findViewById(R.id.ivPhotoPreview);

        btnRecord = findViewById(R.id.btnRecord);
        btnStop = findViewById(R.id.btnStop);
        btnPlay = findViewById(R.id.btnPlay);

        btnPickLocation = findViewById(R.id.btnPickLocation);
        tvLocationInfo = findViewById(R.id.tvLocationInfo);

        setupCamera();
        setupAudio();
        setupLocationPicker();

        // Modo edición si viene id
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

                // Foto
                if (review.getPhotoUri() != null && !review.getPhotoUri().trim().isEmpty()) {
                    currentPhotoUri = Uri.parse(review.getPhotoUri());
                    ivPhotoPreview.setVisibility(View.VISIBLE);
                    ivPhotoPreview.setImageURI(currentPhotoUri);
                }

                // Audio
                currentAudioPath = review.getAudioPath();
                btnPlay.setEnabled(currentAudioPath != null && !currentAudioPath.trim().isEmpty());

                // Ubicación
                currentLat = review.getLat();
                currentLng = review.getLng();
                updateLocationLabel();
            });
        } else {
            setTitle("Nueva reseña");
            updateLocationLabel();
        }

        btnSave.setOnClickListener(v -> onSave());
    }

    private void setupCamera() {
        requestCameraPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        launchCamera();
                    } else {
                        Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show();
                    }
                });

        takePictureLauncher =
                registerForActivityResult(new ActivityResultContracts.TakePicture(), success -> {
                    if (success && currentPhotoUri != null) {
                        ivPhotoPreview.setVisibility(View.VISIBLE);
                        ivPhotoPreview.setImageURI(currentPhotoUri);
                    }
                });

        btnAddPhoto.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                launchCamera();
            } else {
                requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA);
            }
        });
    }

    private void launchCamera() {
        try {
            File dir = new File(getFilesDir(), "images");
            if (!dir.exists()) dir.mkdirs();

            File photoFile = File.createTempFile("photo_", ".jpg", dir);

            currentPhotoUri = FileProvider.getUriForFile(
                    this,
                    getPackageName() + ".fileprovider",
                    photoFile
            );

            takePictureLauncher.launch(currentPhotoUri);

        } catch (IOException e) {
            Toast.makeText(this, "Error creando archivo de foto", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupAudio() {
        requestAudioPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        startRecording();
                    } else {
                        Toast.makeText(this, "Permiso de micrófono denegado", Toast.LENGTH_SHORT).show();
                    }
                });

        btnRecord.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                startRecording();
            } else {
                requestAudioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO);
            }
        });

        btnStop.setOnClickListener(v -> stopRecording());
        btnPlay.setOnClickListener(v -> playAudio());
    }

    private void startRecording() {
        stopPlayerIfNeeded();

        try {
            File dir = new File(getFilesDir(), "audio");
            if (!dir.exists()) dir.mkdirs();

            File audioFile = File.createTempFile("audio_", ".3gp", dir);
            currentAudioPath = audioFile.getAbsolutePath();

            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setOutputFile(currentAudioPath);

            recorder.prepare();
            recorder.start();

            btnRecord.setEnabled(false);
            btnStop.setEnabled(true);
            btnPlay.setEnabled(false);

            Toast.makeText(this, "Grabando...", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, "Error al iniciar grabación", Toast.LENGTH_SHORT).show();
            recorder = null;
            currentAudioPath = null;
            btnRecord.setEnabled(true);
            btnStop.setEnabled(false);
            btnPlay.setEnabled(false);
        }
    }

    private void stopRecording() {
        if (recorder == null) return;

        try {
            recorder.stop();
        } catch (Exception ignored) {
            // si stop falla por estado raro
        }

        try {
            recorder.release();
        } catch (Exception ignored) {
        }

        recorder = null;

        btnRecord.setEnabled(true);
        btnStop.setEnabled(false);
        btnPlay.setEnabled(currentAudioPath != null && !currentAudioPath.trim().isEmpty());

        Toast.makeText(this, "Audio guardado", Toast.LENGTH_SHORT).show();
    }

    private void playAudio() {
        if (currentAudioPath == null || currentAudioPath.trim().isEmpty()) {
            Toast.makeText(this, "No hay audio", Toast.LENGTH_SHORT).show();
            return;
        }

        stopPlayerIfNeeded();

        try {
            player = new MediaPlayer();
            player.setDataSource(currentAudioPath);
            player.prepare();
            player.start();

            Toast.makeText(this, "Reproduciendo...", Toast.LENGTH_SHORT).show();

            player.setOnCompletionListener(mp -> stopPlayerIfNeeded());

        } catch (Exception e) {
            Toast.makeText(this, "Error reproduciendo audio", Toast.LENGTH_SHORT).show();
            stopPlayerIfNeeded();
        }
    }

    private void stopPlayerIfNeeded() {
        try {
            if (player != null) {
                player.stop();
                player.release();
            }
        } catch (Exception ignored) {
        }
        player = null;
    }

    private void setupLocationPicker() {
        pickLocationLauncher =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        double lat = result.getData().getDoubleExtra(PickLocationActivity.EXTRA_LAT, Double.NaN);
                        double lng = result.getData().getDoubleExtra(PickLocationActivity.EXTRA_LNG, Double.NaN);
                        if (!Double.isNaN(lat) && !Double.isNaN(lng)) {
                            currentLat = lat;
                            currentLng = lng;
                            updateLocationLabel();
                        }
                    }
                });

        btnPickLocation.setOnClickListener(v -> {
            // Pedir permiso ubicación si hiciera falta (para mostrar "mi ubicación"),
            // pero para seleccionar en mapa no es estrictamente necesario.
            Intent i = new Intent(this, PickLocationActivity.class);

            if (currentLat != null && currentLng != null) {
                i.putExtra(PickLocationActivity.EXTRA_LAT, currentLat);
                i.putExtra(PickLocationActivity.EXTRA_LNG, currentLng);
            }

            pickLocationLauncher.launch(i);
        });
    }

    private void updateLocationLabel() {
        if (tvLocationInfo == null) return;

        if (currentLat == null || currentLng == null) {
            tvLocationInfo.setText("Ubicación: (no seleccionada)");
        } else {
            tvLocationInfo.setText("Ubicación: " + currentLat + ", " + currentLng);
        }
    }

    private void onSave() {
        String restaurant = etRestaurant.getText().toString().trim();
        String cuisine = etCuisine.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String comment = etComment.getText().toString().trim();
        int rating = (int) ratingBar.getRating();

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
        String photoUriStr = (currentPhotoUri != null) ? currentPhotoUri.toString() : null;

        if (editingId == -1) {
            Review r = new Review(
                    0,
                    restaurant,
                    rating,
                    comment,
                    cuisine,
                    address,
                    photoUriStr,
                    currentAudioPath,
                    currentLat,
                    currentLng,
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

                existing.setPhotoUri(photoUriStr);
                existing.setAudioPath(currentAudioPath);
                existing.setLat(currentLat);
                existing.setLng(currentLng);

                existing.setUpdatedAt(now);

                repository.update(existing, rows -> finish());
            });
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Limpieza segura
        if (recorder != null) {
            try { recorder.stop(); } catch (Exception ignored) {}
            try { recorder.release(); } catch (Exception ignored) {}
            recorder = null;
        }
        stopPlayerIfNeeded();
    }
}
