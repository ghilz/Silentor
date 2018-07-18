package com.example.alessandroghilardi.silentor;

import android.app.IntentService;
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

public class GpsService extends IntentService {

    private DatabaseHelper dbHelper;
    private ArrayList<PinItem> pinItems;

    private LocationManager mLocationManager;
    public long LOCATION_REFRESH_TIME = 10;
    public long LOCATION_REFRESH_DISTANCE = 0;
    public AudioManager am;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) { return null; }

    @Override
    public void onHandleIntent(Intent intent){}

    public GpsService(){
        super("SilentorGPS");
    }

    private final LocationListener mLocationListener = new LocationListener() {

        @Override
        public void onLocationChanged(final Location location) {

            pinItems = new ArrayList<>();
            pinItems.addAll(dbHelper.getAllItems());

            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            am = (AudioManager) getSystemService(AUDIO_SERVICE);

            int radius = 1000;
            NotificationManager n = (NotificationManager) getApplicationContext()
                    .getSystemService(Context.NOTIFICATION_SERVICE);

            boolean notificationPolicy = n.isNotificationPolicyAccessGranted();

            if(n.isNotificationPolicyAccessGranted()) {

                if(pinItems.size() == 0){
                    am.setRingerMode(2);
                }else {

                    for (int i = 0; i < pinItems.size(); i++) {

                        double earthRadius = 6371000; //meters
                        double dLat = Math.toRadians(pinItems.get(i).getLatitude()-location.getLatitude());
                        double dLng = Math.toRadians(pinItems.get(i).getLongitude()-location.getLongitude());

                        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                                Math.cos(Math.toRadians(location.getLatitude())) *
                                        Math.cos(Math.toRadians(pinItems.get(i).getLatitude())) *
                                        Math.sin(dLng/2) * Math.sin(dLng/2);

                        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
                        float dist = (float) (earthRadius * c);


                        if (dist < 100) {
                            // set silent mode
                            am.setRingerMode(0);
                            break;
                        } else {
                            am.setRingerMode(2);
                        }
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
