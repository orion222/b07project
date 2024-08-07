package com.example.b07demosummer2024.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.b07demosummer2024.interfaces.LogoutMVP;
import com.example.b07demosummer2024.utilities.LogoutPresenter;
import com.example.b07demosummer2024.R;
import com.example.b07demosummer2024.utilities.Pagination;

import androidx.fragment.app.FragmentManager;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsPopup extends DialogFragment {
    private LogoutMVP.Presenter presenter;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_settings_popup, null);

        builder.setView(view);

        Button confirmYes = view.findViewById(R.id.buttonYes);
        EditText maxEntries = view.findViewById(R.id.editTextMaxItems);

        //event handlers
        confirmYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String maxEntry = maxEntries.getText().toString().trim();
                try {
                    int number = Integer.parseInt(maxEntry);
                    Pagination.setItemsPerPage(number);
                    Toast.makeText(requireContext(), "Updated Entry Limit To: " + number, Toast.LENGTH_SHORT).show();
                } catch (NumberFormatException e) {
                    Toast.makeText(requireContext(), "Invalid number", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return builder.create();
    }
}
