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

public class fragmentMyChildren extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<myChildrenItem> exampleList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView =  inflater.inflate(R.layout.fragment_my_children, container, false);


        exampleList = new ArrayList<>();
        exampleList.add(new myChildrenItem(R.drawable.profile, "Line 1", "Line 2" ));
        exampleList.add(new myChildrenItem(R.drawable.profile, "Line 3", "Line 4" ));
        exampleList.add(new myChildrenItem(R.drawable.profile, "Line 5", "Line 6" ));


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
