package com.codenrun.deaddropper;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import static android.R.attr.defaultValue;

public class MapsActivity extends AppCompatActivity
        implements
        GoogleMap.OnMyLocationButtonClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback {

    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mPermissionDenied = false;

    private GoogleMap mMap;

    // Instantiate buttons
    Button deployDropButton;
    Button markDropButton;

    // Instatiate textview
    TextView prompt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Assign buttons
        deployDropButton = (Button) findViewById(R.id.deployDropButton);
        markDropButton = (Button) findViewById(R.id.markDropButton);

        // Assign prompt
        prompt = (TextView) findViewById(R.id.prompt);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        mMap.setOnMyLocationButtonClickListener(this);
        enableMyLocation();
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    android.Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    /**
     * Button onClick methods
     */

    // Save coordinates and bring user to drop form
    public void deployDrop(View v) {
        prompt.setText("Deploying drop...");
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();

        Intent intent = new Intent(this, DeployDropActivity.class);
        intent.putExtra("lat", latitude);
        intent.putExtra("long", longitude);
        startActivityForResult(intent, 1);
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        prompt.setText("OnActivityResult called.");
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String dropName = data.getStringExtra("name");
                String dropDesc = data.getStringExtra("desc");
                double latitude = data.getDoubleExtra("latitude", defaultValue);
                double longitude = data.getDoubleExtra("longitude", defaultValue);

                // Add a marker at current location
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(latitude, longitude))
                        .title(dropName)
                        .snippet(dropDesc));
                prompt.setText("Marker " + dropName + " added.");
            }
        } else if (requestCode == 2){
            if (resultCode == RESULT_OK) {
                double latitude = data.getDoubleExtra("lat", defaultValue);
                double longitude = data.getDoubleExtra("long", defaultValue);
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(latitude, longitude))
                        .title("Available drop")
                        .snippet(""));
                prompt.setText("New marker added.");
            }
        } else {
            prompt.setText("requestCode invalid.");
        }
    }

    // Prompt user for coordinates of drop
    public void markDrop(View v) {
        prompt.setText("Marking drop...");
        Intent intent = new Intent(this, MarkDropActivity.class);
        startActivityForResult(intent, 2);
    }

}
