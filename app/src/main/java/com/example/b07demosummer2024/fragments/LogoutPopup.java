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
import androidx.fragment.app.FragmentManager;

import android.widget.TextView;
import android.widget.Toast;

public class LogoutPopup extends DialogFragment implements LogoutMVP.View {
    private LogoutMVP.Presenter presenter;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_confirm_popup, null);


        builder.setView(view);
        presenter = new LogoutPresenter(requireContext());
        presenter.attachView(this);

        TextView message = view.findViewById(R.id.textViewMessage);
        String confirmMsg = "Are you sure you want to logout?";
        message.setText(confirmMsg);

        Button confirmYes = view.findViewById(R.id.buttonYes);
        Button confirmNo = view.findViewById(R.id.buttonNo);

        // event handlers
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
        // Clear the back stack and replace the current fragment with LoginFragment
        if (getActivity() != null) {
            // Clear the back stack
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

            // Navigate to the login fragment
            Fragment loginFragment = new LoginFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, loginFragment)
                    .commit();
        }
    }
}
