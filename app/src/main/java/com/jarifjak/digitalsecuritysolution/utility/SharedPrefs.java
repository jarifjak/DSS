package com.jarifjak.digitalsecuritysolution.utility;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefs {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public SharedPrefs(Context context) {

        prefs = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
    }

    public void setLoggedIn(boolean value) {

        editor = prefs.edit();

        editor.putBoolean(Constants.PREF_LOGGED_IN, value);
        editor.apply();
    }

    public boolean isLoggedIn() {

        return prefs.getBoolean(Constants.PREF_LOGGED_IN, false);
    }

}
