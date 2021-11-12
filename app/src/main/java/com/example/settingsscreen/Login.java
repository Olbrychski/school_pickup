package com.example.settingsscreen;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

public class Login extends AppCompatActivity {

    private EditText edtEmail, edtPassword;
    private ProgressBar progressBar;
    private Button btn_login;
    private TextView txtSignup;

    private ProgressDialog progressDialog;

    private static final String URL_LOGIN = "https://safechild.co.ke/safechild/login.php?apicall=";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        if (SharedPref.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

        progressBar = findViewById(R.id.progressBar);
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        txtSignup = findViewById(R.id.txt_signup);

        btn_login = findViewById(R.id.button_login);

        btn_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                userLogin();

            }
        });

        txtSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Login.this, SignUpActivity.class);
                startActivity(intent);

            }
        });

    }

    private void userLogin(){

        //first getting the values
        final String email = edtEmail.getText().toString();
        final String password = edtPassword.getText().toString();

        //validating inputs
        if (TextUtils.isEmpty(email)) {
            edtEmail.setError("Please enter your Email Address");
            edtEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            edtPassword.setError("Please enter your password");
            edtPassword.requestFocus();
            return;
        }

        progressDialog = new ProgressDialog(Login.this);
        progressDialog.setMessage("Creating Account...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.setCancelable(false);
        progressDialog.show();


        //Volley request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN + "login", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();

                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(response);

                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                        //getting the user from the response
                        JSONObject userJson = obj.getJSONObject("user");

                        String role = userJson.getString("role");

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

                        if (role.equals("Driver")){

                            Intent intent = new Intent(Login.this, DriverHomeActivity.class);
                            finish();
                            startActivity(intent);

                        }else if (role.equals("Father") || role.equals("Mother")){

                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();

                        }


                    } else {

                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
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
                params.put("email", email);
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

}