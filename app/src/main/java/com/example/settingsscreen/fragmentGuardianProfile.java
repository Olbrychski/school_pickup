package com.example.settingsscreen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class fragmentGuardianProfile extends Fragment {

    List<GuardianProfile> guardianProfileList;

    //the recyclerview
    RecyclerView guardianProfileRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_guardian_profile, container, false);


        guardianProfileRecyclerView = (RecyclerView) myView.findViewById(R.id.recyclerView);
        guardianProfileRecyclerView.setHasFixedSize(true);
        guardianProfileRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //initializing the productlist
        guardianProfileList = new ArrayList<>();


        guardianProfileList.add(
                new GuardianProfile(
                        "James Kinyua",
                        "Father",
                        "0712345678",
                        "james@email.com",
                        R.drawable.profile));

        guardianProfileList.add(
                new GuardianProfile(
                        "Mary Kinyua",
                        "Mother",
                        "0712345678",
                        "james@email.com",
                        R.drawable.profile));



        //creating recyclerview adapter
        GuardianProfileAdapter adapter = new GuardianProfileAdapter(getActivity(), guardianProfileList);

        //setting adapter to recyclerview
        guardianProfileRecyclerView.setAdapter(adapter);

        return myView;
    }
}
