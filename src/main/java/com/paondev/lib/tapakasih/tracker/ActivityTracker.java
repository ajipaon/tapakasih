package com.paondev.lib.tapakasih.tracker;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import com.paondev.lib.tapakasih.TapakAsih;

/**
 * Activity lifecycle tracker to automatically track screen changes
 */
public class ActivityTracker implements Application.ActivityLifecycleCallbacks {
    private static final String TAG = "ActivityTracker";
    
    private String currentActivityName;
    
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        // Not used
    }
    
    @Override
    public void onActivityStarted(Activity activity) {
        // Not used
    }
    
    @Override
    public void onActivityResumed(Activity activity) {
        String activityName = activity.getClass().getSimpleName();
        trackActivity(activityName);
    }
    
    @Override
    public void onActivityPaused(Activity activity) {
        // Not used
    }
    
    @Override
    public void onActivityStopped(Activity activity) {
        // Not used
    }
    
    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        // Not used
    }
    
    @Override
    public void onActivityDestroyed(Activity activity) {
        // Not used
    }
    
    /**
     * Track activity screen change
     * @param activityName Name of the activity
     */
    private void trackActivity(String activityName) {
        // Avoid tracking same activity multiple times
        if (activityName.equals(currentActivityName)) {
            return;
        }
        
        currentActivityName = activityName;
        
        Log.d(TAG, "Tracking activity: " + activityName);
        
        // Track the activity using TapakAsih SDK
        TapakAsih.trackPage(activityName);
    }
    
    /**
     * Get current activity name
     * @return Current activity name
     */
    public String getCurrentActivityName() {
        return currentActivityName;
    }
}