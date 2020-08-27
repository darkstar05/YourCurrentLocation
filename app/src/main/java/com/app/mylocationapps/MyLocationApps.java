package com.app.mylocationapps;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public abstract class MyLocationApps extends Service {
    LocationManager locMan;
    Location curLocation;
    Boolean locationChanged;

    Handler handler = new Handler();

    LocationListener gpsListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (curLocation == null) {
                curLocation = location;
                locationChanged = true;
            }
            if (curLocation.getLatitude() == location.getLatitude() && curLocation.getLongitude() == location.getLongitude())
                locationChanged = false;
            else
                locationChanged = true;

            curLocation = location;
            if (locationChanged)
                locMan.removeUpdates(gpsListener);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            if (status == 0) {

            } else if (status == 1) {

            } else if (status == 2) {

            }
        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    @Override
    public void onCreate() {
        Toast.makeText(getBaseContext(), "Inside onCreate of service", Toast.LENGTH_LONG).show();

        Log.e(TAG,"Inside onCreate of Service");
        super.onCreate();

        locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gpsListener);
        if (curLocation != null) {
            double lat = curLocation.getLatitude();
            double lng = curLocation.getLongitude();
            Toast.makeText(getBaseContext(), "Lat : " + String.valueOf(lat) + "\n Long : " + String.valueOf(lng), Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(getBaseContext(), "Didn't Get any location", Toast.LENGTH_LONG).show();
        }
    }

    final String TAG = "LocationService";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(getBaseContext(), "Inside onStartCommand of Service", Toast.LENGTH_LONG).show();
        Log.e(TAG, "Inside onStartCommand of Service");



        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onLowMemory() {
        // TODO Auto-generated method stub
        super.onLowMemory();
    }


    @Override
    public void onStart(Intent i, int startId) {
        Toast.makeText(getBaseContext(), "Inside onStart of Service", Toast.LENGTH_LONG).show();
        Log.e(TAG, "Inside onStart of Service");

        handler.postDelayed(GpsFinder, 5000);// will start after 5 seconds
    }

    public IBinder onBind(Intent arg0) {
        Log.e(TAG, "Inside onBind of Service");
        return null;
    }

    public Runnable GpsFinder = new Runnable() {

        public void run() {
            // TODO Auto-generated method stub

            if (locMan.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }
                locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gpsListener);
            }
            else
            {
                getApplicationContext().startActivity(new Intent("android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS"));
            }
            if (curLocation != null) {
                double lat = curLocation.getLatitude();
                double lng = curLocation.getLongitude();
                Toast.makeText(getBaseContext(),"Lat : " + String.valueOf(lat) + "\n Long : "+ String.valueOf(lng), Toast.LENGTH_LONG).show();

            }

            handler.postDelayed(GpsFinder,5000);// register again to start after 5 seconds...


        }
    };

}

