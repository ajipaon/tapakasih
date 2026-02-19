package com.paondev.lib.tapakasih.manager;

import android.content.Context;
import com.paondev.lib.tapakasih.config.Constants;
import com.paondev.lib.tapakasih.storage.LocalStorage;

/**
 * Manager for handling user session ID
 */
public class SessionManager {
    private final LocalStorage localStorage;
    
    public SessionManager(Context context) {
        this.localStorage = new LocalStorage(context);
    }
    
    /**
     * Save session ID to local storage
     * @param sessionId The session ID to save
     */
    public void saveSessionId(String sessionId) {
        localStorage.saveString(Constants.KEY_SESSION_ID, sessionId);
    }
    
    /**
     * Get session ID from local storage
     * @return The session ID, or null if not set
     */
    public String getSessionId() {
        return localStorage.getString(Constants.KEY_SESSION_ID, null);
    }
    
    /**
     * Check if session ID exists
     * @return true if session ID is set, false otherwise
     */
    public boolean hasSessionId() {
        String sessionId = getSessionId();
        return sessionId != null && !sessionId.trim().isEmpty();
    }
    
    /**
     * Clear session ID
     */
    public void clearSessionId() {
        localStorage.remove(Constants.KEY_SESSION_ID);
    }
}