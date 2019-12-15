package com.cbd.maps;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cbd.database.entities.Venue;
import com.cbd.maps.places.PlacesResponse;
import com.cbd.maps.places.Result;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class LocationProvider {

    private final static int ALL_PERMISSIONS_RESULT = 101;

    private Context context;

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationCallback mLocationCallback;
    private LocationRequest mlocationRequest;
    private JsonObject jObj;
    private JsonElement jElem;
    private FirebaseFirestore db;

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

        db = FirebaseFirestore.getInstance();

        // Obtaining location services
        mFusedLocationProviderClient = LocationServices
                .getFusedLocationProviderClient(context);

        if (hasPermissions()) {
            mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(activity, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        latLng = new Pair<>(location.getLatitude(), location.getLongitude());
                        requestToPlaces();
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
                        Log.w("OLACI", latLng.toString());
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

    private void requestToPlaces() {
        String uri = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=sport+venue&location=46.3090637,16.3477807&rankby=distance&&key=AIzaSyA5SObTwWEGnFkubedir0EkJu40WGwDAzo";

        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest request =
                new StringRequest(Request.Method.GET, uri, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Gson gson = new GsonBuilder().setPrettyPrinting().create();
                            jElem = gson.fromJson(response, JsonElement.class);
                            jObj = jElem.getAsJsonObject();

                            PlacesResponse placesResponse = gson.fromJson(jObj, PlacesResponse.class);

                            for (final Result r : placesResponse.getResults()) {
                                db.collection("venues")
                                        .whereEqualTo("name", r.getFormattedAddress()).get()
                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                if (queryDocumentSnapshots.isEmpty()) {
                                                    Venue newVenue = new Venue(r.getFormattedAddress(),
                                                            r.getGeometry().getLocation().getLat(),
                                                            r.getGeometry().getLocation().getLng(),
                                                            new ArrayList<String>());
                                                    db.collection("venues").add(newVenue);
                                                }
                                            }
                                        });
                            }
                        } catch (Throwable oops) {
                            Toast.makeText(context, "Oops! Something went wrong...", Toast.LENGTH_LONG).show();
                            Log.w("OLACI", oops.toString());
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.w("OLACI", error.toString());
                    }
                });

        queue.add(request);
    }

}