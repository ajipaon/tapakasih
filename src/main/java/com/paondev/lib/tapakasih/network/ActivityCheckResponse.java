package com.paondev.lib.tapakasih.network;

import com.google.gson.annotations.SerializedName;

/**
 * Response model for activity check endpoint
 * {
 *   "status": "ON_DEMAND" | "NO_DEMAND"
 * }
 */
public class ActivityCheckResponse {
    
    @SerializedName("status")
    private String status;
    
    public ActivityCheckResponse() {}
    
    public ActivityCheckResponse(String status) {
        this.status = status;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    /**
     * Check if tracking is required (ON_DEMAND)
     * @return true if ON_DEMAND, false if NO_DEMAND
     */
    public boolean isTrackingRequired() {
        return "ON_DEMAND".equalsIgnoreCase(status);
    }
    
    /**
     * Check if tracking is not required (NO_DEMAND)
     * @return true if NO_DEMAND, false if ON_DEMAND
     */
    public boolean isTrackingDisabled() {
        return "NO_DEMAND".equalsIgnoreCase(status);
    }
}