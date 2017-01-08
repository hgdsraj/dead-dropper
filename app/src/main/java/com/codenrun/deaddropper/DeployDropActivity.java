package com.codenrun.deaddropper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;

import static android.R.attr.defaultValue;

public class DeployDropActivity extends AppCompatActivity {

    private GoogleMap mMap;

    Button saveButton;
    TextView header;
    TextView prompt;
    EditText dropName;
    EditText dropDesc;

    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deploy_drop);

        saveButton = (Button) findViewById(R.id.saveButton);
        header = (TextView) findViewById(R.id.header);
        prompt = (TextView) findViewById(R.id.prompt);
        dropName = (EditText) findViewById(R.id.nameText);
        dropDesc = (EditText) findViewById(R.id.descText);

        Intent intent = getIntent();

        latitude = intent.getDoubleExtra("lat", defaultValue);
        longitude = intent.getDoubleExtra("long", defaultValue);
        prompt.setText(latitude + " " + longitude);
    }

    public void saveDrop(View view) {
        /*
        // Add a marker at current location
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title(dropName.toString())
                .snippet(dropDesc.toString() + "\n" +
                         latitude + " " + longitude));
         */
        Intent resultData = new Intent();
        resultData.putExtra("name", dropName.getText().toString());
        resultData.putExtra("desc", dropDesc.getText().toString() + "\n" + latitude + " " + longitude);
        resultData.putExtra("latitude", latitude);
        resultData.putExtra("longitude", longitude);

        if (getParent() == null) {
            setResult(Activity.RESULT_OK, resultData);
        } else {
            getParent().setResult(Activity.RESULT_OK, resultData);
        }
        finish();
    }
}
