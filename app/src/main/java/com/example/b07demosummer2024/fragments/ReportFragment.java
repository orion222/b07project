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
    private EditText editReportConstraint;
    private Button buttonGenerate;
    private Spinner spinnerFilterOptions;
    private Spinner selectionSpinner;
    private CheckBox contentCheckBox;

    private static final int REQUEST_CODE_PERMISSIONS = 1;
    private final static String[] filterKeyWords = {"", "id", "name", "category", "timePeriod"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);

        editReportConstraint = view.findViewById(R.id.editReportConstraint);
        buttonGenerate = view.findViewById(R.id.buttonGenerate);
        spinnerFilterOptions = view.findViewById(R.id.spinnerFilterOptions);
        selectionSpinner = view.findViewById(R.id.selectionSpinner);
        contentCheckBox = view.findViewById(R.id.contentCheckBox);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.reportFilterOptions, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilterOptions.setAdapter(adapter);

        spinnerFilterOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                handleFilterOptionSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectionSpinner.setVisibility(View.GONE);
                editReportConstraint.setVisibility(View.GONE);
            }
        });

        buttonGenerate.setOnClickListener(v -> checkMediaPermissions());

        return view;
    }

    private void handleFilterOptionSelection(int position) {
        String selectedFilter = filterKeyWords[position];

        if (selectedFilter.equalsIgnoreCase("category")) {
            selectionSpinner.setVisibility(View.VISIBLE);
            ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(requireContext(),
                    R.array.category, android.R.layout.simple_spinner_item);
            categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            selectionSpinner.setAdapter(categoryAdapter);

            editReportConstraint.setVisibility(View.GONE); // Hide EditText for category

        } else if (selectedFilter.equalsIgnoreCase("timePeriod")) {
            selectionSpinner.setVisibility(View.VISIBLE);
            ArrayAdapter<CharSequence> periodAdapter = ArrayAdapter.createFromResource(requireContext(),
                    R.array.period, android.R.layout.simple_spinner_item);
            periodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            selectionSpinner.setAdapter(periodAdapter);

            editReportConstraint.setVisibility(View.GONE);

        } else if (selectedFilter.equalsIgnoreCase("")) {
            selectionSpinner.setVisibility(View.GONE);
            editReportConstraint.setVisibility(View.GONE);

        } else {
            selectionSpinner.setVisibility(View.GONE);
            editReportConstraint.setVisibility(View.VISIBLE);
        }
    }

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

        String filterConstraint;
        boolean contentType = contentCheckBox.isChecked();
        int position = spinnerFilterOptions.getSelectedItemPosition();
        String filterType = filterKeyWords[position];

        if (filterType.equals("category") || filterType.equals("timePeriod")) {
            filterConstraint = selectionSpinner.getSelectedItem().toString();
        } else {
            filterConstraint = editReportConstraint.getText().toString().trim();
        }

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
            Database.fetchItemsFiltered(new Database.OnDataFetchedListener<Item>() {
                @Override
                public void onDataFetched(List<Item> ret) {
                    pdfCreator.createPdf(getContext(), ret, filterType, contentType);
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