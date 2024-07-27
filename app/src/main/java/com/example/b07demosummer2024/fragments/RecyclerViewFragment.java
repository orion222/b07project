package com.example.b07demosummer2024.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.b07demosummer2024.utilities.Database;
import com.example.b07demosummer2024.models.Item;
import com.example.b07demosummer2024.utilities.ItemAdapter;
import com.example.b07demosummer2024.R;
import com.example.b07demosummer2024.interfaces.RecyclerViewInterface;
import com.example.b07demosummer2024.utilities.Pagination;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewFragment extends Fragment implements RecyclerViewInterface {
    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private List<Item> itemList;
    private List<Item> clickedList;
    private Button buttonNext;
    private Button buttonPrevious;
    private int currentPage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        buttonNext = view.findViewById(R.id.buttonNext);
        buttonPrevious = view.findViewById(R.id.buttonPrevious);

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

                itemAdapter = new ItemAdapter(Pagination.generatePage(currentPage, itemList), RecyclerViewFragment.this);
                recyclerView.setAdapter(itemAdapter);
                switchButtonState();

                itemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(DatabaseError error) {
                Log.e("db err", "Failed to fetch items");
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPage++;
                itemAdapter = new ItemAdapter(Pagination.generatePage(currentPage, itemList), RecyclerViewFragment.this);
                recyclerView.setAdapter(itemAdapter);
                switchButtonState();
            }
        });

        buttonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPage--;
                itemAdapter = new ItemAdapter(Pagination.generatePage(currentPage, itemList), RecyclerViewFragment.this);
                recyclerView.setAdapter(itemAdapter);
                switchButtonState();
            }
        });

//        itemAdapter = new ItemAdapter(Pagination.generatePage(currentPage, itemList), RecyclerViewFragment.this);
//        recyclerView.setAdapter(itemAdapter);
        Log.d("WOW4", "goes through recycler");

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
            ViewFragment view = new ViewFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("key", clickedList.get(0));
            view.setArguments(bundle);

            switchFragment(view);
        }
    }

    private void switchButtonState() {
        int size = itemList.size();
        int lastPage = size / Pagination.getItemsPerPage();

        if(size % Pagination.getItemsPerPage() == 0) lastPage--;

        if(size <= Pagination.getItemsPerPage()) {
            buttonNext.setEnabled(false);
            buttonPrevious.setEnabled(false);
        }
        else if(currentPage == 0) {
            buttonPrevious.setEnabled(false);
            buttonNext.setEnabled(true);
        }
        else if(currentPage == lastPage) {
            buttonNext.setEnabled(false);
            buttonPrevious.setEnabled(true);
        }
        else {
            buttonNext.setEnabled(true);
            buttonPrevious.setEnabled(true);
        }
    }


    private void switchFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.home_fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
