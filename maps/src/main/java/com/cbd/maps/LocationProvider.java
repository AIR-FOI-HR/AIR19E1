package com.cbd.maps;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.util.Pair;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class LocationProvider {

    private final static int ALL_PERMISSIONS_RESULT = 101;

    private Context context;

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationCallback mLocationCallback;
    private LocationRequest mlocationRequest;

    private Pair<Double, Double> latLng;

    public LocationProvider() {

    }

    /*
     * Getters&Setters
     */

    public Pair<Double, Double> getLatLng() {
        return latLng;
    }

    private void setContext(Context context) {
        this.context = context;
    }

    /*
     * Setup method to initialise API clients
     */

    public void setup(Context context, Activity activity) {
        // Building and connecting
        // to Google's API client
        setContext(context);
        latLng = new Pair<>(0., 0.);

        // Obtaining location services
        mFusedLocationProviderClient = LocationServices
                .getFusedLocationProviderClient(context);

        if (hasPermissions()) {
            mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(activity, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        latLng = new Pair<>(location.getLatitude(), location.getLongitude());
                    }
                }
            });
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.requestPermissions(new String[]{ACCESS_FINE_LOCATION}, ALL_PERMISSIONS_RESULT);
            }
        }

        createLocationRequest();

        locationCallback();

    }

    private void createLocationRequest() {
        mlocationRequest = new LocationRequest();
        mlocationRequest.setInterval(15000);
        mlocationRequest.setFastestInterval(5000);
        mlocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    private void locationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                for (Location location :
                        locationResult.getLocations()) {
                    if (location != null) {
                        latLng = new Pair<>(location.getLatitude(), location.getLongitude());
                    }
                }
            }
        };
    }

    public void resumeLocationUpdates() {
        mFusedLocationProviderClient.requestLocationUpdates(mlocationRequest,
                mLocationCallback,
                null);
    }

    public void pauseLocationUpdates() {
        mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
    }

    private boolean hasPermissions() {
        return ActivityCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

}