package com.example.settingsscreen;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    private String[] role = {"Select", "Father", "Mother"};

    private String userRole;

    private EditText edtFName, edtEmail, edtPhoneNumber, edtPassword;
    private Button btn_signup;
    private TextView txtLogin;

    private ProgressDialog progressDialog;

    private static final String URL_SIGNUP = "https://safechild.co.ke/safechild/login.php?apicall=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_sign_up);

        //initialize views
        Spinner spin = findViewById(R.id.spinner);
        edtFName = findViewById(R.id.edt_fullname);
        edtEmail = findViewById(R.id.edt_email);
        edtPhoneNumber = findViewById(R.id.edt_phone);
        edtPassword = findViewById(R.id.edt_password);
        txtLogin = findViewById(R.id.txt_login);

        btn_signup = findViewById(R.id.btn_signup);

        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SignUpActivity.this, Login.class);
                startActivity(intent);

            }
        });

        spin.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the roles list
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, role);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                registerUser();

            }
        });


    }

    private void registerUser() {

        final String fName = edtFName.getText().toString().trim();
        final String email = edtEmail.getText().toString().trim();
        final String password = edtPassword.getText().toString().trim();
        final String phoneNumber = edtPhoneNumber.getText().toString().trim();

        //first we will do the validations

        if (TextUtils.isEmpty(fName)) {
            edtFName.setError("Please enter your full name");
            edtFName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            edtEmail.setError("Please enter your email");
            edtEmail.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Enter a valid email");
            edtEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            edtPassword.setError("Enter a password");
            edtPassword.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(phoneNumber)) {
            edtPhoneNumber.setError("Enter your phone number");
            edtPhoneNumber.requestFocus();
            return;
        }

        if (userRole.equals("Select")){

            Toast.makeText(SignUpActivity.this, "Please select your role", Toast.LENGTH_SHORT).show();
            return;

        }

        progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setMessage("Creating Account...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.setCancelable(false);
        progressDialog.show();

        //Volley request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SIGNUP + "signup", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();
                Toast.makeText(SignUpActivity.this, response, Toast.LENGTH_SHORT).show();

                //converting response to json object
                try {

                    JSONObject obj = new JSONObject(response);

                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                        //getting the user from the response
                        JSONObject userJson = obj.getJSONObject("user");

                        //creating a new user object
                        User user = new User(
                                userJson.getInt("id"),
                                userJson.getString("fName"),
                                userJson.getString("idNo"),
                                userJson.getString("phoneNumber"),
                                userJson.getString("emailAddress"),
                                userJson.getString("role"),
                                userJson.getString("carNo"),
                                userJson.getString("driverPic")
                        );

                        //storing the user in shared preferences
                        SharedPref.getInstance(getApplicationContext()).userLogin(user);

                        //starting the profile activity
                        finish();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
                    }

                    } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("fName", fName);
                params.put("email", email);
                params.put("phoneNumber", phoneNumber);
                params.put("role", userRole);
                params.put("password", password);
                return params;

            }
        };

        HttpsTrustManager.allowAllSSL();

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        switch (role[i]){

            case "Select":
                userRole = "Select";

                break;

            case "Father":
                userRole = "Father";

                break;

            case "Mother":
                userRole = "Mother";

                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}