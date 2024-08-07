package com.example.b07demosummer2024.models;

import com.example.b07demosummer2024.fragments.RecyclerViewFragment;
import com.example.b07demosummer2024.interfaces.DeletionMVP;
import com.example.b07demosummer2024.utilities.Database;

import java.util.*;

public class DeletionModel implements DeletionMVP.Model{
    @Override
    public void deletion(Set<Integer> clickedList, List<Item> itemList) {
        Iterator iter = clickedList.iterator();

        while(iter.hasNext()) {
            Database.deleteItemById(iter.next().toString());
        }
        itemList.removeIf(item -> clickedList.contains(Integer.parseInt(item.getId())));

        RecyclerViewFragment.setDeleteMode(false);
    }
}
