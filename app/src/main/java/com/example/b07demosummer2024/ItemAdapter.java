package com.example.b07demosummer2024;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import android.graphics.Color;

//glide imports
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;


public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private List<Item> itemList;
    private RecyclerViewInterface recyclerViewInterface;

    public ItemAdapter(List<Item> itemList, RecyclerViewInterface recyclerViewInterface) {
        this.itemList = itemList;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_adapater, parent, false);
        return new ItemViewHolder(view, recyclerViewInterface);
    }

    //binds one instance of class Item to item adapter fragment
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.textViewTitle.setText(item.getName());
        holder.textViewAuthor.setText(item.getTimePeriod());
        holder.textViewGenre.setText(item.getCategory());
        holder.textViewDescription.setText(item.getDescription());

        //try to showcase thumbnail
        List<String> imagePaths = item.getMedia().getImagePaths();
        if (imagePaths != null && !imagePaths.isEmpty()) {
            String thumbnail = imagePaths.get(0);

            // use Glide to replace the content of itemView
            Glide.with(holder.itemView.getContext())
                    .load(thumbnail)
                    .placeholder(R.drawable.notloading) // placeholder while loading
                    .error(R.drawable.notavailable) // error image
                    .into(holder.imageView);
        }
        else {
            //if no thumbnail in db
            holder.imageView.setImageResource(R.drawable.notavailable);
        }
    }

    //accessory function
    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewAuthor, textViewGenre, textViewDescription;
        ImageView imageView; // Add this line


        public ItemViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewAuthor = itemView.findViewById(R.id.textViewAuthor);
            textViewGenre = itemView.findViewById(R.id.textViewGenre);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);

            imageView = itemView.findViewById(R.id.imageView); // Initialize the ImageView

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewInterface != null) {
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.itemClicked(pos);
                        }

                    }
//                    itemView.setBackgroundColor(Color.LTGRAY);
                }
            });

        }
    }
}
