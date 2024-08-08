package com.example.b07demosummer2024.models;

import android.util.Log;

import com.example.b07demosummer2024.utilities.Database;
import com.example.b07demosummer2024.interfaces.LoginMVP;
import com.google.firebase.database.DatabaseError;
import java.util.ArrayList;
import java.util.List;

//this is the MODEL in MVP, it verifies the login
public class UserModel implements LoginMVP.Model {

    private List<Credentials> credentialsList;

    public UserModel() {
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
                        //tells the presenter that it was si jusuccessful, and passes the uesrname
                        callback.onSuccess("Login successful!", username);
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
