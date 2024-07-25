package com.example.b07demosummer2024;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.*;
import android.widget.Toast;

//this is the VIEW in MVP, it represents the UI (mostly)
public class LoginPopup extends DialogFragment implements LoginMVP.View{

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

        // initialize the presenter
        presenter = new Presenter(new FirebaseModel());
        presenter.attachView(this);
//        credentialsList = new ArrayList<Credentials>();
//        Database.fetchCredentials(new Database.OnDataFetchedListener() {
//            @Override
//            public void onDataFetched(List ret) {
//                credentialsList.clear();
//                credentialsList.addAll(ret);
//                Log.d("cred", "List size is " + ret.size());
//            }
//
//            @Override
//            public void onError(DatabaseError error) {
//                Log.e("db err", "Failed to fetch credentials");
//            }
//        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //handle the login
                presenter.handleLogin(getInputFields(user), getInputFields(pass));
//                if (checkLogin(user, pass)) {
//                    Toast.makeText(getContext(), "Successfully Logged In", Toast.LENGTH_SHORT).show();
//                    setAdmin(true);
//                    dismiss();
//                    loadFragment(new RecyclerViewFragment());
//                } else {
//                    Toast.makeText(getContext(), "Invalid Username/Password", Toast.LENGTH_SHORT).show();
//                }
            }
        });

        return builder.create();
    }

    private String getInputFields(EditText a) {
        return a.getText().toString();
    }

    @Override
    public void onLoginError(String message) {
        Toast.makeText(getContext(), "Invalid Username/Password", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoginSuccess(String message) {
        Toast.makeText(getContext(), "Successfully Logged In", Toast.LENGTH_SHORT).show();
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
