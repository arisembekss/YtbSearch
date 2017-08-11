package com.dtech.ytbsearch.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.dtech.ytbsearch.config.Config;

/**
 * Created by lenovo on 11/08/2017.
 */

public class PrefManager {
    SharedPreferences pref;
    public SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "app-welcome";

    public PrefManager(Context _context) {
        this._context = _context;

        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setJsonResponse(String response) {
        editor.putString(Config.RESPONSE, response);
        editor.commit();
    }
}
