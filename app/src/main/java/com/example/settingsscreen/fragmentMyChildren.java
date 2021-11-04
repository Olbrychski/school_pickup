package com.example.settingsscreen;

import static android.os.Looper.getMainLooper;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class fragmentMyChildren extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Child> childrenList;

    private TextView txtDay, txtTime;
    private ImageView imgProfileImage;
    private ProgressDialog progressDialog;
    User user;

    private static final String getChildrenUrl = "https://safechild.co.ke/safechild/get_child.php";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView =  inflater.inflate(R.layout.fragment_my_children, container, false);

        //get logged in user details from sharedPref
        user = SharedPref.getInstance(getActivity()).getUser();

        imgProfileImage = myView.findViewById(R.id.parent_profile_image);


        //check if profile image exists
        if (!TextUtils.isEmpty(user.getDriverPic())){

            Glide.with(getActivity()).load(user.getDriverPic()).into(imgProfileImage);


        }else {

            Glide.with(getActivity()).load(R.drawable.ic_no_profile).into(imgProfileImage);

        }

        //set time and day
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String day = sdf.format(d);

        txtTime = myView.findViewById(R.id.txt_time);

        final Handler someHandler = new Handler(getMainLooper());
        someHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                txtTime.setText(new SimpleDateFormat("hh:mm:ss a", Locale.US).format(new Date()));
                someHandler.postDelayed(this, 1000);
            }
        }, 10);


        txtDay = myView.findViewById(R.id.txt_day);
        txtDay.setText(day);

        mRecyclerView = myView.findViewById(R.id.recyclerView);
        childrenList = new ArrayList<>();
        mAdapter = new adapterMyChildren(childrenList);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //get list of children

        getChildren(String.valueOf(user.getId()));


        return myView;
    }

    private void getChildren(String parent_id) {

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading.Please wait..."); // Setting Message
        //progressDialog.setTitle("ProgressDialog"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, getChildrenUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject object = new JSONObject(response);

                            JSONArray childArray =  object.getJSONArray("child");

                            for (int i = 0; i < childArray.length(); i++) {

                                //getting the json object of the particular index inside the array
                                JSONObject childObject = childArray.getJSONObject(i);

                                String id = childObject.getString("Id");
                                String FName = childObject.getString("FName");
                                String childClass = childObject.getString("Class");
                                String School = childObject.getString("School");
                                String ChildPic = childObject.getString("ChildPic");
                                String AdmissionNo = childObject.getString("AdmissionNo");

                                Child child = new Child(id, FName, childClass, School, ChildPic, AdmissionNo);

                                childrenList.add(child);

                            }

                            progressDialog.dismiss();

                            mRecyclerView.setAdapter(mAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }){

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("parent_id", parent_id);
                return params;

            }
        };

        HttpsTrustManager.allowAllSSL();

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //adding the string request to request queue
        requestQueue.add(stringRequest);

    }
}
