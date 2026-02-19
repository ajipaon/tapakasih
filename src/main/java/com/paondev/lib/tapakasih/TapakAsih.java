package com.paondev.lib.tapakasih;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.paondev.lib.tapakasih.config.TapakAsihConfig;
import com.paondev.lib.tapakasih.manager.SessionManager;
import com.paondev.lib.tapakasih.manager.TokenManager;
import com.paondev.lib.tapakasih.network.ActivityRequest;
import com.paondev.lib.tapakasih.network.ApiClient;
import com.paondev.lib.tapakasih.tracker.ActivityTracker;
import com.paondev.lib.tapakasih.util.SessionDialog;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * TapakAsih SDK - Activity Tracking SDK
 * 
 * Main class for initializing and using the TapakAsih SDK.
 * 
 * Usage:
 * <pre>
 * // Initialize in Application class
 * TapakAsihConfig config = new TapakAsihConfig.Builder("your_developer_token")
 *     .setEnableDebugLogs(true)
 *     .build();
 * TapakAsih.initialize(application, config);
 * 
 * // Track page manually
 * TapakAsih.trackPage("MainActivity");
 * 
 * // Set session ID
 * TapakAsih.setSessionId("user-session-id");
 * </pre>
 */
public class TapakAsih {
    private static final String TAG = "TapakAsih";
    
    private static TapakAsih instance;
    private boolean isInitialized = false;
    
    private Context context;
    private TapakAsihConfig config;
    private SessionManager sessionManager;
    private TokenManager tokenManager;
    private ApiClient apiClient;
    private ActivityTracker activityTracker;
    private SessionDialog sessionDialog;
    
    private ExecutorService executorService;
    private Handler mainHandler;
    
