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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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

import com.example.b07demosummer2024.utilities.ItemAdapter;
import com.example.b07demosummer2024.utilities.Pagination;
import com.example.b07demosummer2024.utilities.PdfCreator;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;
import java.util.ArrayList;

public class ReportFragment extends Fragment {
    private EditText editReportConstraint;
    private Button buttonGenerate;
    private FirebaseDatabase db;
    private DatabaseReference itemsRef;
    private Spinner spinnerFilterOptions;
    private CheckBox contentCheckBox;
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 1;
    private static PdfCreator pdfCreator = new PdfCreator();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);

        editReportConstraint = view.findViewById(R.id.editReportConstraint);

        buttonGenerate = view.findViewById(R.id.buttonGenerate);

        spinnerFilterOptions = view.findViewById(R.id.spinnerFilterOptions);

        contentCheckBox = view.findViewById(R.id.contentCheckBox);

        ArrayAdapter<CharSequence> adapter;
        adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.reportFilterOptions, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilterOptions.setAdapter(adapter);


        buttonGenerate.setOnClickListener(v -> {
            Log.d("test", "Generate Button");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
                } else {
                    generateReport();
                }
            } else {
                generateReport();
            }
        });

        return view;
    }

    private void generateReport() {
        String filterConstraint = editReportConstraint.getText().toString().trim();
        boolean contentType = contentCheckBox.isChecked();
        int position = spinnerFilterOptions.getSelectedItemPosition();
        String filterType = spinnerFilterOptions.getSelectedItem().toString().replace("By ","");

        Database.fetchItems(new Database.OnDataFetchedListener(){
            @Override
            public void onDataFetched(List ret) {
                pdfCreator.createPdf(getContext(),ret.subList(0,5), contentType);
            }

            @Override
            public void onError(DatabaseError error) {
                Log.e("db err", "Failed to fetch items");
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                generateReport();
            } else {
                Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
