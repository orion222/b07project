package com.example.b07demosummer2024.utilities;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PrefConstants.PREFS_NAME, Context.MODE_PRIVATE);
    }

    /**
     * need to invoke with a context parameter, this is important
     * if you are in a fragment you use getContext(), if you are in an activity you can use 'this'
     * ---
     * to access the value at any given time you must call Preferences.getAdminStatus(Context context)
     * the 'context' by the aforementioned rules above
     */

    //setters
    public static void saveLogin(Context context, boolean isLoggedIn) {
        //first two lines open the editor
        SharedPreferences sharedPref = getSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();

        //stores the boolean loginState into the sharedPreference
        editor.putBoolean(PrefConstants.LOGIN_STATE, isLoggedIn);
        editor.apply();
    }

    public static void saveUser(Context context, String username) {
        //first two lines open the editor
        SharedPreferences sharedPref = getSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();

        //stores the username of logged in person
        editor.putString(PrefConstants.USER, username);
        editor.apply();
    }

    public static void saveAutoLogoutStatus(Context context, boolean autoLogoutStatus) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(PrefConstants.AUTO_LOGOUT_STATUS, autoLogoutStatus);
        editor.apply();
    }

//    public static void saveMaxEntryCount(Context context, int maxEntryCount) {
//        SharedPreferences sharedPref = getSharedPreferences(context);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putInt(PrefConstants.MAX_ENTRY_COUNT, maxEntryCount);
//        editor.apply();
//    }

    //logs out
    public static void logout(Context context){
        SharedPreferences sharedPref = getSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(PrefConstants.USER);
        editor.remove(PrefConstants.LOGIN_STATE);
        editor.apply();
    }

    // getters
    public static String getUser(Context context) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        // the getString method takes a 2nd argument called a default, here its ""
        return sharedPref.getString(PrefConstants.USER, "");
    }

    public static boolean getAdminStatus(Context context) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        // false by default
        return sharedPref.getBoolean(PrefConstants.LOGIN_STATE, false);
    }

    public static boolean getAutoLogoutStatus(Context context) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        // false by default
        return sharedPref.getBoolean(PrefConstants.AUTO_LOGOUT_STATUS, false);
    }

//    public static int getMaxEntryCount(Context context) {
//        SharedPreferences sharedPref = getSharedPreferences(context);
//        // 6 is default
//        return sharedPref.getInt(PrefConstants.MAX_ENTRY_COUNT, 6);
//    }

}
