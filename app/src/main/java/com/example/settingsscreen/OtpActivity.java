package com.example.settingsscreen;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import java.util.HashMap;
import java.util.Map;

public class OtpActivity extends AppCompatActivity {

    private Button btnVerify;
    private EditText edtOtp;
    private TextView txtResend;
    private String otp,phoneNumber, childId, parentId, childStatus;
    private ProgressDialog progressDialog;

    private AlertDialog.Builder builder;
    private User user;


    private static final String setChildStatusUrl = "https://safechild.co.ke/safechild/set_child_status.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_otp);

        //get logged in user details from sharedPref
        user = SharedPref.getInstance(OtpActivity.this).getUser();

        Bundle bundle = getIntent().getExtras();
        otp = bundle.getString("otp");
        phoneNumber = bundle.getString("number");
        childId = bundle.getString("child_id");
        parentId = bundle.getString("parent_id");
        childStatus = bundle.getString("child_status");

        //Toast.makeText(OtpActivity.this, otp, Toast.LENGTH_SHORT).show();
        builder = new AlertDialog.Builder(this);

        //initialize views
        btnVerify = findViewById(R.id.button_verify);
        edtOtp = findViewById(R.id.edt_otp);
        txtResend = findViewById(R.id.txt_resend);

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                verifyOtp(otp);

            }
        });

    }

    private void verifyOtp(String otp) {

        String enteredOtp = edtOtp.getText().toString().trim();
        if (TextUtils.isEmpty(enteredOtp)){

            edtOtp.setError("OTP is required");
            edtOtp.requestFocus();
            return;

        }

        if (enteredOtp.equals(otp)){

            //verified
            setChildStatus(String.valueOf(user.getId()), childId, parentId, childStatus);


        }else {

            builder.setMessage("Failed the OTP entered is wrong! Don't release the child till you verify the OTP")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();

                        }
                    })
                    .setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //  Action for 'NO' Button
                            dialog.cancel();

                        }
                    });
            //Creating dialog box
            AlertDialog alert = builder.create();
            alert.show();


        }

    }

    private void setChildStatus(String driverId, String childId, String parentId, String childStatus){

        progressDialog = new ProgressDialog(OtpActivity.this);
        progressDialog.setMessage("Loading..."); // Setting Message
        //progressDialog.setTitle("ProgressDialog"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, setChildStatusUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();

                       if (response.equals("Child Status Set")){

                           builder.setMessage("Verified you can release the child.Thank You")
                                   .setCancelable(false)
                                   .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                       public void onClick(DialogInterface dialog, int id) {

                                           Intent intent = new Intent(OtpActivity.this, DriverHomeActivity.class);
                                           finish();
                                           startActivity(intent);

                                       }
                                   });
                           //Creating dialog box
                           AlertDialog alert = builder.create();
                           alert.show();

                       }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressDialog.dismiss();

                        builder.setMessage("Failed please ensure you have internet connection")
                                .setCancelable(false)
                                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        verifyOtp(otp);

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
                params.put("child_status", childStatus);
                params.put("parent_id", parentId);
                params.put("driver_id", driverId);
                params.put("child_id", childId);
                return params;

            }
        };

        HttpsTrustManager.allowAllSSL();

        RequestQueue requestQueue = Volley.newRequestQueue(OtpActivity.this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);


    }

}