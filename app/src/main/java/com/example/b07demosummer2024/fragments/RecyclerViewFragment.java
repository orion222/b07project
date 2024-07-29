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
    private ItemViewModel itemViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        buttonNext = view.findViewById(R.id.buttonNext);
        buttonPrevious = view.findViewById(R.id.buttonPrevious);

        // List to keep track of clicked items
        clickedList = new ArrayList<>();

        itemAdapter = new ItemAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(itemAdapter);

        itemViewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);

        itemViewModel.getFilteredList().observe(getViewLifecycleOwner(), new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                itemList = items; // Update itemList with the new filtered items
                updateRecyclerView();
                switchButtonState();
            }
        });

        if (itemViewModel.getItemList().getValue() == null) {
            itemViewModel.fetchViewModelItems();
        }

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPage++;
                updateRecyclerView();
                switchButtonState();
            }
        });

        buttonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPage--;
                updateRecyclerView();
                switchButtonState();
            }
        });

        Log.d("WOW4", "goes through recycler");

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
        Item clickedItem = itemAdapter.getItems().get(pos);

        if (clickedList.contains(clickedItem)) {
            clickedList.remove(clickedItem);
        } else {
            clickedList.add(clickedItem);
        }

        if (clickedList.size() != 1) {
            Toast.makeText(getContext(), "Please select only ONE item to view", Toast.LENGTH_SHORT).show();
        } else {
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

        if (size % Pagination.getItemsPerPage() == 0) lastPage--;

        if (size <= Pagination.getItemsPerPage()) {
            buttonNext.setEnabled(false);
            buttonPrevious.setEnabled(false);
        } else if (currentPage == 0) {
            buttonPrevious.setEnabled(false);
            buttonNext.setEnabled(true);
        } else if (currentPage == lastPage) {
            buttonNext.setEnabled(false);
            buttonPrevious.setEnabled(true);
        } else {
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
