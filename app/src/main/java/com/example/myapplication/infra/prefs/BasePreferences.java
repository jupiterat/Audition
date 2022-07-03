package com.example.myapplication.infra.prefs;

import android.content.Context;
import android.content.SharedPreferences;

public class BasePreferences {

    private static final String SP_NAME = "Preferences";

    final SharedPreferences sharedPreferences;

    BasePreferences(Context context) {
        this.sharedPreferences =
                context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    BasePreferences(Context context, String spName) {
        this.sharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
    }

    void putString(String key, String value) {
        sharedPreferences.edit()
                .putString(key, value)
                .apply();
    }

    void putBoolean(String key, boolean value) {
        sharedPreferences.edit()
                .putBoolean(key, value)
                .apply();
    }

    void putInt(String key, int value) {
        sharedPreferences.edit()
                .putInt(key, value)
                .apply();
    }

    void remove(String key) {
        sharedPreferences.edit()
                .remove(key)
                .apply();
    }
}
