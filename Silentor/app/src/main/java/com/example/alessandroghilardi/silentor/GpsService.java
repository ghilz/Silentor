package com.example.alessandroghilardi.silentor;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class GpsService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) { return null; }
    private DatabaseHelper dbHelper;
    private ArrayList<PinItem> pinItems;

    private LocationManager mLocationManager;
    public long LOCATION_REFRESH_TIME = 10;
    public long LOCATION_REFRESH_DISTANCE = 0;
    public AudioManager am;

    private final LocationListener mLocationListener = new LocationListener() {

        @Override
        public void onLocationChanged(final Location location) {

            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            am = (AudioManager) getSystemService(AUDIO_SERVICE);

            Location pinLocation = location;
            pinLocation.setLatitude(37);
            pinLocation.setLongitude(-122);

            int radius = 1000;
            NotificationManager n = (NotificationManager) getApplicationContext()
                    .getSystemService(Context.NOTIFICATION_SERVICE);

            boolean notificationPolicy = n.isNotificationPolicyAccessGranted();

            if(n.isNotificationPolicyAccessGranted()) {

                for(int i = 0; i < pinItems.size(); i++) {

                    pinLocation.setLongitude(pinItems.get(i).getLongitude());
                    pinLocation.setLatitude(pinItems.get(i).getLatitude());

                    if ((pinLocation.getLongitude() - longitude) * (pinLocation.getLongitude() - longitude) +
                            (pinLocation.getLatitude() - latitude) * (pinLocation.getLatitude() - latitude) <= radius * radius) {
                        // set silent mode

                        am.setRingerMode(0);
                    } else {
                        am.setRingerMode(2);
                    }
                }
            }


        }


        @Override
        public void onStatusChanged(String location, int someInt, Bundle bundle){}
        @Override
        public void onProviderEnabled(String someString){}
        @Override
        public void onProviderDisabled(String someString){}

    };

    @Override
    public void onCreate(){
        Toast.makeText(this, "Service Created", Toast.LENGTH_LONG).show();

        dbHelper = new DatabaseHelper(this);
        pinItems = new ArrayList<>();
        pinItems.addAll(dbHelper.getAllItems());
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION )
                == PackageManager.PERMISSION_GRANTED ) {

            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                    LOCATION_REFRESH_DISTANCE, mLocationListener);
        }
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();


        return START_NOT_STICKY;
    }

}
