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
import androidx.fragment.app.FragmentTransaction;

import com.example.b07demosummer2024.interfaces.LogoutMVP;
import com.example.b07demosummer2024.utilities.LogoutPresenter;
import com.example.b07demosummer2024.R;

import android.widget.Toast;

public class LogoutPopup extends DialogFragment implements LogoutMVP.View {
    private LogoutMVP.Presenter presenter;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_logout_popup, null);

        builder.setView(view);
        presenter = new LogoutPresenter(requireContext());
        presenter.attachView(this);


        Button confirmYes = view.findViewById(R.id.buttonYes);
        Button confirmNo = view.findViewById(R.id.buttonNo);

        //event handlers
        confirmYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //handle the login
                presenter.logout();
            }
        });

        confirmNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return builder.create();
    }

    public void onLogoutSuccess() {
        Toast.makeText(requireContext(), "Logged Out", Toast.LENGTH_SHORT).show();
        dismiss(); //closes the dialog
        if (getActivity() != null) {
            getActivity().recreate(); // calls onCreate() for main
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
