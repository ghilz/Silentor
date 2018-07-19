package com.example.alessandroghilardi.silentor;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LauncherApps;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{

        private GoogleMap mMap;
        private DatabaseHelper dbHelper;
        private ArrayList<PinItem> pinItems;
        private LocationManager mLocationManager;

        private static final long MIN_TIME = 3;
        private static final float MIN_DISTANCE = 10;

        private final LocationListener mLocationListener = new LocationListener() {
            @Override
            public void onStatusChanged(String location, int someInt, Bundle bundle){}
            @Override
            public void onProviderEnabled(String someString){}
            @Override
            public void onProviderDisabled(String someString){}



            @Override
            public void onLocationChanged(final Location location) {

                mMap.clear();

                LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());

                MarkerOptions currentMarker  = new MarkerOptions();
                currentMarker.position(currentPosition);

                currentMarker.draggable(true);
                currentMarker.icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                mMap.addMarker(currentMarker);

                mMap.moveCamera(CameraUpdateFactory.newLatLng(currentPosition));
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
                mMap.animateCamera(zoom);

                LoadMapPin();
            }
        };

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_maps);

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);

            mapFragment.getMapAsync(this);

            dbHelper = new DatabaseHelper(this);

            pinItems = new ArrayList<>();

            mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION )
                    == PackageManager.PERMISSION_GRANTED ) {

                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME,
                        MIN_DISTANCE, mLocationListener);
            }

        }

        public void LoadMapPin(){

            pinItems.addAll(dbHelper.getAllItems());

            for(int i = 0; i < pinItems.size(); i++){

                LatLng marker = new LatLng(pinItems.get(i).getLatitude(), pinItems.get(i).getLongitude());
                mMap.addMarker(new MarkerOptions().position(marker).title(pinItems.get(i).getName()));

            }
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;

            LoadMapPin();

            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){

                @Override
                public void onMapClick(LatLng point) {

                    mMap.addMarker(new MarkerOptions().position(point).title("You tapped here"));

                    PinItem newPin = new PinItem();
                    newPin.setCoordinates(point);

                    Intent intent=new Intent(MapsActivity.this, ManageNewPinActivity.class);
                    intent.putExtra("NewPinLongitude", newPin.getLongitude());
                    intent.putExtra("NewPinLatitude", newPin.getLatitude());
                    startActivity(intent);
                }
            });

        }


}
