package com.example.settingsscreen;

import android.content.Context;
import android.content.SharedPreferences;

public class DriverSharedPref {

    //the constants
    private static final String SHARED_PREF_NAME = "driversharedpref";
    private static final String KEY_ID = "keyid";

    private static DriverSharedPref mInstance;
    private static Context mCtx;

    private DriverSharedPref(Context context) {
        mCtx = context;
    }

    public static synchronized DriverSharedPref getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DriverSharedPref(context);
        }
        return mInstance;
    }

    public void startTrip(String tripNo) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ID, tripNo);
        editor.apply();
    }

    public boolean hasActiveTrip() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ID, null) != null;
    }

    //this method will logout the user
    public void endTrip() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

    }

}
