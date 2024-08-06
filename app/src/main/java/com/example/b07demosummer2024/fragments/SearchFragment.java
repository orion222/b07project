package com.example.b07demosummer2024.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.b07demosummer2024.R;
import com.example.b07demosummer2024.models.ItemViewModel;

public class SearchFragment extends Fragment {
    private EditText edtTxtLotNumber;
    private EditText edtTxtName;
    private Spinner spnCategory;
    private Spinner spnPeriod;
    private Button btnResult;
    private ItemViewModel itemViewModel;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        edtTxtLotNumber = view.findViewById(R.id.edtTxtLotNumber);
        edtTxtName = view.findViewById(R.id.edtTxtName);
        spnCategory = view.findViewById(R.id.spnCategory);
        spnPeriod = view.findViewById(R.id.spnPeriod);
        btnResult = view.findViewById(R.id.btnResult);

        itemViewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);

        btnResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lotNumber = edtTxtLotNumber.getText().toString().toLowerCase().trim();
                String itemName = edtTxtName.getText().toString().toLowerCase().trim();
                String category = spnCategory.getSelectedItem().toString().toLowerCase().trim();
                String period = spnPeriod.getSelectedItem().toString().toLowerCase().trim();
                itemViewModel.search(lotNumber, itemName, category, period);

                getParentFragmentManager().beginTransaction()
                        .replace(R.id.home_fragment_container, new RecyclerViewFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }
}