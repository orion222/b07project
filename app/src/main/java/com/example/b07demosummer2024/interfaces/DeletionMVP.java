package com.example.b07demosummer2024.interfaces;

import com.example.b07demosummer2024.models.Item;

import java.util.*;

public interface DeletionMVP {
    interface View {
        void onDeletionSuccess();
    }

    interface Presenter {
        void attachView(DeletionMVP.View view);
        void detachView();
        void handleDeletion(Set<Integer> clickedList, List<Item> itemList);
    }

    interface Model {
        void deletion(Set<Integer> clickedList, List<Item> itemList);
    }
}
