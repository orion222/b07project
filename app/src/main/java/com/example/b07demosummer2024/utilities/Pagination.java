package com.example.b07demosummer2024.utilities;

import com.example.b07demosummer2024.models.Item;
import java.util.*;

public class Pagination {
    // can switch num of items to display per page
    private static final int ITEMS_PER_PAGE = 3;

    public static List<Item> generatePage(int currentPage, List<Item> itemList) {
        if(itemList == null) return new ArrayList<Item>();

        int numItems = itemList.size();

        int startingItem = currentPage * ITEMS_PER_PAGE;
        int lastPage = numItems / ITEMS_PER_PAGE;
        int itemsRemaining = numItems % ITEMS_PER_PAGE;

        List<Item> itemsToDisplay = new ArrayList<Item>();

        if(currentPage < lastPage) {
            for(int i = startingItem; i < startingItem + ITEMS_PER_PAGE; i++) {
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

    public static int getItemsPerPage() {
        return ITEMS_PER_PAGE;
    }
}
