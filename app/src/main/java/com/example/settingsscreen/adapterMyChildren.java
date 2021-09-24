package com.example.settingsscreen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class adapterMyChildren  extends RecyclerView.Adapter<adapterMyChildren.ExampleViewHolder>{
    private ArrayList<myChildrenItem> mExampleList;
    private Context mCtx;

    //View holder
    public static class ExampleViewHolder extends RecyclerView.ViewHolder{
        public ImageView childImageView;
        public TextView mTextViewChildName;
        public TextView mTextViewChildClass, txtViewChildStatus;


        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            childImageView = itemView.findViewById(R.id.profile_image);
            mTextViewChildName = itemView.findViewById(R.id.textview);
            mTextViewChildClass = itemView.findViewById(R.id.textViewClass);
            txtViewChildStatus = itemView.findViewById(R.id.textViewProgress);

        }
    }
    //Get data of array list into adapter
    public adapterMyChildren(ArrayList<myChildrenItem> exampleList){
        mExampleList = exampleList;


    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //add layout to adapter
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_my_children, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v);

        mCtx = parent.getContext();

        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        myChildrenItem currentItem = mExampleList.get(position);

        holder.childImageView.setImageResource(currentItem.getChildImage());
        holder.mTextViewChildName.setText(currentItem.getChildName());
        holder.mTextViewChildClass.setText(currentItem.getChildClass());
        holder.txtViewChildStatus.setText(currentItem.getChildLocation());


        switch (currentItem.getChildLocation()){

            case "In Bus":

                holder.txtViewChildStatus.setTextColor(ContextCompat.getColor(mCtx, R.color.purple_200));

                break;

            case "At Home":

                holder.txtViewChildStatus.setTextColor(ContextCompat.getColor(mCtx, R.color.grey));

                break;

        }

    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

}
