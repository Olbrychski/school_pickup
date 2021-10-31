package com.example.settingsscreen;

import static android.os.Looper.getMainLooper;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class fragmentMyChildren extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<myChildrenItem> exampleList;

    private TextView txtDay, txtTime;
    private ImageView imgProfileImage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView =  inflater.inflate(R.layout.fragment_my_children, container, false);

        //get logged in user details from sharedPref
        User user = SharedPref.getInstance(getActivity()).getUser();

        imgProfileImage = myView.findViewById(R.id.parent_profile_image);

        

        //check if profile image exists
        if (!TextUtils.isEmpty(user.getDriverPic())){

            Glide.with(getActivity()).load(R.drawable.icon_profile).into(imgProfileImage);


        }else {

            imgProfileImage.setImageDrawable(getResources().getDrawable(R.drawable.profile));

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

        exampleList = new ArrayList<>();
        exampleList.add(new myChildrenItem(R.drawable.child3, "John Rider", "Grade 2 South", "At School"));
        exampleList.add(new myChildrenItem(R.drawable.child1, "Samantha Rider", "Class 8 East", "In Bus"));
        exampleList.add(new myChildrenItem(R.drawable.child2, "Ryan Rider", "Grade 4 North", "At Home"));


        mRecyclerView = myView.findViewById(R.id.recyclerView);
        //recyclerview doesn't change size
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());

        mAdapter = new adapterMyChildren(exampleList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        return myView;
    }
}
