package com.example.b07demosummer2024;

import android.app.Application;
import android.util.Log;

import com.example.b07demosummer2024.utilities.Preferences;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        Log.d("APP_CONFIG", "start");
        super.onCreate();
    }

    // used for when the app is closed
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            boolean autoLogoutStatus = Preferences.getAutoLogoutStatus(this);
            Log.d("APP_CONFIG", "Auto Logout Value: " + autoLogoutStatus);
            if (autoLogoutStatus && Preferences.getAdminStatus(this)) {
                Preferences.logout(this);
            }
        }
    }
}