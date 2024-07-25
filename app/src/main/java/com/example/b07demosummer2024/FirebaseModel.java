package com.example.b07demosummer2024;

import android.util.Log;

import com.google.firebase.database.DatabaseError;
import java.util.ArrayList;
import java.util.List;

//this is the MODEL in MVP, it verifies the login
public class FirebaseModel implements LoginMVP.Model {

    private List<Credentials> credentialsList;

    public FirebaseModel() {
        credentialsList = new ArrayList<>();
    }

    @Override
    public void login(String username, String password, LoginCallback callback) {
        //fetch credentials
        Database.fetchCredentials(new Database.OnDataFetchedListener() {
            @Override
            public void onDataFetched(List ret) {
                credentialsList.clear();
                credentialsList.addAll(ret);
                Log.d("cred1", "List size is " + ret.size());

                for (Credentials c : credentialsList) {
//                    Log.d("BOOL", String.valueOf(username.equals(c.getUsername()) && password.equals(c.getPassword())));
                    if (username.equals(c.getUsername()) && password.equals(c.getPassword())) {
                        callback.onSuccess("Login successful!");
                        return;
                    }
                }
                callback.onError("Invalid username or password.");
            }

            @Override
            public void onError(DatabaseError error) {
                Log.e("db err", "Failed to fetch credentials");
                callback.onError("Failed to fetch credentials");
            }
        });
    }
}
