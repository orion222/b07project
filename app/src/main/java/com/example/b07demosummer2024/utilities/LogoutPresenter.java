package com.example.b07demosummer2024.utilities;

import com.example.b07demosummer2024.interfaces.LoginMVP;
import com.example.b07demosummer2024.interfaces.LogoutMVP;
import android.content.Context;

public class LogoutPresenter implements LogoutMVP.Presenter {
    private LogoutMVP.View view;
    private Context context;

    // constructor
    public LogoutPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void logout() {
        Preferences.logout(context);
        view.onLogoutSuccess();
    }

    // getters and setters
    @Override
    public void attachView(LogoutMVP.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }

}

