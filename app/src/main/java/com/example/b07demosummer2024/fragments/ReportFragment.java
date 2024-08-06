package com.example.b07demosummer2024.fragments;

import android.os.Bundle;
import android.os.Build;
import android.content.pm.PackageManager;
import android.Manifest;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.b07demosummer2024.models.Item;
import com.example.b07demosummer2024.R;
import com.example.b07demosummer2024.utilities.Database;
import com.example.b07demosummer2024.utilities.PDFCreator;
import com.google.firebase.database.DatabaseError;
import java.util.List;

public class ReportFragment extends Fragment {
    private EditText editTextConstraint;
    private Button buttonGenerate;
    private Spinner spinnerFilterOptions;
    private Spinner spinnerCategory;
    private Spinner spinnerTimePeriod;
    private CheckBox contentCheckBox;

    private static final int REQUEST_CODE_PERMISSIONS = 1;
    private final static String[] filterKeyWords = {"", "id", "name", "category", "timePeriod"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);

        editTextConstraint = view.findViewById(R.id.editTextConstraint);
        spinnerCategory = view.findViewById(R.id.spinnerCategory);
        spinnerTimePeriod = view.findViewById(R.id.spinnerTimePeriod);
        buttonGenerate = view.findViewById(R.id.buttonGenerate);
        spinnerFilterOptions = view.findViewById(R.id.spinnerFilterOptions);
        contentCheckBox = view.findViewById(R.id.contentCheckBox);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.reportFilterOptions, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilterOptions.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapterCategory = ArrayAdapter.createFromResource(requireContext(),
                R.array.category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapterCategory);

        ArrayAdapter<CharSequence> adapterTime = ArrayAdapter.createFromResource(requireContext(),
                R.array.period, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTimePeriod.setAdapter(adapterTime);

        spinnerFilterOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0){
                    editTextConstraint.setVisibility(view.GONE);
                    spinnerCategory.setVisibility(view.GONE);
                    spinnerTimePeriod.setVisibility(view.GONE);
                }else if(i <= 2){
                    editTextConstraint.setVisibility(view.VISIBLE);
                    spinnerCategory.setVisibility(view.GONE);
                    spinnerTimePeriod.setVisibility(view.GONE);
                }else if( i == 3){
                    editTextConstraint.setVisibility(view.GONE);
                    spinnerCategory.setVisibility(view.VISIBLE);
                    spinnerTimePeriod.setVisibility(view.GONE);
                }else{
                    editTextConstraint.setVisibility(view.GONE);
                    spinnerCategory.setVisibility(view.GONE);
                    spinnerTimePeriod.setVisibility(view.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                editTextConstraint.setVisibility(view.GONE);
                spinnerCategory.setVisibility(view.GONE);
                spinnerTimePeriod.setVisibility(view.GONE);
            }
        });

        buttonGenerate.setOnClickListener(v -> {
//            Log.d("REPORT", "Button clicked");
            checkMediaPermissions();
        });

        return view;
    }

    // asks and enables permissions
    private void checkMediaPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            boolean readMediaImages = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED;
            boolean readMediaVideo = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED;
            boolean readMediaAudio = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED;

            if (!readMediaImages || !readMediaVideo || !readMediaAudio) {
                Log.d("REPORT", "Requesting media permissions");
                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO, Manifest.permission.READ_MEDIA_AUDIO},
                        REQUEST_CODE_PERMISSIONS);

            } else {
                generateReport();
            }
        } else {
            // for devices < Android 13, use old permissions
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                Log.d("REPORT", "Requesting WRITE_EXTERNAL_STORAGE permission");
                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_PERMISSIONS);
            } else {
                generateReport();
            }
        }
    }

    private void generateReport() {
        Toast.makeText(requireContext(), "Generating report ... Please allow up to 5 seconds", Toast.LENGTH_SHORT).show();
        Log.d("REPORT", "Generating report");
        PDFCreator pdfCreator = new PDFCreator();

        String textConstraint = editTextConstraint.getText().toString().trim();
        boolean contentType = contentCheckBox.isChecked();
        int position = spinnerFilterOptions.getSelectedItemPosition();
        int categoryPosition = spinnerCategory.getSelectedItemPosition();
        int timePeriodPosition = spinnerTimePeriod.getSelectedItemPosition();
        String category =(String) spinnerCategory.getItemAtPosition(categoryPosition);
        String timePeriod = (String) spinnerTimePeriod.getItemAtPosition(timePeriodPosition);
        String filterType = filterKeyWords[position];
        String filterConstraint = "";

        if (position == 0) {
            Database.fetchItems(new Database.OnDataFetchedListener<Item>() {
                @Override
                public void onDataFetched(List<Item> ret) {
                    pdfCreator.createPdf(getContext(), ret, "All Items", contentType);
                }

                @Override
                public void onError(DatabaseError error) {
                    Log.e("db err", "Failed to fetch items");
                }
            });
        } else {
            filterConstraint = "";
            if(position <= 2){
                filterConstraint = textConstraint;
            }else if(position == 3){
                filterConstraint = category;
            }else{
                filterConstraint = timePeriod;
            }
            String finalFilterConstraint = filterConstraint;
            Database.fetchItemsFiltered(new Database.OnDataFetchedListener<Item>() {
                @Override
                public void onDataFetched(List<Item> ret) {
                    pdfCreator.createPdf(getContext(), ret, finalFilterConstraint, contentType);
                }

                @Override
                public void onError(DatabaseError error) {
                    Log.e("db err", "Failed to fetch items");
                }
            }, filterType, filterConstraint);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            boolean allPermissionsGranted = true;
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }
            if (allPermissionsGranted) {
                Log.d("REPORT", "Permissions granted");
                generateReport();
            } else {
                Log.d("REPORT", "Permissions denied");
                Toast.makeText(getContext(), "Permissions denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
