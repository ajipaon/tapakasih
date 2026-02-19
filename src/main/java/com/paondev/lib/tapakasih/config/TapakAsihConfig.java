package com.paondev.lib.tapakasih.config;

/**
 * Configuration class for TapakAsih SDK
 */
public class TapakAsihConfig {
    private String developerToken;
    private boolean enableDebugLogs;
    private boolean enableOfflineQueue;
    private int retryAttempts;
    
    private TapakAsihConfig(Builder builder) {
        this.developerToken = builder.developerToken;
        this.enableDebugLogs = builder.enableDebugLogs;
        this.enableOfflineQueue = builder.enableOfflineQueue;
        this.retryAttempts = builder.retryAttempts;
    }
    
    public String getDeveloperToken() {
        return developerToken;
    }
    
    public boolean isEnableDebugLogs() {
        return enableDebugLogs;
    }
    
    public boolean isEnableOfflineQueue() {
        return enableOfflineQueue;
    }
    
    public int getRetryAttempts() {
        return retryAttempts;
    }
    
    /**
     * Builder for TapakAsihConfig
     */
    public static class Builder {
        private String developerToken;
        private boolean enableDebugLogs = false;
        private boolean enableOfflineQueue = true;
        private int retryAttempts = Constants.MAX_RETRY_ATTEMPTS;
        
        public Builder(String developerToken) {
            if (developerToken == null || developerToken.trim().isEmpty()) {
                throw new IllegalArgumentException("Developer token cannot be null or empty");
            }
            this.developerToken = developerToken;
        }
        
        public Builder setEnableDebugLogs(boolean enableDebugLogs) {
            this.enableDebugLogs = enableDebugLogs;
            return this;
        }
        
        public Builder setEnableOfflineQueue(boolean enableOfflineQueue) {
            this.enableOfflineQueue = enableOfflineQueue;
            return this;
        }
        
        public Builder setRetryAttempts(int retryAttempts) {
            this.retryAttempts = retryAttempts;
            return this;
        }
        
        public TapakAsihConfig build() {
            return new TapakAsihConfig(this);
        }
    }
}