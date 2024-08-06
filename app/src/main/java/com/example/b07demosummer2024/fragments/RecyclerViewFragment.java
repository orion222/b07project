package com.example.b07demosummer2024.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.b07demosummer2024.models.ItemViewModel;
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
    private ImageButton buttonDelete;
    private ItemViewModel itemViewModel;
    private SearchView searchView;

    //defaults to 0
    private int currentPage;

    private static boolean deleteMode;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        searchView = view.findViewById(R.id.searchView);
        searchView.clearFocus();

        buttonNext = view.findViewById(R.id.buttonNext);
        buttonPrevious = view.findViewById(R.id.buttonPrevious);
        buttonDelete = view.findViewById(R.id.buttonDelete);

        // List to keep track of clicked items
        clickedList = new ArrayList<Item>();

        itemAdapter = new ItemAdapter(new ArrayList<>(), RecyclerViewFragment.this, getContext());
        recyclerView.setAdapter(itemAdapter);

        itemViewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);

        itemViewModel.getFilteredList().observe(getViewLifecycleOwner(), new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                itemList = items;
                updateRecyclerView();
                switchButtonState();
            }
        });

        if (itemViewModel.getItemList().getValue() == null) {
            itemViewModel.fetchViewModelItems();
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                itemViewModel.searchByNameOrDescription(newText.toLowerCase().trim());
                return true;
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedList.clear();
                changePage(currentPage + 1);
            }
        });

        buttonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedList.clear();
                changePage(currentPage - 1);
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
                    DeletionPopup temp = new DeletionPopup(clickedList, itemList);
                    temp.show(getParentFragmentManager(), "Item Deletion");
                }
            }
        });

        return view;
    }

    private void updateRecyclerView() {
        if (itemList != null) {
            List<Item> pageItems = Pagination.generatePage(currentPage, itemList);
            itemAdapter.setItems(pageItems);
        }
    }

    @Override
    public void itemClicked(int pos) {
//        Item clickedItem = itemList.get(pos);
        Item clickedItem = Pagination.generatePage(currentPage, itemList).get(pos);
        if (clickedList.contains(clickedItem)) {
            clickedList.remove(clickedItem);
        } else {
            clickedList.add(clickedItem);
        }

        // creates ViewFragment w/ item data in a bundle
        ViewFragment view = new ViewFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("key", clickedList.get(0));
        view.setArguments(bundle);
        Log.e("zebraaa", clickedList.get(0).toString());
        switchFragment(view);

    }

    public void changePage(int currentPage) {
        this.currentPage = currentPage;
        itemAdapter = new ItemAdapter(Pagination.generatePage(currentPage, itemList),
                RecyclerViewFragment.this, itemAdapter.getSet());
        recyclerView.setAdapter(itemAdapter);
        switchButtonState();
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

    public static void setDeleteMode(boolean c) {
        deleteMode = c;
//        if(c) {
//            Toast.makeText(requireContext(), "Delete Mode Activated", Toast.LENGTH_SHORT).show();
//        }
    }

    private void switchFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

        // I added animations, but the exit ones are kind of glitchy so didn't include them for now
        transaction.setCustomAnimations(
                R.anim.fragment_enter,
                R.anim.fragment_exit//,
//                R.anim.fragment_pop_enter,  // pop enter animation
//                R.anim.fragment_pop_exit    // pop exit animation
        );

        transaction.replace(R.id.home_fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
