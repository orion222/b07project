package com.example.b07demosummer2024.interfaces;

public interface LogoutMVP {
    interface View {
        void onLogoutSuccess();
    }

    interface Presenter {
        void logout();
        void attachView(LogoutMVP.View view);
        void detachView();
    }
}
