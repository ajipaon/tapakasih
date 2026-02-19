package com.paondev.lib.tapakasih.storage;

import android.content.Context;
import android.content.SharedPreferences;
import com.paondev.lib.tapakasih.config.Constants;

/**
 * Local storage wrapper for SharedPreferences
 */
public class LocalStorage {
    private final SharedPreferences prefs;
    
    public LocalStorage(Context context) {
        this.prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
    }
    
    public void saveString(String key, String value) {
        prefs.edit().putString(key, value).apply();
    }
    
    public String getString(String key, String defaultValue) {
        return prefs.getString(key, defaultValue);
    }
    
    public void saveBoolean(String key, boolean value) {
        prefs.edit().putBoolean(key, value).apply();
    }
    
    public boolean getBoolean(String key, boolean defaultValue) {
        return prefs.getBoolean(key, defaultValue);
    }
    
    public void saveLong(String key, long value) {
        prefs.edit().putLong(key, value).apply();
    }
    
    public long getLong(String key, long defaultValue) {
        return prefs.getLong(key, defaultValue);
    }
    
    public void remove(String key) {
        prefs.edit().remove(key).apply();
    }
    
    public void clear() {
        prefs.edit().clear().apply();
    }
}