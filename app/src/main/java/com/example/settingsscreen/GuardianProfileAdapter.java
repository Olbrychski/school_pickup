package com.example.settingsscreen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GuardianProfileAdapter extends RecyclerView.Adapter<GuardianProfileAdapter.GuardianProfileViewHolder>  {

    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<GuardianProfile> guardianProfileList;

    //getting the context and product list with constructor
    public GuardianProfileAdapter(Context mCtx, List<GuardianProfile> guardianProfileList) {
        this.mCtx = mCtx;
        this.guardianProfileList = guardianProfileList;
    }

    @NonNull
    @Override
    public GuardianProfileAdapter.GuardianProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_guardian_profile, null);
        return new GuardianProfileViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull GuardianProfileViewHolder holder, int position) {

        //getting the product of the specified position
        GuardianProfile product = guardianProfileList.get(position);

        //binding the data with the viewholder views
        holder.textViewGuardianName.setText(product.getGuardianName());
        holder.textViewGuardianTitle.setText(product.getGuardianTitle());
        holder.textViewGuardianPhoneNumber.setText(String.valueOf(product.getGuardianPhoneNumber()));
        holder.textViewGuardianEmail.setText(String.valueOf(product.getGuardianEmail()));

        holder.imgViewProfileImage.setImageDrawable(mCtx.getResources().getDrawable(product.getGuardianProfileImage()));


    }

    @Override
    public int getItemCount() {
        return guardianProfileList.size();
    }


    class GuardianProfileViewHolder extends RecyclerView.ViewHolder {

        TextView textViewGuardianName, textViewGuardianTitle, textViewGuardianPhoneNumber, textViewGuardianEmail;
        ImageView imgViewProfileImage;

        public GuardianProfileViewHolder(View itemView) {
            super(itemView);

            textViewGuardianName = itemView.findViewById(R.id.txt_guardian_name);
            textViewGuardianTitle = itemView.findViewById(R.id.txtGuardianTitle);
            textViewGuardianPhoneNumber = itemView.findViewById(R.id.phoneNumber);
            textViewGuardianEmail = itemView.findViewById(R.id.emailAddress);
            imgViewProfileImage = itemView.findViewById(R.id.profile_image);
        }
    }

}
