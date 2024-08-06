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
    private Spinner viewSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("WOW10", "activity commenced");

        setContentView(R.layout.activity_home);

        // Initialize ViewModel
        itemViewModel = new ViewModelProvider(this).get(ItemViewModel.class);

        // initialize spinner and adapter (used for dropdown)
        viewSpinner = findViewById(R.id.actionSpinner);
        ArrayAdapter<CharSequence> adapter;

        isAdmin = Preferences.checkLogin(this);
        if (isAdmin) {
            adapter = ArrayAdapter.createFromResource(this,
                    R.array.adminActions, android.R.layout.simple_spinner_item);
        } else {
            adapter = ArrayAdapter.createFromResource(this,
                    R.array.userActions, android.R.layout.simple_spinner_item);
        }

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        viewSpinner.setAdapter(adapter);

        viewSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Load fragments based on selected action
                Fragment selectedFragment = null;

                if (position == 0) {
                    itemViewModel.fetchViewModelItems();
                    selectedFragment = new RecyclerViewFragment();
                } else if (position == 1) {
                    selectedFragment = new SearchFragment();
                }
                if (isAdmin) {
                    if (position == 2) selectedFragment = new AddItemFragment();
                    else if (position == 3) selectedFragment = new ReportFragment();
                }

                if (selectedFragment != null) {
                    // only load if it's not the currently displayed fragment
                    Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.home_fragment_container);
                    if (currentFragment == null || !currentFragment.getClass().equals(selectedFragment.getClass())) {
                        loadFragment(selectedFragment);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Listen for changes to the back stack
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.home_fragment_container);

                // Update the spinner based on the current fragment
                if (currentFragment instanceof RecyclerViewFragment) {
                    viewSpinner.setSelection(0);
                } else if (currentFragment instanceof SearchFragment) {
                    viewSpinner.setSelection(1);
                } else if (currentFragment instanceof AddItemFragment) {
                    viewSpinner.setSelection(2);
                } else if (currentFragment instanceof ReportFragment) {
                    viewSpinner.setSelection(3);
                }
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
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (fragmentManager.getBackStackEntryCount() > 0) {
            // pop the back stack to go back to the previous fragment
            fragmentManager.popBackStack();
        } else {
            // when no fragments in back stack (go back to login - MainActivity)
            RecyclerViewFragment.setDeleteMode(false);

            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
            finish();
        }
    }
}
