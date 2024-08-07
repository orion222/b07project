package com.example.b07demosummer2024.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.b07demosummer2024.utilities.Preferences;


import com.example.b07demosummer2024.R;
import com.example.b07demosummer2024.activities.HomeActivity;

public class LoginFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        // Find buttons by their IDs
        Button loginAdmin = view.findViewById(R.id.adminButton);
        Button loginGuest = view.findViewById(R.id.guestButton);
        Button logoutButton = view.findViewById(R.id.logoutButton); // initially hidden

        TextView continueMSG = view.findViewById(R.id.textContinueAs);

        boolean loggedIn = Preferences.getAdminStatus(requireContext());
        Log.d("ZEBRA3", "Is logged in: " + loggedIn);
        //conditional rendering for logged in
        if (loggedIn) {
            String user = Preferences.getUser(requireContext());
            // welcomes with username if not null
            if (user == null)
            {
                continueMSG.setText(getString(R.string.welcome_back, "Unnamed User"));
            }
            continueMSG.setText(getString(R.string.welcome_back, user));
            loginGuest.setVisibility(View.INVISIBLE);
            logoutButton.setVisibility(View.VISIBLE);
        } else {
            // ensure visibility otherwise
            loginAdmin.setVisibility(View.VISIBLE);
            loginGuest.setVisibility(View.VISIBLE);
            logoutButton.setVisibility(View.INVISIBLE);

        }

        // set click listeners for the buttons
        loginAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Admin login clicked", Toast.LENGTH_SHORT).show();

                //if admin is already logged in, don't need to re-enter information
                if (loggedIn)
                {
                    Intent intent = new Intent(requireContext(), HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    LoginPopup custom = new LoginPopup();
                    custom.show(getParentFragmentManager(), "Admin Login");
                    //eventually starts new activity
                }
            }
        });

        loginGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(requireContext(), "Guest login clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(requireContext(), HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
//                loadFragment(new RecyclerViewFragment());
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //initiates logout
                //Toast.makeText(requireContext(), "Logout pressed", Toast.LENGTH_SHORT).show();
                LogoutPopup custom = new LogoutPopup();
                custom.show(getParentFragmentManager(), "Logout");
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
