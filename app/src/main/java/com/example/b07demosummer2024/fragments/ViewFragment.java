package com.example.b07demosummer2024.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.b07demosummer2024.models.Item;
import com.example.b07demosummer2024.R;

import android.util.Log;

// Temp Layout, will make it look better later

public class ViewFragment extends Fragment {
    private TextView nameText;
    private TextView DateText;
    private TextView CategoryText;
    private TextView DescriptionText;
    private TextView IdText;
    private Button backButton;


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
        backButton = view.findViewById(R.id.btnBack);

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

        nameText.setText(item.getName());
        DateText.setText("Time Period: " + item.getTimePeriod());
        CategoryText.setText("Category: " + item.getCategory());
        IdText.setText("LOT ID: " + item.getId());
        DescriptionText.setText(item.getDescription());

        // TODO: Add Pictures

        return view;
    }



}