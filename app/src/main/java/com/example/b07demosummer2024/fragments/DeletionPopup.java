package com.example.b07demosummer2024.fragments;

import androidx.fragment.app.DialogFragment;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.b07demosummer2024.R;
import com.example.b07demosummer2024.interfaces.DeletionMVP;
import com.example.b07demosummer2024.models.DeletionModel;
import com.example.b07demosummer2024.models.Item;
import com.example.b07demosummer2024.utilities.DeletionPresenter;

import java.util.*;

public class DeletionPopup extends DialogFragment implements DeletionMVP.View {

    private RecyclerView recyclerView;
    private Button buttonConfirm;
    private Button buttonCancel;
    private DeletionPresenter presenter;
    private Set<Integer> clickedList;
    private List<Item> itemList;

    public DeletionPopup(Set<Integer> clickedList, List<Item> itemList) {
        this.clickedList = clickedList;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_deletion_popup, null);

        builder.setView(view);

        buttonConfirm = view.findViewById(R.id.buttonConfirm);
        buttonCancel = view.findViewById(R.id.buttonCancel);

        // initialize the presenter, give a concrete model as its argument
        presenter = new DeletionPresenter(new DeletionModel());
        presenter.attachView(this);

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.handleDeletion(clickedList, itemList);
                dismiss();
                switchFragment(new RecyclerViewFragment());
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return builder.create();
    }

    @Override
    public void onDeletionSuccess() {
        Toast.makeText(requireContext(), "Items Successfully Deleted", Toast.LENGTH_SHORT).show();
    }

    private void switchFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.home_fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
