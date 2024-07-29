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

    public LiveData<List<Item>> getItemList() {
        return itemList;
    }

    public LiveData<List<Item>> getFilteredList() {
        return filteredList;
    }

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
                if (item.getId().toLowerCase().contains(lotNumber) &&
                        item.getName().toLowerCase().contains(itemName) &&
                        item.getCategory().toLowerCase().contains(category) &&
                        item.getTimePeriod().toLowerCase().contains(period)) {
                    filteredItems.add(item);
                }
            }
            filteredList.postValue(filteredItems);
        }
    }
}
