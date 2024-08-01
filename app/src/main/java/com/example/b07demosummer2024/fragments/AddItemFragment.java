package com.example.b07demosummer2024.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.b07demosummer2024.models.Item;
import com.example.b07demosummer2024.R;
import com.example.b07demosummer2024.utilities.Database;
import com.example.b07demosummer2024.models.Media;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;
import java.util.ArrayList;

public class AddItemFragment extends Fragment {
    private EditText editTextName, editTextLotId, editTextCategory, editTextTimePeriod, editTextDescription;
    private Spinner mediaSpinner;
    private Button buttonAdd, buttonUpload;
    private List<String> images, videos;
    private FirebaseDatabase db;
    private DatabaseReference itemsRef;
    public boolean uploadingImage = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_item, container, false);


        editTextName = view.findViewById(R.id.editTextName);
        editTextLotId = view.findViewById(R.id.editTextLotId);
        editTextCategory = view.findViewById(R.id.editTextCategory);
        editTextDescription = view.findViewById(R.id.editTextDescription);
        editTextTimePeriod = view.findViewById(R.id.editTextTimePeriod);
        mediaSpinner = view.findViewById(R.id.mediaSpinner);
        buttonUpload = view.findViewById(R.id.buttonUpload);


        ArrayAdapter<CharSequence> adapter;
        adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.mediaUploadOptions, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mediaSpinner.setAdapter(adapter);


        mediaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    uploadingImage = true;
                } else uploadingImage = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {};
        });
        images = new ArrayList<String>();
        videos = new ArrayList<String>();


        buttonAdd = view.findViewById(R.id.buttonAdd);

        db = Database.getInstance();
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });

        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }


    private void addItem() {
        String name = editTextName.getText().toString().trim();
        String lotID = editTextLotId.getText().toString().trim();
        String category = editTextCategory.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String timePeriod = editTextTimePeriod.getText().toString().trim();


        if (name.isEmpty() || lotID.isEmpty() || category.isEmpty() || description.isEmpty() || timePeriod.isEmpty()) {
            Toast.makeText(getContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        itemsRef = db.getReference("items");
        String id = itemsRef.push().getKey();

        if (images.isEmpty()) images.add("null");
        if (videos.isEmpty()) videos.add("null");

        Item item = new Item(lotID, name, timePeriod, category, description, new Media(images, videos));

        itemsRef.child(id).setValue(item).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getContext(), "Item added", Toast.LENGTH_SHORT).show();
                editTextName.getText().clear();
                editTextLotId.getText().clear();
                editTextDescription.getText().clear();
                editTextTimePeriod.getText().clear();
                editTextCategory.getText().clear();

            } else {
                Toast.makeText(getContext(), "Failed to add item", Toast.LENGTH_SHORT).show();
            }
        });
    }





}
