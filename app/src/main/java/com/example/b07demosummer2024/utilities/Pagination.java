package com.example.b07demosummer2024.utilities;

import com.example.b07demosummer2024.models.Item;
import java.util.*;

public class Pagination {
    // can switch num of items to display per page
    private static int items_per_page = 6;

    public static List<Item> generatePage(int currentPage, List<Item> itemList) {
        if(itemList == null) return new ArrayList<Item>();

        int numItems = itemList.size();

        int startingItem = currentPage * items_per_page;
        int lastPage = numItems / items_per_page;
        int itemsRemaining = numItems % items_per_page;

        List<Item> itemsToDisplay = new ArrayList<Item>();

        if(currentPage < lastPage) {
            for(int i = startingItem; i < startingItem + items_per_page; i++) {
                itemsToDisplay.add(itemList.get(i));
            }
        }
        else if(itemsRemaining > 0) {
            for(int i = startingItem; i < startingItem + itemsRemaining; i++) {
                itemsToDisplay.add(itemList.get(i));
            }
        }

        return itemsToDisplay;
    }

    public static void setItemsPerPage(int num) {
        items_per_page = num;
    }
    public static int getItemsPerPage() {
        return items_per_page;
    }
}