    // Private constructor
    private TapakAsih() {
        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());
    }
    
    /**
     * Get singleton instance of TapakAsih
     * @return TapakAsih instance
     */
    public static TapakAsih getInstance() {
        if (instance == null) {
            instance = new TapakAsih();
        }
        return instance;
    }
    
    /**
     * Initialize TapakAsih SDK
     * @param application Application context
     * @param config Configuration
     */
    public static void initialize(Application application, TapakAsihConfig config) {
        getInstance().init(application, config);
    }
    
    /**
     * Initialize TapakAsih SDK with simplified config
     * @param application Application context
     * @param developerToken Developer token
     */
    public static void initialize(Application application, String developerToken) {
        TapakAsihConfig config = new TapakAsihConfig.Builder(developerToken).build();
        getInstance().init(application, config);
    }
    
    /**
     * Internal initialization
     */
    private void init(Application application, TapakAsihConfig config) {
        if (isInitialized) {
            Log.w(TAG, "TapakAsih is already initialized");
            return;
        }
        
        this.context = application.getApplicationContext();
        this.config = config;
        
        // Initialize managers
        this.sessionManager = new SessionManager(context);
        this.tokenManager = new TokenManager(context);
        
        // Save developer token
        tokenManager.saveDeveloperToken(config.getDeveloperToken());
        
        // Initialize API client
        this.apiClient = new ApiClient(tokenManager, config);
        
        // Initialize activity tracker
        this.activityTracker = new ActivityTracker();
        application.registerActivityLifecycleCallbacks(activityTracker);
        
        // Initialize session dialog
        this.sessionDialog = new SessionDialog(context, sessionManager);
        
        this.isInitialized = true;
        
        Log.i(TAG, "TapakAsih SDK initialized successfully");
        
        // Check if session ID exists, show dialog if not
        checkSessionAndShowDialog();
    }
    
    /**
     * Check if session ID exists and show dialog if not
     */
    public void checkSessionAndShowDialog() {
        if (!isInitialized) {
            Log.w(TAG, "SDK is not initialized");
            return;
        }
        
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                if (!sessionManager.hasSessionId()) {
                    Log.i(TAG, "No session ID found, showing dialog");
                    sessionDialog.show();
                } else {
                    Log.i(TAG, "Session ID found: " + sessionManager.getSessionId());
                }
            }
        });
    }
    
    /**
     * Track a page/activity
     * @param pageName Name of the page or activity
     */
    public static void trackPage(final String pageName) {
        getInstance().track(pageName);
    }
    
    /**
     * Internal track method
     */
    private void track(final String pageName) {
        if (!isInitialized) {
            Log.w(TAG, "SDK is not initialized, cannot track");
            return;
        }
        
        if (pageName == null || pageName.trim().isEmpty()) {
            Log.w(TAG, "Page name cannot be null or empty");
            return;
        }
        
        if (!sessionManager.hasSessionId()) {
            Log.w(TAG, "No session ID, cannot track");
            checkSessionAndShowDialog();
            return;
        }
        
        if (tokenManager.isTokenExpired()) {
            Log.w(TAG, "Developer token is expired, cannot track");
            return;
        }
        
        // Send activity in background thread
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                long epochTime = System.currentTimeMillis() / 1000;
                String sessionId = sessionManager.getSessionId();
                
                ActivityRequest request = new ActivityRequest(epochTime, pageName, sessionId);
                boolean success = apiClient.sendActivity(request);
                
                if (success) {
                    if (config.isEnableDebugLogs()) {
                        Log.i(TAG, "Successfully tracked: " + pageName);
                    }
                } else {
                    if (config.isEnableDebugLogs()) {
                        Log.e(TAG, "Failed to track: " + pageName);
                    }
                }
            }
        });
    }
    
    /**
     * Set session ID manually
     * @param sessionId User's session ID
     */
    public static void setSessionId(String sessionId) {
        getInstance().setSession(sessionId);
    }
    
    /**
     * Internal set session method
     */
    private void setSession(String sessionId) {
        if (!isInitialized) {
            Log.w(TAG, "SDK is not initialized");
            return;
        }
        
        sessionManager.saveSessionId(sessionId);
        Log.i(TAG, "Session ID set: " + sessionId);
    }
    
    /**
     * Get current session ID
     * @return Session ID or null if not set
     */
    public static String getSessionId() {
        return getInstance().getSession();
    }
    
    /**
     * Internal get session method
     */
    private String getSession() {
        if (!isInitialized) {
            Log.w(TAG, "SDK is not initialized");
            return null;
        }
        
        return sessionManager.getSessionId();
    }
    
    /**
     * Check if SDK is initialized
     * @return true if initialized, false otherwise
     */
    public static boolean isInitialized() {
        return getInstance().isInit();
    }
    
    /**
     * Internal is initialized check
     */
    private boolean isInit() {
        return isInitialized;
    }
    
    /**
     * Get configuration
     * @return TapakAsihConfig or null if not initialized
     */
    public static TapakAsihConfig getConfig() {
        return getInstance().config;
    }
    
    /**
     * Check if developer token is expired
     * @return true if token is expired, false otherwise
     */
    public static boolean isTokenExpired() {
        return getInstance().checkTokenExpired();
    }
    
    /**
     * Internal check token expired method
     */
    private boolean checkTokenExpired() {
        if (!isInitialized) {
            Log.w(TAG, "SDK is not initialized");
            return true;
        }
        
        return tokenManager.isTokenExpired();
    }
    
    /**
     * Check if SDK can perform tracking
     * Returns true if SDK is initialized, has developer token, has session ID, and token is not expired
     * @return true if tracking is possible, false otherwise
     */
    public static boolean canTrack() {
        return getInstance().checkCanTrack();
    }
    
    /**
     * Internal check can track method
     */
    private boolean checkCanTrack() {
        if (!isInitialized) {
            Log.w(TAG, "SDK is not initialized");
            return false;
        }
        
        if (!tokenManager.hasDeveloperToken()) {
            Log.w(TAG, "No developer token");
            return false;
        }
        
        if (!sessionManager.hasSessionId()) {
            Log.w(TAG, "No session ID");
            return false;
        }
        
        if (tokenManager.isTokenExpired()) {
            Log.w(TAG, "Token is expired");
            return false;
        }
        
        return true;
    }
    
    /**
     * Show session dialog manually
     */
    public static void showSessionDialog() {
        getInstance().showDialog();
    }
    
    /**
     * Internal show dialog method
     */
    private void showDialog() {
        if (!isInitialized) {
            Log.w(TAG, "SDK is not initialized");
            return;
        }
        
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                sessionDialog.show();
            }
        });
    }
    
    /**
     * Clear session ID
     */
    public static void clearSessionId() {
        getInstance().clearSession();
    }
    
    /**
     * Internal clear session method
     */
    private void clearSession() {
        if (!isInitialized) {
            Log.w(TAG, "SDK is not initialized");
            return;
        }
        
        sessionManager.clearSessionId();
        Log.i(TAG, "Session ID cleared");
    }
    
    /**
     * Destroy SDK and release resources
     */
    public static void destroy() {
        getInstance().cleanup();
    }
    
    /**
     * Internal cleanup method
     */
    private void cleanup() {
        if (!isInitialized) {
            return;
        }
        
        if (context instanceof Application) {
            ((Application) context).unregisterActivityLifecycleCallbacks(activityTracker);
        }
        
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
        
        isInitialized = false;
        Log.i(TAG, "TapakAsih SDK destroyed");
    }
}