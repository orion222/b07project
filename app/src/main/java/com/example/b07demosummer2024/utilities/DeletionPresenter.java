package com.example.b07demosummer2024.utilities;

import android.content.Context;

import com.example.b07demosummer2024.interfaces.DeletionMVP;
import com.example.b07demosummer2024.models.Item;

import java.util.*;

public class DeletionPresenter implements DeletionMVP.Presenter {

    private DeletionMVP.View view;
    private DeletionMVP.Model model;

    public DeletionPresenter(DeletionMVP.Model model) {
        this.model = model;
    }

    @Override
    public void attachView(DeletionMVP.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void handleDeletion(Set<Integer> clickedList, List<Item> itemList) {
        if(view == null) return;

        model.deletion(clickedList, itemList);
        view.onDeletionSuccess();
    }
}
