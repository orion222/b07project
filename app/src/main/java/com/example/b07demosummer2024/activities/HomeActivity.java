package com.example.b07demosummer2024.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.b07demosummer2024.R;
import com.example.b07demosummer2024.fragments.AddItemFragment;
import com.example.b07demosummer2024.fragments.RecyclerViewFragment;
import com.example.b07demosummer2024.fragments.ReportFragment;
import com.example.b07demosummer2024.fragments.SearchFragment;
import com.example.b07demosummer2024.utilities.Preferences;
import com.example.b07demosummer2024.models.ItemViewModel;

public class HomeActivity extends AppCompatActivity {

    private boolean isAdmin;
    private ItemViewModel itemViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("WOW10", "activity commenced");

        setContentView(R.layout.activity_home);
        itemViewModel = new ViewModelProvider(this).get(ItemViewModel.class);

        // initialize spinner and adapter (used for dropdown)
        Spinner viewSpinner = findViewById(R.id.actionSpinner);
        ArrayAdapter<CharSequence> adapter;

        isAdmin = Preferences.getAdminStatus(this);
        if (isAdmin){
            adapter = ArrayAdapter.createFromResource(this,
                    R.array.adminActions, android.R.layout.simple_spinner_item);
        }
        else {
            adapter = ArrayAdapter.createFromResource(this,
                    R.array.userActions, android.R.layout.simple_spinner_item);
        }

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        viewSpinner.setAdapter(adapter);

        viewSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // load fragments based on selected action
                Fragment fragment = null;
                String tag = null;

                switch (position) {
                    case 1:
                        itemViewModel.fetchViewModelItems();
                        fragment = new RecyclerViewFragment();
                        tag = "RecyclerViewFragment";
                        break;
                    case 2:
                        fragment = new SearchFragment();
                        tag = "SearchFragment";
                        break;
                    case 3:
                        if (isAdmin) {
                            fragment = new AddItemFragment();
                            tag = "AddItemFragment";
                        }
                        break;
                    case 4:
                        if (isAdmin) {
                            fragment = new ReportFragment();
                            tag = "ReportFragment";
                        }
                        break;
                }

                if (fragment != null) {
                    loadFragment(fragment, tag);
                }
                parent.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // load initial fragment if needed
        if (savedInstanceState == null) {
            loadFragment(new RecyclerViewFragment(), "RecyclerViewFragment");
        }
    }

    private void loadFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // check if the fragment is already in the back stack
        Fragment existingFragment = fragmentManager.findFragmentByTag(tag);
        if (existingFragment != null) {
            // remove duplicates in the back stack
            fragmentManager.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        // add the new fragment
        transaction.replace(R.id.home_fragment_container, fragment, tag);
        transaction.addToBackStack(tag);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (fragmentManager.getBackStackEntryCount() > 1) {
            fragmentManager.popBackStack();
        } else {
            // when no fragments in back stack, go back to login - MainActivity
            RecyclerViewFragment.setDeleteMode(false);
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }

    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
