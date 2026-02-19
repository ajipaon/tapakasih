package com.paondev.lib.tapakasih.manager;

import android.content.Context;
import com.paondev.lib.tapakasih.config.Constants;
import com.paondev.lib.tapakasih.storage.LocalStorage;

/**
 * Manager for handling developer token
 */
public class TokenManager {
    private final LocalStorage localStorage;
    
    public TokenManager(Context context) {
        this.localStorage = new LocalStorage(context);
    }
    
    /**
     * Save developer token to local storage
     * @param token The developer token to save
     */
    public void saveDeveloperToken(String token) {
        localStorage.saveString(Constants.KEY_DEVELOPER_TOKEN, token);
        // Reset expired flag when new token is saved
        localStorage.saveBoolean(Constants.KEY_TOKEN_EXPIRED, false);
    }
    
    /**
     * Get developer token from local storage
     * @return The developer token, or null if not set
     */
    public String getDeveloperToken() {
        return localStorage.getString(Constants.KEY_DEVELOPER_TOKEN, null);
    }
    
    /**
     * Check if developer token exists
     * @return true if token is set, false otherwise
     */
    public boolean hasDeveloperToken() {
        String token = getDeveloperToken();
        return token != null && !token.trim().isEmpty();
    }
    
    /**
     * Mark token as expired (called when API returns 401/403)
     */
    public void markTokenAsExpired() {
        localStorage.saveBoolean(Constants.KEY_TOKEN_EXPIRED, true);
    }
    
    /**
     * Check if token is marked as expired
     * @return true if token is expired, false otherwise
     */
    public boolean isTokenExpired() {
        return localStorage.getBoolean(Constants.KEY_TOKEN_EXPIRED, false);
    }
    
    /**
     * Clear developer token
     */
    public void clearDeveloperToken() {
        localStorage.remove(Constants.KEY_DEVELOPER_TOKEN);
        localStorage.remove(Constants.KEY_TOKEN_EXPIRED);
    }
}