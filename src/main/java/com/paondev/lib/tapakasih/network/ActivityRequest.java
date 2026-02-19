package com.paondev.lib.tapakasih.network;

import com.google.gson.annotations.SerializedName;

/**
 * Request model for activity tracking
 */
public class ActivityRequest {
    @SerializedName("epochtime")
    private long epochTime;
    
    @SerializedName("pageName")
    private String pageName;
    
    @SerializedName("sessionId")
    private String sessionId;
    
    public ActivityRequest(long epochTime, String pageName, String sessionId) {
        this.epochTime = epochTime;
        this.pageName = pageName;
        this.sessionId = sessionId;
    }
    
    public long getEpochTime() {
        return epochTime;
    }
    
    public void setEpochTime(long epochTime) {
        this.epochTime = epochTime;
    }
    
    public String getPageName() {
        return pageName;
    }
    
    public void setPageName(String pageName) {
        this.pageName = pageName;
    }
    
    public String getSessionId() {
        return sessionId;
    }
    
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}