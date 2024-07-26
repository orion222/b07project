package com.example.b07demosummer2024;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class RecyclerViewFragment extends Fragment implements RecyclerViewInterface {
    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private List<Item> itemList;
    private Spinner viewSpinner;
    private List<Item> clickedList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // List to keep track of clicked items
        clickedList = new ArrayList<Item>();

        // create a listener for itemList, so that when
        // fetchItems() eventually returns the data,
        // itemList will be set accordingly
        itemList = new ArrayList<Item>();
        Database.fetchItems(new Database.OnDataFetchedListener(){
            @Override
            public void onDataFetched(List ret) {
                itemList.clear();
                itemList.addAll(ret);
                itemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(DatabaseError error) {
                Log.e("db err", "Failed to fetch items");
            }
        });

        itemAdapter = new ItemAdapter(itemList, this);
        recyclerView.setAdapter(itemAdapter);

        // initialize spinner and adapter (used for dropdown)
        viewSpinner = view.findViewById(R.id.actionSpinner);


        //testing different spinner layouts depending on admin or not (can delete later)
        ArrayAdapter<CharSequence> adapter;
        if (LoginPopup.admin) {
            adapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.adminActions, android.R.layout.simple_spinner_item);
        } else {
            adapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.userActions, android.R.layout.simple_spinner_item);
        }

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        viewSpinner.setAdapter(adapter);

        viewSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // position = 0 represents all items
                if (position == 0) {
                    // load detailed view fragment
                }
                else if (position == 1){
                    // load Search Fragment
                }
                else if (position == 2){
                    // load Add fragment
                }
                else if (position == 3){
                    // load remove fragment
                }
                else if (position == 4){
                    // load report fragment
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

        return view;
    }

    @Override
    public void itemClicked(int pos) {
        Item clickedItem = itemList.get(pos);

        if (clickedList.contains(clickedItem)) {
            clickedList.remove(clickedItem);
        } else {
            clickedList.add(clickedItem);
        }

        // Temp implementation of view function, only allow 1 item at a time
        // Modify later to incorporate remove functionality w/ multiple items allowed
        if (clickedList.size() != 1) {
            Toast.makeText(getContext(), "Please select only ONE item to view", Toast.LENGTH_SHORT).show();
        } else {
            // Creates ViewFragment w/ item data in a bundle
            ViewFragment fragment = new ViewFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("key", clickedList.get(0));
            fragment.setArguments(bundle);

            switchFragment(fragment);
        }
    }


    private void switchFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
