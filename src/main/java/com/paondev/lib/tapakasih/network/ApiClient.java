package com.paondev.lib.tapakasih.network;

import android.util.Log;
import com.google.gson.Gson;
import com.paondev.lib.tapakasih.config.Constants;
import com.paondev.lib.tapakasih.config.TapakAsihConfig;
import com.paondev.lib.tapakasih.manager.TokenManager;
import okhttp3.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * API Client for sending activity data to server
 */
public class ApiClient {
    private static final String TAG = "ApiClient";
    
    private final OkHttpClient client;
    private final Gson gson;
    private final TokenManager tokenManager;
    private final TapakAsihConfig config;
    
    public ApiClient(TokenManager tokenManager, TapakAsihConfig config) {
        this.tokenManager = tokenManager;
        this.config = config;
        
        this.gson = new Gson();
        
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(Constants.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(Constants.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(Constants.CONNECTION_TIMEOUT, TimeUnit.SECONDS);
        
        this.client = builder.build();
    }
    
    /**
     * Check activity demand status from API
     * @return ActivityCheckResponse containing status, or null if request fails
     */
    public ActivityCheckResponse checkActivityDemand() {
        String developerToken = tokenManager.getDeveloperToken();
        if (developerToken == null) {
            if (config.isEnableDebugLogs()) {
                Log.w(TAG, "Developer token is null, cannot check activity demand");
            }
            // Default to ON_DEMAND (safer)
            return new ActivityCheckResponse("ON_DEMAND");
        }
        
        String url = Constants.BASE_URL + Constants.CHECK_ENDPOINT;
        
        try {
            Request httpRequest = new Request.Builder()
                    .url(url)
                    .addHeader("Authorization", "Bearer " + developerToken)
                    .get()
                    .build();
            
            Response response = client.newCall(httpRequest).execute();
            
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                ActivityCheckResponse checkResponse = gson.fromJson(responseBody, ActivityCheckResponse.class);
                
                if (config.isEnableDebugLogs()) {
                    Log.i(TAG, "Activity check successful: " + checkResponse.getStatus());
                }
                
                response.close();
                return checkResponse;
            } else {
                if (config.isEnableDebugLogs()) {
                    Log.w(TAG, "Activity check failed: HTTP " + response.code());
                }
                response.close();
                // Default to ON_DEMAND (safer)
                return new ActivityCheckResponse("ON_DEMAND");
            }
        } catch (IOException e) {
            if (config.isEnableDebugLogs()) {
                Log.e(TAG, "Activity check error: " + e.getMessage());
            }
            // Default to ON_DEMAND (safer)
            return new ActivityCheckResponse("ON_DEMAND");
        }
    }
    
    /**
     * Send activity data to API
     * @param request Activity request data
     * @return true if successful, false otherwise
     */
    public boolean sendActivity(ActivityRequest request) {
        if (tokenManager.isTokenExpired()) {
            if (config.isEnableDebugLogs()) {
                Log.w(TAG, "Token is expired, skipping activity send");
            }
            return false;
        }
        
        String developerToken = tokenManager.getDeveloperToken();
        if (developerToken == null) {
            if (config.isEnableDebugLogs()) {
                Log.w(TAG, "Developer token is null, skipping activity send");
            }
            return false;
        }
        
        String jsonBody = gson.toJson(request);
        String url = Constants.BASE_URL + Constants.ACTIVITY_ENDPOINT;
        
        for (int attempt = 1; attempt <= config.getRetryAttempts(); attempt++) {
            try {
                Request httpRequest = new Request.Builder()
                        .url(url)
                        .addHeader("Content-Type", Constants.CONTENT_TYPE)
                        .addHeader("Authorization", "Bearer " + developerToken)
                        .post(RequestBody.create(jsonBody, MediaType.parse(Constants.CONTENT_TYPE)))
                        .build();
                
                Response response = client.newCall(httpRequest).execute();
                
                if (response.isSuccessful()) {
                    if (config.isEnableDebugLogs()) {
                        Log.i(TAG, "Activity sent successfully: " + request.getPageName());
                    }
                    response.close();
                    return true;
                } else {
                    if (response.code() == 401 || response.code() == 403) {
                        // Token is expired or invalid
                        tokenManager.markTokenAsExpired();
                        if (config.isEnableDebugLogs()) {
                            Log.w(TAG, "Token expired or invalid (code: " + response.code() + ")");
                        }
                        response.close();
                        return false;
                    }
                    
                    if (config.isEnableDebugLogs()) {
                        Log.w(TAG, "Failed attempt " + attempt + ": HTTP " + response.code());
                    }
                    response.close();
                }
            } catch (IOException e) {
                if (config.isEnableDebugLogs()) {
                    Log.e(TAG, "Attempt " + attempt + " failed: " + e.getMessage());
                }
            }
            
            // Wait before retrying (except for last attempt)
            if (attempt < config.getRetryAttempts()) {
                try {
                    Thread.sleep(Constants.RETRY_DELAY_MS * attempt);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
        
        if (config.isEnableDebugLogs()) {
            Log.e(TAG, "Failed to send activity after " + config.getRetryAttempts() + " attempts");
        }
        return false;
    }
}