/*
 Created by Intellij IDEA
 Author Name: KULDEEP SINGH (kuldeep506)
 Date: 22-05-2022
*/

package com.example.straycareapp;

import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RequestListAdapter extends RecyclerView.Adapter<RequestListAdapter.ViewHolder> {
    private final ArrayList<DetailModel> list;

    public RequestListAdapter(ArrayList<DetailModel> list) {
        this.list = list;
    }

    @NonNull
    @NotNull
    @Override
    public RequestListAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.registration_list_data_container, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RequestListAdapter.ViewHolder holder, int position) {
        String gender =(list.get(position).getGender());
        holder.gender.setText(gender);
        holder.type.setText(list.get(position).getAnimalType());
        holder.condition.setText(list.get(position).getCondition());
        holder.city.setText(list.get(position).getCity());

        String imageUrl=list.get(position).getImageUri();
        Picasso.get().load(imageUrl).fit() // Picasso lib to download and add image to imageview
                .placeholder(R.drawable.ic_baseline_image_24)
                .into(holder.Image);


        /** listener to open the full details of student*/
        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), FullDetails.class);
            intent.putExtra("imageurl",imageUrl);
//            Log.d("imgi", "url: "+imageUrl);
            ContextCompat.startActivity(v.getContext(), intent, null);
        });

        // open large image by clicking on image
        holder.Image.setOnClickListener(v -> {
            final Dialog nagDialog = new Dialog(v.getContext(), android.R.style.Widget_DeviceDefault_ActionBar);
            nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            nagDialog.setCancelable(true);
            nagDialog.setContentView(R.layout.preview_image);
            ImageView ivPreview = nagDialog.findViewById(R.id.iv_preview_image);
            ivPreview.setBackgroundDrawable(holder.Image.getDrawable());
            nagDialog.show();
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView gender;
        public TextView type;
        public TextView condition;
        public TextView city;

        public de.hdodenhof.circleimageview.CircleImageView Image;
        public CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);

            this.gender = itemView.findViewById(R.id.contID);
            this.type = itemView.findViewById(R.id.contName);
            this.condition = itemView.findViewById(R.id.contStop);
            this.city = itemView.findViewById(R.id.contNumber);

            this.Image = itemView.findViewById(R.id.contImage);
            this.cardView = itemView.findViewById(R.id.cardview);
        }
    }
}
