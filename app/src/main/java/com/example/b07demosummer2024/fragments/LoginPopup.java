package com.example.b07demosummer2024.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.b07demosummer2024.interfaces.LoginMVP;
import com.example.b07demosummer2024.utilities.Preferences;
import com.example.b07demosummer2024.utilities.Presenter;
import com.example.b07demosummer2024.R;
import com.example.b07demosummer2024.models.Credentials;
import com.example.b07demosummer2024.models.UserModel;
import com.google.firebase.database.FirebaseDatabase;
import java.util.*;
import android.widget.Toast;

//this is the VIEW in MVP, it represents the UI (mostly)
public class LoginPopup extends DialogFragment implements LoginMVP.View {
    private LoginMVP.Presenter presenter;
    private FirebaseDatabase db;
    private List<Credentials> credentialsList;
    public static boolean admin;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_login_popup, null);

        builder.setView(view);

        Button buttonLogin = view.findViewById(R.id.buttonLogin);
        EditText user = (EditText) view.findViewById(R.id.editTextUsername);
        EditText pass = (EditText) view.findViewById(R.id.editTextPassword);

        // initialize the presenter, give a concrete model as its argument
        presenter = new Presenter(new UserModel(), requireContext());
        presenter.attachView(this);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //handle the login
                presenter.handleLogin(getInputFields(user), getInputFields(pass));
            }
        });
        return builder.create();
    }

    private String getInputFields(EditText a) {
        return a.getText().toString();
    }

    //login success/error should focus on only the UI aspect, other logic is in Presenter
    @Override
    public void onLoginError(String message) {
        Toast.makeText(requireContext(), "Invalid Username/Password", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoginSuccess(String message) {
        Toast.makeText(requireContext(), "Successfully Logged In", Toast.LENGTH_SHORT).show();
        dismiss();
        loadFragment(new RecyclerViewFragment());
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
