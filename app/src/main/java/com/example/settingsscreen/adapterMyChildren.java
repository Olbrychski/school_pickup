package com.example.settingsscreen;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class adapterMyChildren  extends RecyclerView.Adapter<adapterMyChildren.ExampleViewHolder>{
    private List<Child> mExampleList;
    private Context mCtx;

    //View holder
    public static class ExampleViewHolder extends RecyclerView.ViewHolder{
        public ImageView childImageView;
        public TextView mTextViewChildName;
        public TextView mTextViewChildClass, txtViewChildStatus, txtSchool;
        public LinearLayout childLayout;


        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            childImageView = itemView.findViewById(R.id.profile_image);
            mTextViewChildName = itemView.findViewById(R.id.textview);
            mTextViewChildClass = itemView.findViewById(R.id.textViewClass);
            txtViewChildStatus = itemView.findViewById(R.id.textViewProgress);
            childLayout = itemView.findViewById(R.id.child_layout);
            txtSchool = itemView.findViewById(R.id.textView2);

        }
    }
    //Get data of array list into adapter
    public adapterMyChildren(List<Child> exampleList){
        this.mExampleList = exampleList;



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
        Child currentItem = mExampleList.get(position);

        if (!TextUtils.isEmpty(currentItem.getChildPic())){
            Glide.with(mCtx).load(currentItem.getChildPic()).into(holder.childImageView);
        }else {

            Glide.with(mCtx).load(R.drawable.child1).into(holder.childImageView);

        }

        holder.txtSchool.setText(currentItem.getSchool());
        holder.mTextViewChildName.setText(currentItem.getfName());
        holder.mTextViewChildClass.setText(currentItem.getChildClass());
        holder.txtViewChildStatus.setText(currentItem.getAdmNo());


//        switch (currentItem.getChildLocation()){
//
//            case "In Bus":
//
//                holder.txtViewChildStatus.setTextColor(ContextCompat.getColor(mCtx, R.color.primary));
//
//                break;
//
//            case "At Home":
//
//                holder.txtViewChildStatus.setTextColor(ContextCompat.getColor(mCtx, R.color.grey));
//
//                break;
//
//        }

        holder.childLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mCtx, ChildActivity.class);
                mCtx.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

}
