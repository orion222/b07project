package com.example.b07demosummer2024.utilities;

import com.example.b07demosummer2024.interfaces.LogoutMVP;
import android.content.Context;

public class LogoutPresenter implements LogoutMVP.Presenter {
    private LogoutMVP.View view;
    private Context context;

    //constructor
    public LogoutPresenter(LogoutMVP.View view, Context context) {
        this.view = view;
        this.context = context;
    }

    @Override
    public void logout() {
        Preferences.logout(context);
        view.onLogoutSuccess();
    }

}

