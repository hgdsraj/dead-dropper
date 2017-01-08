package com.codenrun.deaddropper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MarkDropActivity extends AppCompatActivity {

    Button saveButton;
    EditText latitude;
    EditText longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_drop);

        latitude = (EditText) findViewById(R.id.latText);
        longitude = (EditText) findViewById(R.id.longText);
        saveButton = (Button) findViewById(R.id.saveButton);
    }

    public void saveCoords(View view) {
        Intent resultData = new Intent();
        resultData.putExtra("lat", Double.parseDouble(latitude.getText().toString()));
        resultData.putExtra("long", Double.parseDouble(longitude.getText().toString()));
        if (getParent() == null) {
            setResult(Activity.RESULT_OK, resultData);
        } else {
            getParent().setResult(Activity.RESULT_OK, resultData);
        }
        finish();
    }
}
