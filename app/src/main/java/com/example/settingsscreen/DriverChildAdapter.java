package com.example.settingsscreen;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class DriverChildAdapter extends RecyclerView.Adapter<DriverChildAdapter.ChildrenViewHolder>{

    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<DriverChild> childrenList;

    //getting the context and product list with constructor
    public DriverChildAdapter(Context mCtx, List<DriverChild> childrenList) {
        this.mCtx = mCtx;
        this.childrenList = childrenList;
    }

    @NonNull
    @Override
    public DriverChildAdapter.ChildrenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_driver_children, null);
        return new ChildrenViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ChildrenViewHolder holder, int position) {

        //getting the product of the specified position
        DriverChild children = childrenList.get(position);

        //binding the data with the viewholder views
        holder.textViewName.setText(children.getfName());
        holder.textViewClass.setText(children.getChildClass());
        holder.textViewPickLocation.setText(children.getAdmNo());

        if (!TextUtils.isEmpty(children.getChildPic())){

            Glide.with(mCtx).load(children.getChildPic()).into(holder.imageViewChild);

        }else {

            Glide.with(mCtx).load(R.drawable.ic_no_profile).into(holder.imageViewChild);

        }

        holder.txtPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mCtx, ChildPickupActivity.class);
                intent.putExtra("child_id", String.valueOf(children.getId()));
                intent.putExtra("status", "picked");
                mCtx.startActivity(intent);

            }
        });

        holder.txtDrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mCtx, ChildPickupActivity.class);
                intent.putExtra("child_id", String.valueOf(children.getId()));
                intent.putExtra("status", "dropped");
                mCtx.startActivity(intent);

            }
        });

        holder.txtMissedBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(mCtx, "Done, child has missed the bus you may proceed with the journey", Toast.LENGTH_LONG).show();

            }
        });


    }

    @Override
    public int getItemCount() {
        return childrenList.size();
    }

    class ChildrenViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName, textViewSchool, textViewClass, textViewPickLocation, txtPick, txtDrop, txtMissedBus;
        ImageView imageViewChild;

        public ChildrenViewHolder(View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.txt_name);
            textViewSchool = itemView.findViewById(R.id.txt_school);
            textViewClass = itemView.findViewById(R.id.txt_class);
            textViewPickLocation = itemView.findViewById(R.id.txt_location);
            imageViewChild = itemView.findViewById(R.id.profile_image);
            txtPick = itemView.findViewById(R.id.txt_pick);
            txtDrop = itemView.findViewById(R.id.txt_drop);
            txtMissedBus = itemView.findViewById(R.id.txt_missed_bus);

        }
    }

}
