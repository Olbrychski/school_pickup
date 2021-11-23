package com.example.settingsscreen;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChildPickupActivity extends AppCompatActivity {

    private static final String URL = "https://safechild.co.ke/safechild/get_parents.php";

    private List<User> parentsList;

    //the recyclerview
    private RecyclerView parentsRecyclerView;
    private RecyclerView.Adapter mAdapter;

    private ProgressDialog progressDialog;
    private String childId, childStatus;

    private AlertDialog.Builder builder;

    private static final int PERMISSION_SEND_SMS = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_child_pickup);

        Intent startingIntent = getIntent();
        childId = startingIntent.getStringExtra("child_id");
        childStatus = startingIntent.getStringExtra("status");

        builder = new AlertDialog.Builder(this);

        parentsRecyclerView = findViewById(R.id.recyclerView);

        parentsRecyclerView.setHasFixedSize(true);
        parentsRecyclerView.setLayoutManager(new LinearLayoutManager(ChildPickupActivity.this));

        parentsList = new ArrayList<>();

        mAdapter = new ChildPickupAdapter(getApplicationContext(), parentsList, childId, childStatus);

        //request sms permission
        requestSmsPermission();


    }

    private void requestSmsPermission() {

        // check permission is given
        if (ContextCompat.checkSelfPermission(ChildPickupActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            // request permission (see result in onRequestPermissionsResult() method)
            ActivityCompat.requestPermissions(ChildPickupActivity.this,
                    new String[]{Manifest.permission.SEND_SMS},
                    PERMISSION_SEND_SMS);
        } else {
            // permission already granted run sms send
            fetchParents();
        }
    }

    private void fetchParents() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..."); // Setting Message
        //progressDialog.setTitle("ProgressDialog"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();

                        try {

                            JSONObject object = new JSONObject(response);

                            JSONArray childArray =  object.getJSONArray("parents");

                            for (int i = 0; i < childArray.length(); i++) {

                                //getting the json object of the particular index inside the array
                                JSONObject childObject = childArray.getJSONObject(i);

                                //creating a new user object
                                User child = new User(
                                        childObject.getInt("Id"),
                                        childObject.getString("FName"),
                                        childObject.getString("StaffNo"),
                                        childObject.getString("PhoneNumber"),
                                        childObject.getString("EmailAddress"),
                                        childObject.getString("Role"),
                                        childObject.getString("CarNo"),
                                        childObject.getString("DriverPicture")
                                );
                                parentsList.add(child);

                            }

                            progressDialog.dismiss();

                            parentsRecyclerView.setAdapter(mAdapter);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                builder.setMessage("Failed please ensure you have internet connection")
                        .setCancelable(false)
                        .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                fetchParents();

                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                alert.show();

            }
        }){

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("child_id", childId);
                return params;

            }
        };

        HttpsTrustManager.allowAllSSL();

        RequestQueue requestQueue = Volley.newRequestQueue(ChildPickupActivity.this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_SEND_SMS: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    fetchParents();

                } else {
                    // permission denied
                    Toast.makeText(ChildPickupActivity.this, "You need to allow sms permission before proceeding", Toast.LENGTH_SHORT).show();

                }
                return;
            }
        }
    }

}