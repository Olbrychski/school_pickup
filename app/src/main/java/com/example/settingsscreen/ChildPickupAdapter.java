package com.example.settingsscreen;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Random;

public class ChildPickupAdapter extends RecyclerView.Adapter<ChildPickupAdapter.ChildPickupViewholder> {

    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<User> parentsList;

    Random random = new Random();

    private String childId, parentId, childStatus;

    //getting the context and product list with constructor
    public ChildPickupAdapter(Context mCtx, List<User> parentsList, String childId, String childStatus) {
        this.mCtx = mCtx;
        this.parentsList = parentsList;
        this.childId = childId;
        this.childStatus = childStatus;
    }

    @NonNull
    @Override
    public ChildPickupAdapter.ChildPickupViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_parent_item, null);
        return new ChildPickupAdapter.ChildPickupViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChildPickupAdapter.ChildPickupViewholder holder, int position) {

        //getting the product of the specified position
        User parents = parentsList.get(position);
        parentId = String.valueOf(parents.getId());

        //binding the data with the viewholder views
        holder.textViewName.setText(parents.getfName());
        holder.textViewRole.setText(parents.getRole());
        holder.textViewPhoneNumber.setText(parents.getPhoneNumber());

        if (!TextUtils.isEmpty(parents.getDriverPic())){

            Glide.with(mCtx).load(parents.getDriverPic()).into(holder.imageViewParent);

        }else {

            Glide.with(mCtx).load(R.drawable.ic_no_profile).into(holder.imageViewParent);

        }

        holder.textViewSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //generate otp
                String otp = String.format("%04d", random.nextInt(10000));

                //send otp
                SmsManager mySmsManager = SmsManager.getDefault();
                mySmsManager.sendTextMessage(parents.getPhoneNumber(),null, otp, null, null);

                Toast.makeText(mCtx, "SMS Sent", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mCtx, OtpActivity.class);
                intent.putExtra("otp", otp);
                intent.putExtra("number", parents.getPhoneNumber());
                intent.putExtra("child_id", childId);
                intent.putExtra("parent_id", parentId);
                intent.putExtra("child_status", childStatus);
                mCtx.startActivity(intent);



            }
        });

    }

    @Override
    public int getItemCount() {
        return parentsList.size();
    }


    class ChildPickupViewholder extends RecyclerView.ViewHolder {

        TextView textViewName, textViewRole, textViewPhoneNumber, textViewSelect;
        ImageView imageViewParent;

        public ChildPickupViewholder(View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.txt_name);
            textViewRole = itemView.findViewById(R.id.txt_role);
            textViewPhoneNumber = itemView.findViewById(R.id.txt_phone);
            textViewSelect = itemView.findViewById(R.id.txt_select);
            imageViewParent = itemView.findViewById(R.id.profile_image);


        }
    }

}
