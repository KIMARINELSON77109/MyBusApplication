package com.bustracker.kimari_nelson.mybustracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import java.util.jar.Manifest;

/**
 * Created by kimari_nelson on 3/12/18.
 */

public class GPSTracker extends Service implements LocationListener {
    private final Context mContext;

    boolean isGPSEnabled = false;

    boolean isNetworkedEnabled = false;

    boolean canGetLocation = false;

    Location location;

    double latitude;
    double longitude;

    private static final long MIN_DISTANCE_FOR_UPDATES = 10;
    private  static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    protected LocationManager locationManager;

    public GPSTracker (Context context){
        this.mContext = context;
        getLocation();
    }

    public Location getLocation()
    {
        try
        {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            isNetworkedEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkedEnabled)
            {

            }
            else
            {
                this.canGetLocation = true;
                if (isNetworkedEnabled)
                {
                    if (ActivityCompat.checkSelfPermission((Activity)mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission((Activity)mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    {
                        return null;
                    }
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_DISTANCE_FOR_UPDATES, MIN_TIME_BW_UPDATES, this);
                    if (locationManager != null)
                    {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        if (location != null)
                        {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                if(isGPSEnabled)
                {
                    if (location == null)
                    {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_DISTANCE_FOR_UPDATES, MIN_TIME_BW_UPDATES, this);

                        if (locationManager != null)
                        {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                            if (location != null)
                            {

                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return location;
    }
    public void stopUsingGPS()
    {
        if (locationManager!=null)
        {
            if (ActivityCompat.checkSelfPermission((Activity)mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission((Activity)mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                return;
            }
            locationManager.removeUpdates(GPSTracker.this);
        }
    }

    public double getLatitude()
    {
        if(location!=null)
        {
            latitude = location.getLatitude();
        }
        return latitude;
    }

    public double getLongitude()
    {
        if(location!=null)
        {
            longitude = location.getLongitude();
        }
        return longitude;
    }

    public boolean canGetLocation()
    {
        return this.canGetLocation;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public void showSettingAlert()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        //Setting Dialog Title
        alertDialog.setTitle("GPS is Setting");

        //Setting Dialog message
        alertDialog.setMessage("GPS is not enabled. DO you want to go to setting menu");

        //On pressing Seting button
        alertDialog.setPositiveButton("setting",new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                Intent myIntent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
                mContext.startActivity(myIntent);
            }
        });

        alertDialog .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }
    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
