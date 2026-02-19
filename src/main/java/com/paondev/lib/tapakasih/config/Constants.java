package com.paondev.lib.tapakasih.config;

/**
 * Constants for TapakAsih SDK
 */
public class Constants {
    // Network
    public static final String BASE_URL = "https://api.pycompany.com";
    public static final String ACTIVITY_ENDPOINT = "/actifity/claim";
    public static final String CHECK_ENDPOINT = "/activity/check";
    public static final String CONTENT_TYPE = "application/json";
    
    // Storage Keys
    public static final String PREFS_NAME = "tapakasih_prefs";
    public static final String KEY_SESSION_ID = "session_id";
    public static final String KEY_DEVELOPER_TOKEN = "developer_token";
    public static final String KEY_TOKEN_EXPIRED = "token_expired";
    
    // Request Settings
    public static final int MAX_RETRY_ATTEMPTS = 3;
    public static final int RETRY_DELAY_MS = 1000; // 1 second
    public static final int CONNECTION_TIMEOUT = 30; // seconds
    
    // Prevent instantiation
    private Constants() {}
}