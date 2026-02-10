package com.example.foodreview;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class PickLocationActivity extends AppCompatActivity {

    public static final String EXTRA_LAT = "extra_lat";
    public static final String EXTRA_LNG = "extra_lng";

    private GoogleMap map;
    private Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_location);
        setTitle("Elige ubicación");

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        if (mapFragment == null) {
            Toast.makeText(this, "No se pudo cargar el mapa", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        mapFragment.getMapAsync(googleMap -> {
            map = googleMap;

            // Coordenadas iniciales
            double lat = getIntent().getDoubleExtra(EXTRA_LAT, 40.4168); // Madrid por defecto
            double lng = getIntent().getDoubleExtra(EXTRA_LNG, -3.7038);

            LatLng initial = new LatLng(lat, lng);
            marker = map.addMarker(new MarkerOptions().position(initial).title("Ubicación seleccionada"));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(initial, 14f));

            map.setOnMapClickListener(point -> {
                if (marker != null) marker.remove();
                marker = map.addMarker(new MarkerOptions().position(point).title("Ubicación seleccionada"));

                Intent data = new Intent();
                data.putExtra(EXTRA_LAT, point.latitude);
                data.putExtra(EXTRA_LNG, point.longitude);
                setResult(RESULT_OK, data);
                finish();
            });
        });
    }
}
