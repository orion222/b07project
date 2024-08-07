package com.example.b07demosummer2024.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.b07demosummer2024.utilities.Preferences;
import com.example.b07demosummer2024.R;
import com.example.b07demosummer2024.fragments.LoginFragment;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("WOW10", "main activity commenced");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean isLoggedIn = Preferences.getAdminStatus(this);
        if (savedInstanceState == null) {
            Log.d("wow15", "Is logged in: " + isLoggedIn);

            loadFragment(new LoginFragment());
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onStop() {
        super.onStop();

        boolean autoLogoutStatus = Preferences.getAutoLogoutStatus(this);
//        Log.d("LOGOUT", "AUTOLOGOUT VAL: " + autoLogoutStatus);
        if (autoLogoutStatus && Preferences.getAdminStatus(this)) {
            Preferences.logout(this);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }


    @Override
    public void onBackPressed() {
//        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
//            getSupportFragmentManager().popBackStack();
//        } else {
//            super.onBackPressed();
//        }
    }
}