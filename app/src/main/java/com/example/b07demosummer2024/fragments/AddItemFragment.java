package com.example.b07demosummer2024.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddItemFragment extends Fragment {
    private EditText editTextName, editTextLotId, editTextCategory, editTextTimePeriod, editTextDescription;
    private TextView mediaCount;
    private Spinner mediaSpinner;
    private Button buttonAdd, buttonUpload;
    private List<String> images, videos;
    private FirebaseDatabase db;
    private DatabaseReference itemsRef;
    private FirebaseStorage storage;
    private int count;

    private Uri media;
    private boolean uploadingImage = true;
    private boolean isUploading = false;

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
        buttonAdd = view.findViewById(R.id.buttonAdd);
        mediaCount = view.findViewById(R.id.mediaCount);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.mediaUploadOptions, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mediaSpinner.setAdapter(adapter);

        mediaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                uploadingImage = (position == 0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        images = new ArrayList<>();
        videos = new ArrayList<>();
        db = Database.getInstance();
        storage = Database.getStorageInstance();

        buttonAdd.setOnClickListener(v -> {
            if (!isUploading) {
                addItem();
            } else {
                Toast.makeText(requireContext(), "Please wait for the upload to complete", Toast.LENGTH_SHORT).show();
            }
        });

        buttonUpload.setOnClickListener(v -> selectFile());

        return view;
    }

    private void selectFile() {
        Intent intent = new Intent();
        intent.setType(uploadingImage ? "image/*" : "video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);
    }

    private void uploadFile() {
        isUploading = true;
        Toast.makeText(requireContext(), "Uploading...", Toast.LENGTH_SHORT).show();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA);
        String fileName = formatter.format(new Date());
        String path = (uploadingImage ? "media/images/" : "media/videos/") + fileName;
        StorageReference ref = storage.getReference(path);

        ref.putFile(media)
                .addOnSuccessListener(taskSnapshot -> {
                    if (uploadingImage) {
                        // For images, directly use the URI
                        images.add(media.toString());  // Directly add the URI
                        Toast.makeText(requireContext(), "Image Successfully Uploaded", Toast.LENGTH_SHORT).show();
                    } else {
                        // For videos, get the download URL
                        ref.getDownloadUrl().addOnSuccessListener(uri -> {
                                    String downloadUrl = uri.toString();
                                    videos.add(downloadUrl);  // Add the download URL
                                    Toast.makeText(requireContext(), "Video Successfully Uploaded", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(requireContext(), "Failed to Retrieve URL", Toast.LENGTH_SHORT).show();
                                });
                    }
                    isUploading = false;
                    count++;
                    mediaCount.setText("Uploaded files: " + count);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireContext(), "Failed to Upload", Toast.LENGTH_SHORT).show();
                    isUploading = false;
                });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == -1 && data != null && data.getData() != null) {
            media = data.getData();
            uploadFile();
        }
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
                clearFields();
            } else {
                Toast.makeText(getContext(), "Failed to add item", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearFields() {
        editTextName.getText().clear();
        editTextLotId.getText().clear();
        editTextDescription.getText().clear();
        editTextTimePeriod.getText().clear();
        editTextCategory.getText().clear();
    }
}