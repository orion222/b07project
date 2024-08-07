package com.example.b07demosummer2024.models;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.b07demosummer2024.utilities.Database;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

public class ItemViewModel extends ViewModel {
    private MutableLiveData<List<Item>> itemList = new MutableLiveData<>();
    private MutableLiveData<List<Item>> filteredList = new MutableLiveData<>();
    private MutableLiveData<Boolean> noResults = new MutableLiveData<>(false);

    public LiveData<List<Item>> getItemList() {
        return itemList;
    }

    public LiveData<List<Item>> getFilteredList() {
        return filteredList;
    }

    public LiveData<Boolean> getNoResults() { return noResults; }

    public void fetchViewModelItems() {
        Database.fetchItems(new Database.OnDataFetchedListener<Item>() {
            @Override
            public void onDataFetched(List<Item> ret) {
                itemList.postValue(ret);
                filteredList.postValue(ret);
            }

            @Override
            public void onError(DatabaseError error) {
                Log.e("db err", "Failed to fetch items");
            }
        });
    }

    public void search(String lotNumber, String itemName, String category, String period) {
        List<Item> allItems = itemList.getValue();
        if (allItems != null) {
            List<Item> filteredItems = new ArrayList<>();
            for (Item item : allItems) {
                boolean matchesLotNumber = lotNumber.isEmpty() ||
                        item.getId().equals(lotNumber);
                boolean matchesName = itemName.isEmpty() ||
                        (item.getName().toLowerCase()).contains(itemName);
                boolean matchesCategory = category.isEmpty() ||
                        (item.getCategory().toLowerCase()).contains(category);
                boolean matchesPeriod = period.isEmpty() ||
                        (item.getTimePeriod().toLowerCase()).contains(period);

                if (matchesLotNumber && matchesName && matchesCategory && matchesPeriod) {
                    filteredItems.add(item);
                }
            }
            if (filteredItems.isEmpty()) {
                noResults.postValue(true);
            } else {
                noResults.postValue(false);
            }
            filteredList.postValue(filteredItems);
        }
    }

    public void searchByNameOrDescription(String query) {
        List<Item> allItems = itemList.getValue();
        if (allItems != null) {
            List<Item> nameMatches = new ArrayList<>();
            List<Item> descriptionMatches = new ArrayList<>();

            for (Item item : allItems) {
                boolean matchesName = item.getName().toLowerCase().contains(query.toLowerCase());
                boolean matchesDescription = item.getDescription().toLowerCase().contains(query.toLowerCase());

                if (matchesName) {
                    nameMatches.add(item);
                } else if (matchesDescription) {
                    descriptionMatches.add(item);
                }
            }

            // Combine the lists: name matches first, then description matches
            List<Item> filteredItems = new ArrayList<>(nameMatches);
            filteredItems.addAll(descriptionMatches);

            if (filteredItems.isEmpty()) {
                noResults.postValue(true);
            } else {
                noResults.postValue(false);
            }

            filteredList.postValue(filteredItems);
        }
    }
}