package com.example.b07demosummer2024.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.b07demosummer2024.models.Item;
import com.example.b07demosummer2024.R;
import android.util.Log;
import java.util.List;

public class ViewFragment extends Fragment {
    private TextView nameText;
    private TextView DateText;
    private TextView CategoryText;
    private TextView DescriptionText;
    private TextView IdText;
    private Button backButton;
    private ImageView imageView;

    private TextView linksTextView;
    private TextView linksHeader;

    private Item item;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("CYCLE", "ViewFragment - onCreateView");
        View view = inflater.inflate(R.layout.fragment_view, container, false);

        nameText = view.findViewById(R.id.textName);
        DateText = view.findViewById(R.id.textDate);
        CategoryText = view.findViewById(R.id.textCategory);
        IdText = view.findViewById(R.id.textId);
        DescriptionText = view.findViewById(R.id.textDescription);
        imageView = view.findViewById(R.id.imageView);
        backButton = view.findViewById(R.id.btnBack);
        linksTextView = view.findViewById(R.id.textLinks);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getParentFragmentManager().getBackStackEntryCount() > 0) {
                    getParentFragmentManager().popBackStack();
                }
            }
        });

        if (getArguments() != null) {
            item = (Item) getArguments().getSerializable("key");
            assert item != null;
        }

        // update TextViews with item details
        nameText.setText(item.getName());
        DateText.setText("Time Period: " + item.getTimePeriod());
        CategoryText.setText("Category: " + item.getCategory());
        IdText.setText("Lot ID: " + item.getId());
        DescriptionText.setText(item.getDescription());

        List<String> imagePaths = item.getMedia().getImagePaths();
        if (imagePaths != null && !imagePaths.isEmpty()) {
            String thumbnail = imagePaths.get(0);

            Glide.with(imageView.getContext())
                    .load(thumbnail)
                    .placeholder(R.drawable.notloading) // placeholder while loading
                    .error(R.drawable.pic_not_available) // error image
                    .into(imageView);
        } else {
            // set default image if no image available
            imageView.setImageResource(R.drawable.notloading);
        }

        List<String> videoLinks = item.getMedia().getVideoPaths();
        if (videoLinks != null && !videoLinks.isEmpty()) {
            if ("null".equals(videoLinks.get(0))) {
                linksTextView.setVisibility(View.INVISIBLE);
            } else {
                StringBuilder linksBuilder = new StringBuilder();
                for (String link : videoLinks) {
                    linksBuilder.append("Video: ").append(link).append("\n\n");
                }
                linksTextView.setText(linksBuilder.toString());
                linksTextView.setMovementMethod(LinkMovementMethod.getInstance()); // make links clickable
            }
        }


        return view;
    }
}
