package com.example.alessandroghilardi.silentor;

import android.content.pm.LauncherApps;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{//, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {

        private GoogleMap mMap;
        private DatabaseHelper dbHelper;
        private ArrayList<PinItem> pinItems;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_maps);
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);

            mapFragment.getMapAsync(this);

            dbHelper = new DatabaseHelper(this);

            pinItems = new ArrayList<>();

        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;

            pinItems.addAll(dbHelper.getAllItems());



            if(pinItems.size() == 0){
                // Add a marker in Sydney and move the camera
                LatLng sydney = new LatLng(-34, 151);
                //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            }

            for(int i = 0; i < pinItems.size(); i++){

                LatLng marker = new LatLng(pinItems.get(i).getLatitude(), pinItems.get(i).getLongitude());
                mMap.addMarker(new MarkerOptions().position(marker));

                if(i == 0){
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(marker));
                    CameraUpdate zoom = CameraUpdateFactory.zoomTo(10);
                    mMap.animateCamera(zoom);
                }
            }

            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){

                @Override
                public void onMapClick(LatLng point) {

                    mMap.addMarker(new MarkerOptions().position(point).title("You tapped here"));

                    PinItem newPin = new PinItem();
                    newPin.setCoordinates(point);

                    dbHelper.insertItem(newPin);
                }
            });
        }
}
