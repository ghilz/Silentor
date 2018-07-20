package com.example.alessandroghilardi.silentor;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

public class ManageNewPinActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    double defaultValue = 0;
    double LATITUDE = 0;
    double LONGITUDE = 0;
    LatLng coordinates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_new_pin);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);


        Intent intent = getIntent();

        LATITUDE = intent.getDoubleExtra("NewPinLatitude", defaultValue);
        LONGITUDE = intent.getDoubleExtra("NewPinLongitude", defaultValue);

        coordinates = new LatLng(LATITUDE, LONGITUDE);

        dbHelper = new DatabaseHelper(this);

        TextView latTxt = findViewById(R.id.item_lat);
        TextView lngTxt = findViewById(R.id.item_long);

        latTxt.setText(String.valueOf(LATITUDE));
        lngTxt.setText(String.valueOf(LONGITUDE));
    }

    public void savePin(View view){

        EditText editText = findViewById(R.id.editText);
        String name = editText.getText().toString();


        PinItem newPin = new PinItem();
        newPin.setCoordinates(coordinates);
        newPin.setName(name);

        dbHelper.insertItem(newPin);
        finish();
    }

}
