package com.example.b07demosummer2024.activities;

import android.os.Bundle;
import android.util.Log;
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
import android.content.Intent;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.b07demosummer2024.R;
import com.example.b07demosummer2024.fragments.AddItemFragment;
import com.example.b07demosummer2024.fragments.LogoutPopup;
import com.example.b07demosummer2024.fragments.RecyclerViewFragment;
import com.example.b07demosummer2024.fragments.LoginPopup;
import com.example.b07demosummer2024.fragments.ReportFragment;
import com.example.b07demosummer2024.models.Item;
import com.example.b07demosummer2024.utilities.PdfCreator;
import com.example.b07demosummer2024.utilities.Preferences;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("WOW10", "activity commenced");

        setContentView(R.layout.activity_home);

        // initialize spinner and adapter (used for dropdown)
        Spinner viewSpinner = findViewById(R.id.actionSpinner);

        //testing different spinner layouts depending on admin or not (can delete later)
        //previously the adapter was initialized based off of the old login
        ArrayAdapter<CharSequence> adapter;
        adapter = ArrayAdapter.createFromResource(this,
                R.array.adminActions, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        viewSpinner.setAdapter(adapter);

        viewSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    // load Home Fragment
                    Log.d("WOW", String.valueOf(Preferences.checkLogin(getApplicationContext())));
                    loadFragment(new RecyclerViewFragment());
                    Log.d("WOW2", "recycler view made");

                }
                else if (position == 1){
                    // load Search fragment
                    loadFragment(new RecyclerViewFragment());
                }
                else if (position == 2){
                    // load Add fragment
                    loadFragment(new AddItemFragment());

                }
                else if (position == 3){
                    // load Remove fragment
                }
                else if (position == 4){
                    // load Report fragment
                    loadFragment(new ReportFragment());
//                    List<Item> test =  new ArrayList();
//                    test.add(new Item("bruh","name", "time","category", "There are many apps in which data from the app is provided to users in the downloadable PDF file format. So in this case we have to create a PDF file from the data present inside our app and represent that data properly inside our app. So by using this technique, we can easily create a new PDF according to our requirements. In this article, we will take a look at creating a new PDF file from the data present inside your Android app and saving that PDF file in the external storage of the users’ device. So for generating a new PDF file from the data present inside our Android app we will be using Canvas. Canvas is a predefined class in Android which is used to make 2D drawings of the different objects on our screen. So in this article, we will be using canvas to draw our data inside our canvas, and then we will store that canvas in the form of a PDF. Now we will move towards the implementation of our project. ",null));
//                    PdfCreator pdfCreator = new PdfCreator();
//                    pdfCreator.createPdf(getApplicationContext(),test, false);
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
        //when you press back, kill this activity
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (fragmentManager.getBackStackEntryCount() > 1) {
            // pop the back stack to go back to the previous fragment (changed to > 1)
            fragmentManager.popBackStack();
        } else {
            // when no fragments in back stack (go back to login - MainActivity)
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
            finish();
        }
    }



}