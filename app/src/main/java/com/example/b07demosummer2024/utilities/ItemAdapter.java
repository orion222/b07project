package com.example.b07demosummer2024.utilities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import com.bumptech.glide.Glide;
import com.example.b07demosummer2024.R;
import com.example.b07demosummer2024.interfaces.RecyclerViewInterface;
import com.example.b07demosummer2024.models.Item;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private List<Item> itemList;
    private RecyclerViewInterface recyclerViewInterface;

    public ItemAdapter(List<Item> itemList, RecyclerViewInterface recyclerViewInterface) {
        this.itemList = itemList;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    public void setItems(List<Item> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    public List<Item> getItems() {
        return itemList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_adapater, parent, false);
        return new ItemViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.textViewTitle.setText(item.getName());
        holder.textViewAuthor.setText(item.getTimePeriod());
        holder.textViewGenre.setText(item.getCategory());
        holder.textViewDescription.setText(item.getDescription());

        List<String> imagePaths = item.getMedia().getImagePaths();
        if (imagePaths != null && !imagePaths.isEmpty()) {
            String thumbnail = imagePaths.get(0);
            Glide.with(holder.itemView.getContext())
                    .load(thumbnail)
                    .placeholder(R.drawable.notloading)
                    .error(R.drawable.notavailable)
                    .into(holder.imageView);
        } else {
            holder.imageView.setImageResource(R.drawable.notavailable);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewAuthor, textViewGenre, textViewDescription;
        ImageView imageView;

        public ItemViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewAuthor = itemView.findViewById(R.id.textViewAuthor);
            textViewGenre = itemView.findViewById(R.id.textViewGenre);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            imageView = itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewInterface != null) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.itemClicked(pos);
                        }
                    }
                }
            });
        }
    }
}
