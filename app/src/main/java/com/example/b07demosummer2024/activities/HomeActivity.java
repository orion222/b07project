package com.example.b07demosummer2024.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.b07demosummer2024.R;
import com.example.b07demosummer2024.fragments.AddItemFragment;
import com.example.b07demosummer2024.fragments.RecyclerViewFragment;
import com.example.b07demosummer2024.fragments.LoginPopup;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // initialize spinner and adapter (used for dropdown)
        Spinner viewSpinner = findViewById(R.id.actionSpinner);


        //testing different spinner layouts depending on admin or not (can delete later)
        //previously the adapter was initialized based off of the old login
        ArrayAdapter<CharSequence> adapter;
        adapter = ArrayAdapter.createFromResource(this,
                R.array.userActions, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        viewSpinner.setAdapter(adapter);

        viewSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    // load Home Fragment
                    loadFragment(new RecyclerViewFragment());
                }
                else if (position == 1){
                    // load Search fragment
                    loadFragment(new AddItemFragment());
                }
                else if (position == 2){
                    // load Add fragment
                }
                else if (position == 3){
                    // load Remove fragment
                }
                else if (position == 4){
                    // load Report fragment
                }
                else{
                    // load back fragment
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.home_fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }


}