package com.example.alien.myapplication1.Options;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by BotNaEasy on 2015-10-26.
 */
public class Preferences {
    Context context;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    public Preferences(Context context)
    {
        this.context = context;
        sharedPref = context.getSharedPreferences("PREFS", Activity.MODE_PRIVATE);
        editor = sharedPref.edit();
    }
    public boolean getPreferencesBoolean(String pref)
    {
        return sharedPref.getBoolean(pref, false);
    }
    public String getPreferencesString(String pref)
    {
        return sharedPref.getString(pref, "");
    }
    public int getPreferencesInt(String pref)
    {
        return sharedPref.getInt(pref, 0);
    }
    public float getPreferencesFloat(String pref)
    {
        return sharedPref.getFloat(pref, 0);
    }
    public long getPreferencesLong(String pref)
    {
        return sharedPref.getLong(pref, 0);
    }
    public void savePreferencesBoolean(String pref, boolean var)
    {
        editor.putBoolean(pref, var);
        editor.apply();
    }
    public void savePreferencesInt(String pref, int var )
    {
        editor.putInt(pref, var);
        editor.apply();
    }
    public void savePreferencesString(String pref, String var)
    {
        editor.putString(pref, var);
        editor.apply();
    }
    public void savePreferencesFloat(String pref, float var)
    {
        editor.putFloat(pref, var);
        editor.apply();
    }
    public void savePreferencesLong(String pref, long var)
    {
        editor.putLong(pref, var);
        editor.apply();
    }
}
