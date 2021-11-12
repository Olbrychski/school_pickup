package com.example.settingsscreen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DriverHomeActivity extends AppCompatActivity implements LocationListener {

    //a list to store all the products
    private List<DriverChild> childrenList;

    //the recyclerview
    private RecyclerView childrenRecyclerView;
    private RecyclerView.Adapter mAdapter;

    private ProgressDialog progressDialog;
    private FrameLayout btnStartTrip;
    private FloatingActionButton fab;

    private User user;

    private int driverId;

    PendingIntent mGeofencePendingIntent;
    public static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 100;
    private List<Geofence> mGeofenceList;
    private GoogleApiClient mGoogleApiClient;
    public static final String TAG = "Activity";
    LocationRequest mLocationRequest;
    double currentLatitude = 8.5565795, currentLongitude = 76.8810227;
    Boolean locationFound;
    protected LocationManager locationManager;
    protected LocationListener locationListener;

    private static final String URL = "https://safechild.co.ke/safechild/drivers_children.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_driver_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //get logged in user details from sharedPref
        user = SharedPref.getInstance(DriverHomeActivity.this).getUser();

        childrenRecyclerView = findViewById(R.id.children_recyclerView);

        btnStartTrip = findViewById(R.id.btn_start_trip);

        driverId = user.getId();

        //check if driver has an active trip
        if (!DriverSharedPref.getInstance(this).hasActiveTrip()) {

            btnStartTrip.setVisibility(View.VISIBLE);
            childrenRecyclerView.setVisibility(View.GONE);

        } else {

            btnStartTrip.setVisibility(View.GONE);

            fetchAllChildren(String.valueOf(user.getId()));

        }


        childrenRecyclerView.setHasFixedSize(true);
        childrenRecyclerView.setLayoutManager(new LinearLayoutManager(DriverHomeActivity.this));

        //initializing the productlist
        childrenList = new ArrayList<>();

        mAdapter = new DriverChildAdapter(getApplicationContext(), childrenList);

        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnStartTrip.setVisibility(View.GONE);

                //set trip to active
                DriverSharedPref.getInstance(getApplicationContext()).startTrip("active");

                fetchAllChildren(String.valueOf(user.getId()));

            }
        });

        //Geofence implementation starts here
        if (savedInstanceState == null) {

            mGeofenceList = new ArrayList<Geofence>();

            int resp = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
            if (resp == ConnectionResult.SUCCESS) {

                initGoogleAPIClient();

                createGeofences(currentLatitude, currentLongitude);

            } else {
                Log.e(TAG, "Your Device doesn't support Google Play Services.");
            }

            // Create the LocationRequest object
            mLocationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(1 * 1000)        // 10 seconds, in milliseconds
                    .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        }


    }

    public void initGoogleAPIClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(connectionAddListener)
                .addOnConnectionFailedListener(connectionFailedListener)
                .build();
        mGoogleApiClient.connect();
    }

    private GoogleApiClient.ConnectionCallbacks connectionAddListener =
            new GoogleApiClient.ConnectionCallbacks() {
                @Override
                public void onConnected(Bundle bundle) {
                    Log.i(TAG, "onConnected");

                    if (ActivityCompat.checkSelfPermission(DriverHomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(DriverHomeActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                    if (location == null) {
                        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, DriverHomeActivity.this);

                    } else {
                        //If everything went fine lets get latitude and longitude
                        currentLatitude = location.getLatitude();
                        currentLongitude = location.getLongitude();

                        Log.i(TAG, currentLatitude + " WORKS " + currentLongitude);

                        //createGeofences(currentLatitude, currentLongitude);
                        //registerGeofences(mGeofenceList);
                    }

                    try{
                        LocationServices.GeofencingApi.addGeofences(
                                mGoogleApiClient,
                                getGeofencingRequest(),
                                getGeofencePendingIntent()
                        ).setResultCallback(new ResultCallback<Status>() {

                            @Override
                            public void onResult(Status status) {
                                if (status.isSuccess()) {
                                    Log.i(TAG, "Saving Geofence");

                                } else {
                                    Log.e(TAG, "Registering geofence failed: " + status.getStatusMessage() +
                                            " : " + status.getStatusCode());
                                }
                            }
                        });

                    } catch (SecurityException securityException) {
                        // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
                        Log.e(TAG, "Error");
                    }
                }

                @Override
                public void onConnectionSuspended(int i) {

                    Log.e(TAG, "onConnectionSuspended");

                }
            };

    private GoogleApiClient.OnConnectionFailedListener connectionFailedListener =
            new GoogleApiClient.OnConnectionFailedListener() {
                @Override
                public void onConnectionFailed(ConnectionResult connectionResult) {
                    Log.e(TAG, "onConnectionFailed");
                }
            };

    public void createGeofences(double latitude, double longitude) {
        String id = UUID.randomUUID().toString();
        Geofence fence = new Geofence.Builder()
                .setRequestId(id)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                .setCircularRegion(latitude, longitude, 200)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .build();
        mGeofenceList.add(fence);
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        return PendingIntent.getService(this, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
    }

    private void fetchAllChildren(String driver_id) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading.Please wait..."); // Setting Message
        //progressDialog.setTitle("ProgressDialog"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject object = new JSONObject(response);

                            JSONArray childArray =  object.getJSONArray("child");

                            for (int i = 0; i < childArray.length(); i++) {

                                //getting the json object of the particular index inside the array
                                JSONObject childObject = childArray.getJSONObject(i);

                                int id = childObject.getInt("Id");
                                String FName = childObject.getString("FName");
                                String childClass = childObject.getString("Class");
                                String DropLocationLat = childObject.getString("DropLocationLat");
                                String ChildPic = childObject.getString("ChildPic");
                                String DropLocationLong = childObject.getString("DropLocationLong");
                                String AdmissionNo = childObject.getString("AdmissionNo");

                                DriverChild child = new DriverChild(id, ChildPic, FName, DropLocationLong, childClass, DropLocationLat, AdmissionNo);

                                childrenList.add(child);

                            }

                            progressDialog.dismiss();

                            childrenRecyclerView.setAdapter(mAdapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(DriverHomeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }){

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("driver_id", driver_id);
                return params;

            }
        };

        HttpsTrustManager.allowAllSSL();

        RequestQueue requestQueue = Volley.newRequestQueue(DriverHomeActivity.this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.driver_home_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case R.id.start_trip:
                Toast.makeText(getApplicationContext(),"Item 1 Selected",Toast.LENGTH_LONG).show();
                return true;

            case R.id.end_trip:

                DriverSharedPref.getInstance(getApplicationContext()).endTrip();

                return true;

            case R.id.logout:

                finish();
                SharedPref.getInstance(getApplicationContext()).logout();

                return true;

            default:
                return super.onOptionsItemSelected(item);

        }

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();

    }

}