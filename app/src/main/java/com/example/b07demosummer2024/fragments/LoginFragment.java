package com.example.b07demosummer2024.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.b07demosummer2024.R;

public class LoginFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        // Find buttons by their IDs
        Button loginAdmin = view.findViewById(R.id.adminButton);
        Button loginGuest = view.findViewById(R.id.guestButton);

        // Set click listeners for the buttons
        loginAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Admin login clicked", Toast.LENGTH_SHORT).show();
//                loadFragment(new RecyclerViewFragment());
                LoginPopup custom = new LoginPopup();
                custom.show(getParentFragmentManager(), "Admin Login");
            }
        });

        loginGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Guest login clicked", Toast.LENGTH_SHORT).show();
                loadFragment(new RecyclerViewFragment());
            }
        });

        return view;
    }
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
