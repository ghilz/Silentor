package com.example.alessandroghilardi.silentor;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.audiofx.Equalizer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static int ON_DO_NOT_DISTURB_CALLBACK_CODE = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        NotificationManager n = (NotificationManager) getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);

        // check permission for change notification
        if(!n.isNotificationPolicyAccessGranted()){
            Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            startActivityForResult( intent, MainActivity.ON_DO_NOT_DISTURB_CALLBACK_CODE );
        }

        // check permission to access location
        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION )
                != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MainActivity.ON_DO_NOT_DISTURB_CALLBACK_CODE);
        }

        // start service to keep checking my position
        startService(new Intent(this, GpsService.class));
    }

    public void openMap(View view){
        // open map to add new pins
        Intent it = new Intent(this, MapsActivity.class);
        startActivity(it);
    }

    public void manageAllPin(View view){
        // open activity to check and remove pins
        Intent it = new Intent(this, ManagerActivity.class);
        startActivity(it);
    }

    public void managerSettings(View view) {
        // open activity to modify range settings
        Intent it = new Intent(this, ManagerSettingsActivity.class);
        startActivity(it);
    }


}
