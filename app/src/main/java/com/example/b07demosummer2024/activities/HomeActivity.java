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
import androidx.appcompat.view.menu.MenuView;
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

        //Initialize ViewModel
        itemViewModel = new ViewModelProvider(this).get(ItemViewModel.class);

        // initialize spinner and adapter (used for dropdown)
        Spinner viewSpinner = findViewById(R.id.actionSpinner);
        ArrayAdapter<CharSequence> adapter;

        isAdmin = Preferences.checkLogin(this);
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
                if (position == 0) {
                    // load home fragment (view)
                    itemViewModel.fetchViewModelItems();
                    loadFragment(new RecyclerViewFragment());
                } else if (position == 1) {
                    loadFragment(new SearchFragment());
                }
                if (isAdmin) {
                    if (position == 2) loadFragment(new AddItemFragment());
                    else if (position == 3) loadFragment(new ReportFragment());
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
        //when you press back, kill this activity
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (fragmentManager.getBackStackEntryCount() > 1) {
            // pop the back stack to go back to the previous fragment (changed to > 1)
            fragmentManager.popBackStack();
        } else {
            // when no fragments in back stack (go back to login - MainActivity)

            // there were currently some issues with this
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