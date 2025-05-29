package com.example.finalprojesicamerax3;

import static android.app.ProgressDialog.show;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private final Context context;
    private final ArrayList<ImageItem> imageList;

    public ImageAdapter(Context context, ArrayList<ImageItem> imageList) {
        this.context = context;
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.single_image_recycler_adapter, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        ImageItem currentItem = imageList.get(position);

//        Picasso ESKI
//        Picasso.get().load(currentItem.getImageUrl()).into(holder.imageView);

//        Glide YENI
        // Load image using GLide or any library
        Glide.with(context).load(currentItem.getImageUrl()).into(holder.imageView);
        holder.textViewName.setText(currentItem.getImageName());

        holder.itemView.setOnLongClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition == RecyclerView.NO_POSITION) {
                return false; // If position is invalid, do nothing
            }
            ImageItem itemToDelete = imageList.get(currentPosition);

            // Ask for deletion
            new AlertDialog.Builder(context)
                .setTitle("Fotoğraf Silmek")
                .setMessage("Bu fotoğrafı silmek ister misin?")
                .setPositiveButton("Evet", (dialog, which) -> {
                    // Cal delete method in the Activity
                    if (context instanceof Gallery2) {
                        ((Gallery2) context).deleteImage(currentItem, position);
                    }
                })
               .setNegativeButton("Hayır", null)
               .show();

            return true; // Important: Return true to consume the long-click event
        });
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textViewName;
        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textViewName = itemView.findViewById(R.id.textViewName);
        }
    }
}
