package com.example.b07demosummer2024.utilities;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.graphics.Color;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

//glide imports
import com.bumptech.glide.Glide;
import com.example.b07demosummer2024.R;
import com.example.b07demosummer2024.activities.HomeActivity;
import com.example.b07demosummer2024.activities.MainActivity;
import com.example.b07demosummer2024.fragments.RecyclerViewFragment;
import com.example.b07demosummer2024.interfaces.RecyclerViewInterface;
import com.example.b07demosummer2024.models.Item;


public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private List<Item> itemList;
    private RecyclerViewInterface recyclerViewInterface;
//    private List<Integer> clickedList;
    private Set<Integer> clickedList;

    public ItemAdapter(List<Item> itemList, RecyclerViewInterface recyclerViewInterface) {
        this.itemList = itemList;
        this.recyclerViewInterface = recyclerViewInterface;
//        clickedList = new ArrayList<Integer>();
        clickedList = new HashSet<Integer>();
    }

    // temp
    // could just be one constructor
    public ItemAdapter(List<Item> itemList, RecyclerViewInterface recyclerViewInterface, Set<Integer> set) {
        this.itemList = itemList;
        this.recyclerViewInterface = recyclerViewInterface;
//        clickedList = new ArrayList<Integer>();
        clickedList = set;
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
        String lot = "Lot ID: " + item.getId();
        holder.textLotID.setText(lot);
        String category = "Category: " + item.getCategory();
        holder.textViewGenre.setText(category);
        String time = "Time Period: " + item.getTimePeriod();
        holder.textViewAuthor.setText(time);
        holder.textViewDescription.setText(item.getDescription());
        holder.textViewDescription.setMaxLines(3);
        holder.textViewDescription.setEllipsize(TextUtils.TruncateAt.END);
        holder.textViewDescription.setText(item.getDescription());


        //try to showcase thumbnail
        List<String> imagePaths = item.getMedia().getImagePaths();
        if (imagePaths != null && !imagePaths.isEmpty()) {
            String thumbnail = imagePaths.get(0);

            // use Glide to replace the content of itemView
            Glide.with(holder.itemView.getContext())
                    .load(thumbnail)
                    .placeholder(R.drawable.notloading) // placeholder while loading
                    .error(R.drawable.pic_not_available) // error image
                    .into(holder.imageView);
        }
        else {
            //if no thumbnail in db
            holder.imageView.setImageResource(R.drawable.notavailable);
        }

        if (clickedList.contains(Integer.parseInt(item.getId()))) {
            holder.itemView.setBackgroundColor(Color.parseColor("#f5ebe0"));
        } else {
//            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }

        // used to listen for long presses; used for deletion
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(!RecyclerViewFragment.getDeleteMode()) {
                    RecyclerViewFragment.setDeleteMode(true, view);
                    checkItemToRemove(holder, item);
                }
                return true;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();

                if(RecyclerViewFragment.getDeleteMode()) {
                    checkItemToRemove(holder, item);

                    if(clickedList.isEmpty()) {
                        RecyclerViewFragment.setDeleteMode(false, view);
                    }
                }
                else if (recyclerViewInterface != null) {
                    if (pos != RecyclerView.NO_POSITION) {
                        recyclerViewInterface.itemClicked(pos);
                    }
                }
            }
        });

    }

    public void checkItemToRemove(ItemViewHolder holder, Item item) {
        int pos = holder.getAdapterPosition();
        int posLotNum = Integer.parseInt(item.getId());

        if (clickedList.contains(posLotNum)) {
            clickedList.remove(posLotNum);
        } else {
            clickedList.add(posLotNum);
        }

        notifyItemChanged(pos);
    }

    //accessory function
    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public Set<Integer> getSet() {
        return clickedList;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewAuthor, textViewGenre, textViewDescription, textLotID;
        ImageView imageView; // Add this line


        public ItemViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewAuthor = itemView.findViewById(R.id.textViewAuthor);
            textViewGenre = itemView.findViewById(R.id.textViewGenre);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textLotID = itemView.findViewById(R.id.textViewLotId);

            imageView = itemView.findViewById(R.id.imageView); // Initialize the ImageView

        }
    }
}
