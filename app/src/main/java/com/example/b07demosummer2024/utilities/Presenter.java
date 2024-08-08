package com.example.b07demosummer2024.utilities;

import com.example.b07demosummer2024.interfaces.LoginMVP;
import android.content.Context;

//this is the PRESENTER in MVP, it uses callbacks to link the model (backend) and view (frontend)
public class Presenter implements LoginMVP.Presenter {

    private LoginMVP.View view;
    private LoginMVP.Model model;

    //need to store login status and admin username
    private Context context;

    public Presenter(LoginMVP.Model model, Context context) {
        this.model = model;
        this.context = context;
    }

    //tbh idk why we cant directly attach it but this is what the thing said to do
    @Override
    public void attachView(LoginMVP.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void handleLogin(String username, String password) {
        if (view == null) return;

        //calling login method on UserModel object sends back callback
        model.login(username, password, new LoginMVP.Model.LoginCallback() {
            @Override
            public void onSuccess(String message, String username) {
                if (view != null) {
                    Preferences.saveLogin(context, true); //saves login state
                    Preferences.saveUser(context, username); //saves username
                    view.onLoginSuccess(message);
                }
            }

            @Override
            public void onError(String message) {
                if (view != null) {
                    Preferences.saveLogin(context, false); //makes login state false, may remove this
                    view.onLoginError(message);
                }
            }
        });
    }

    // Getter for the view, useful for testing
    public LoginMVP.View getView() {
        return view;
    }
}
