package com.example.b07demosummer2024.utilities;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.b07demosummer2024.models.Credentials;
import com.example.b07demosummer2024.models.Item;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class Database {

    private static final FirebaseDatabase db = FirebaseDatabase.getInstance("https://b07proj-default-rtdb.firebaseio.com/");
    private static final FirebaseStorage storage = FirebaseStorage.getInstance("gs://b07proj.appspot.com/");

    private Database() {}
    public static FirebaseStorage getStorageInstance() {return storage; }

    public static FirebaseDatabase getInstance() {
        return db;
    }

    public interface OnDataFetchedListener<T> {
        void onDataFetched(List<T> ret);
        void onError(DatabaseError error);
    }

    // main method to retrieve all data
    public static void fetchItems(OnDataFetchedListener<Item> listener) {
        DatabaseReference myRef = db.getReference("items");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Item> itemList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Item item = snapshot.getValue(Item.class);
                    itemList.add(item);
                    Log.d("TAGTEST", "1" + item);
                }

                // log the fetched items
                for (Item i : itemList) {
                    Log.d("firebase", "name: " + i.getName());
                    if (i.getMedia() != null) {
                        Log.d("IMG", "first image: " + i.getMedia().getImagePaths().get(0));
                    }
                }

                listener.onDataFetched(itemList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("firebase", "Failed to read value.", error.toException());
            }
        });
    }

    public static void fetchCredentials(OnDataFetchedListener<Credentials> listener) {
        DatabaseReference myRef = db.getReference("admins");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Credentials> lst = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Credentials cred = snapshot.getValue(Credentials.class);
                    Log.d("cred", cred.toString());
                    lst.add(cred);
                }
                listener.onDataFetched(lst);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("firebase", "Failed to read value.", error.toException());
            }
        });
    }

    public static void fetchItemsFiltered(OnDataFetchedListener<Item> listener, String filterType, String filterValue) {
        DatabaseReference myRef = db.getReference("items");

        Query q = myRef.orderByChild(filterType).equalTo(filterValue);

        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Item> itemList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Item item = snapshot.getValue(Item.class);
                    itemList.add(item);
                }
                listener.onDataFetched(itemList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("SEARCH", "Failed to search for filter.", error.toException());
                listener.onError(error);
            }
        });
    }

    public static void deleteItemById(String id) {
        DatabaseReference myRef = db.getReference("items");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean itemFound = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Item item = snapshot.getValue(Item.class);
                    if (item != null && item.getId().equalsIgnoreCase(id)) {
                        snapshot.getRef().removeValue().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Log.d("DELETE", "onDataChange: DELETION SUCCESSFUL");
                            } else {
                                Log.d("DELETE", "onDataChange: DELETION FAILED");
                            }
                        });
                        itemFound = true;
                        break;
                    }
                }
                if (!itemFound) {
                    Log.d("DELETE", "onDataChange: ITEM NOT FOUND");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("DELETE", "onCancelled: CANCELLED");
            }
        });
    }

    public static void fetchItemByLotID(String lotID, OnDataFetchedListener<Item> listener) {
        DatabaseReference myRef = db.getReference("items");

        myRef.orderByChild("id").equalTo(lotID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Item> itemList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Item item = snapshot.getValue(Item.class);
                    itemList.add(item);
                    break; // Item is found
                }
                listener.onDataFetched(itemList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("SEARCH", "Failed to search for lotID.", error.toException());
                listener.onError(error);
            }
        });
    }
}
