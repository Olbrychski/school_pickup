package com.example.settingsscreen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditLocationActivity extends AppCompatActivity {

    private Button btnGetCurrentLocation;

    private LocationRequest locationRequest;
    private ProgressDialog progressDialog;
    private double latitude, longitude;
    private User user;

    AlertDialog.Builder builder;

    private static final String URL_LOCATION = "https://safechild.co.ke/safechild/set_location.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_location);

        btnGetCurrentLocation = findViewById(R.id.btn_get_location);

        builder = new AlertDialog.Builder(this);

        //getting the current user
        user = SharedPref.getInstance(this).getUser();

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        btnGetCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                builder.setTitle("Alert");
                builder.setMessage("Ensure you are at the exact location the child should be dropped or picked");
                builder.setCancelable(false);
                builder.setPositiveButton("Get location", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                            if (ContextCompat.checkSelfPermission( EditLocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION )
                                    == PackageManager.PERMISSION_GRANTED) {

                                if (isGpsEnabled()) {

                                    LocationServices.getFusedLocationProviderClient(EditLocationActivity.this)
                                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                                @Override
                                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                                    super.onLocationResult(locationResult);

                                                    LocationServices.getFusedLocationProviderClient(EditLocationActivity.this)
                                                            .removeLocationUpdates(this);

                                                    if (locationResult != null && locationResult.getLocations().size() > 0){

                                                        int index = locationResult.getLocations().size() - 1;

                                                        latitude = locationResult.getLocations().get(index).getLatitude();
                                                        longitude = locationResult.getLocations().get(index).getLongitude();

                                                        //upload coordinates to sql db
                                                        uploadCoordinates(latitude, longitude);


                                                    }

                                                }

                                            }, Looper.getMainLooper());

                                } else {

                                    turnOnGps();

                                }


                            } else {

                                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                            }

                        }

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog alert = builder.create();
                alert.show();



            }

        });

    }

    private void turnOnGps() {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(EditLocationActivity.this, "GPS is already tured on", Toast.LENGTH_SHORT).show();

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(EditLocationActivity.this, 2);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //Device does not have location
                            break;
                    }
                }
            }
        });



    }

    private boolean isGpsEnabled(){


        LocationManager locationManager = null;
        boolean isEnabled = false;

        if (locationManager == null){

            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        }

        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        return isEnabled;

    }

    private void uploadCoordinates(double latitude, double longitude){

        progressDialog = new ProgressDialog(EditLocationActivity.this);
        progressDialog.setMessage("saving location...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.setCancelable(false);
        progressDialog.show();

        //Volley request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOCATION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();

                if (response.equals("Successful")){

                    Toast.makeText(EditLocationActivity.this, "Location update successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditLocationActivity.this, MainActivity.class);
                    finish();
                    startActivity(intent);

                }else if (response.equals("No children linked")){

                    builder.setTitle("Error");
                    builder.setMessage("Sorry, you cannot set location if you don't have children added to your account");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Go back", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            Intent intent = new Intent(EditLocationActivity.this, MainActivity.class);
                            finish();
                            startActivity(intent);

                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();


                }

            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {

                builder.setTitle("Error");
                builder.setMessage("Please check your internet connection and try again!");
                builder.setCancelable(false);
                builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        uploadCoordinates(latitude,longitude);

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

            }
        }) {

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("parent_id", String.valueOf(user.getId()));
                params.put("latitude", String.valueOf(latitude));
                params.put("longitude", String.valueOf(longitude));
                return params;

            }
        };

        HttpsTrustManager.allowAllSSL();

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);

    }

}