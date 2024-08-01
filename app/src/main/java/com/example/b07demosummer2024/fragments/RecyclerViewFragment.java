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

import java.util.*;

public class RecyclerViewFragment extends Fragment implements RecyclerViewInterface {
    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private List<Item> itemList;
    private List<Item> clickedList;
    private Button buttonNext;
    private Button buttonPrevious;
    private Button buttonDelete;

    //defaults to 0
    private int currentPage;

    private static boolean deleteMode;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        buttonNext = view.findViewById(R.id.buttonNext);
        buttonPrevious = view.findViewById(R.id.buttonPrevious);
        buttonDelete = view.findViewById(R.id.buttonDelete);

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
                // too much text in one line, can change later
                itemAdapter = new ItemAdapter(Pagination.generatePage(currentPage, itemList),
                        RecyclerViewFragment.this, itemAdapter.getSet());
                recyclerView.setAdapter(itemAdapter);
                switchButtonState();
            }
        });

        buttonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPage--;
                itemAdapter = new ItemAdapter(Pagination.generatePage(currentPage, itemList),
                        RecyclerViewFragment.this, itemAdapter.getSet());
                recyclerView.setAdapter(itemAdapter);
                switchButtonState();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Set<Integer> clickedList = itemAdapter.getSet();

                if(clickedList.isEmpty()) {
                    Toast.makeText(view.getContext(), "Long-Press Items to Delete", Toast.LENGTH_SHORT).show();
                }
                else {
                    Iterator iter = clickedList.iterator();
                    while(iter.hasNext()) {
                        Database.deleteItemById(iter.next().toString());
                    }
                    itemList.removeIf(item -> clickedList.contains(Integer.parseInt(item.getId())));

                    Toast.makeText(view.getContext(), "Items Successfully Deleted", Toast.LENGTH_SHORT).show();
                    deleteMode = false;

                    currentPage = 0;
                    itemAdapter = new ItemAdapter(Pagination.generatePage(currentPage, itemList),
                            RecyclerViewFragment.this, itemAdapter.getSet());
                    recyclerView.setAdapter(itemAdapter);
                    switchButtonState();
                }
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

        // Creates ViewFragment w/ item data in a bundle
        ViewFragment view = new ViewFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("key", clickedList.get(0));
        view.setArguments(bundle);

        switchFragment(view);
    }

    // changes UI, we need to move this to a new class
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

    //getters and setters
    public static boolean getDeleteMode() {
        return deleteMode;
    }

    public static void setDeleteMode(boolean c, View view) {
        deleteMode = c;

        if(c) {
            Toast.makeText(view.getContext(), "Delete Mode Activated", Toast.LENGTH_SHORT).show();
        }
    }

    private void switchFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.home_fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
