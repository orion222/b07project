package com.example.b07demosummer2024.utilities;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.b07demosummer2024.R;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private final List<String> imagePaths;

    public ImageAdapter(List<String> imagePaths) {
        this.imagePaths = imagePaths != null ? imagePaths : new ArrayList<>();
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        if (position >= imagePaths.size()) {
            Log.w("ImageAdapter", "Invalid position: " + position);
            return;
        }

        String imagePath = imagePaths.get(position);
        Log.d("ImageAdapter", "Binding image at position " + position + ": " + imagePath);

        Glide.with(holder.imageView.getContext())
                .load(imagePath)
                .placeholder(R.drawable.notloading)
                .error(R.drawable.pic_not_available)
                .into(holder.imageView)
                .clearOnDetach(); // Ensures cleanup if view is detached
        Log.d("ImageAdapter", "Glide call completed");
    }

    @Override
    public int getItemCount() {
        return imagePaths.size();
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
