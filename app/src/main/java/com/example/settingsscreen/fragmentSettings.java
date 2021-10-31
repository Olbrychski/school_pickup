package com.example.settingsscreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

public class fragmentSettings extends Fragment {

    TextView txtParentName;
    ImageView parentProfileImage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView =  inflater.inflate(R.layout.fragment_settings, container, false);

        //if the user is not logged in
        //starting the login activity
        if (!SharedPref.getInstance(getActivity()).isLoggedIn()) {
            getActivity().finish();
            startActivity(new Intent(getActivity(), Login.class));
        }

        //get logged in user details from sharedPref
        User user = SharedPref.getInstance(getActivity()).getUser();

        txtParentName = myView.findViewById(R.id.txt_parent_name);
        txtParentName.setText(user.getfName());

        parentProfileImage = myView.findViewById(R.id.profile_image);
        //check if profile image exists
        if (!user.getDriverPic().isEmpty()){

            Glide.with(getActivity()).load(user.getDriverPic()).into(parentProfileImage);

        }else {

            Glide.with(getActivity()).load(R.drawable.icon_profile).into(parentProfileImage);

        }

        return myView;
    }
}
