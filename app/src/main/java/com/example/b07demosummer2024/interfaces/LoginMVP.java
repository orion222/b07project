package com.example.b07demosummer2024.interfaces;

public interface LoginMVP
{
    interface View {
        void onLoginSuccess(String message);
        void onLoginError(String message);
    }

    interface Presenter {
        void attachView(View view);
        void detachView();
        void handleLogin(String username, String password);
    }

    interface Model {
        void login(String username, String password, LoginCallback callback);

        interface LoginCallback {
            void onSuccess(String message);
            void onError(String message);
        }
    }
}
