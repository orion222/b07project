package com.example.b07demosummer2024;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;



public class Database {

    private static final FirebaseDatabase db = FirebaseDatabase.getInstance("https://b07proj-default-rtdb.firebaseio.com/");


    private Database() {};
    public static FirebaseDatabase getInstance() {
        return db;
    }

    public interface OnDataFetchedListener<T> {
        void onDataFetched(List<T> ret);
        void onError(DatabaseError error);
    }

    public static void fetchItems(OnDataFetchedListener listener){
        DatabaseReference myRef = db.getReference("items");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Item> itemList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // converts snapshot into an instance of the Item class
                    Item item = snapshot.getValue(Item.class);
                    itemList.add(item);
                    Log.d("TAGTEST", "1" + item);
                }

                // all items have been added to itemList, we can now access them
                for (Item i : itemList) {
                    Log.d("firebase", "name: " + i.getName());
                }

                listener.onDataFetched(itemList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w("firebase", "Failed to read value.", error.toException());
            }
        });

    }

    public static void fetchCredentials(OnDataFetchedListener listener) {
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
}
