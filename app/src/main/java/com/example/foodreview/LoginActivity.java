package com.example.foodreview;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout tilEmail, tilPassword;
    private TextInputEditText etEmail, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Si en tu XML no tienes tilEmail, puedes borrar esta línea y usar solo etEmail.
        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        MaterialButton btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(v -> {
            if (validateInputs()) {
                startActivity(new Intent(LoginActivity.this, MyReviewsActivity.class));
            }
        });
    }

    private boolean validateInputs() {
        String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        String pass = etPassword.getText() != null ? etPassword.getText().toString() : "";

        boolean ok = true;

        // Limpia errores previos
        if (tilEmail != null) tilEmail.setError(null);
        if (tilPassword != null) tilPassword.setError(null);

        // Email
        if (email.isEmpty()) {
            if (tilEmail != null) tilEmail.setError("El email es obligatorio");
            ok = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            if (tilEmail != null) tilEmail.setError("Email no válido");
            ok = false;
        }

        // Password (mínimo simple para DI)
        if (pass.trim().isEmpty()) {
            if (tilPassword != null) tilPassword.setError("La contraseña es obligatoria");
            ok = false;
        } else if (pass.length() < 6) {
            if (tilPassword != null) tilPassword.setError("Mínimo 6 caracteres");
            ok = false;
        }

        return ok;
    }
}



