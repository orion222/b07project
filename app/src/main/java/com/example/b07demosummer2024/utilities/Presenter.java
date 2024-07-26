package com.example.b07demosummer2024.utilities;

import com.example.b07demosummer2024.interfaces.LoginMVP;

//this is the PRESENTER in MVP, it uses callbacks to link the model (backend) and view (frontend)
public class Presenter implements LoginMVP.Presenter {

    private LoginMVP.View view;
    private LoginMVP.Model model;

    public Presenter(LoginMVP.Model model) {
        this.model = model;
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

        model.login(username, password, new LoginMVP.Model.LoginCallback() {
            @Override
            public void onSuccess(String message) {
                if (view != null) {
                    view.onLoginSuccess(message);
                }
            }

            @Override
            public void onError(String message) {
                if (view != null) {
                    view.onLoginError(message);
                }
            }
        });
    }
}
