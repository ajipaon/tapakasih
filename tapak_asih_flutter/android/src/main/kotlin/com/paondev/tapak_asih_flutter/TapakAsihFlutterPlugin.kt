package com.paondev.tapak_asih_flutter

import android.app.Application
import android.content.Context
import android.util.Log
import com.paondev.lib.tapakasih.TapakAsih
import com.paondev.lib.tapakasih.config.TapakAsihConfig
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

/** TapakAsihFlutterPlugin */
class TapakAsihFlutterPlugin : FlutterPlugin, MethodCallHandler {
    private val TAG = "TapakAsihFlutterPlugin"
    
    private lateinit var channel: MethodChannel
    private var applicationContext: Context? = null

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "tapak_asih")
        channel.setMethodCallHandler(this)
        applicationContext = flutterPluginBinding.applicationContext
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        when (call.method) {
            "initialize" -> initialize(call, result)
            "trackPage" -> trackPage(call, result)
            "setSessionId" -> setSessionId(call, result)
            "getSessionId" -> getSessionId(result)
            "isInitialized" -> isInitialized(result)
            "showSessionDialog" -> showSessionDialog(result)
            "clearSessionId" -> clearSessionId(result)
            "destroy" -> destroy(result)
            else -> result.notImplemented()
        }
    }

    private fun initialize(call: MethodCall, result: Result) {
        try {
            val args = call.arguments as? Map<String, Any>
            val developerToken = args?.get("developerToken") as? String
            val enableDebugLogs = args?.get("enableDebugLogs") as? Boolean ?: false
            val enableOfflineQueue = args?.get("enableOfflineQueue") as? Boolean ?: true
            val retryAttempts = args?.get("retryAttempts") as? Int ?: 3

            if (developerToken == null) {
                result.error("INVALID_ARGUMENT", "Developer token is required", null)
                return
            }

            val context = applicationContext
            if (context !is Application) {
                result.error("INVALID_CONTEXT", "Application context is required", null)
                return
            }

            val config = TapakAsihConfig.Builder(developerToken)
                .setEnableDebugLogs(enableDebugLogs)
                .setEnableOfflineQueue(enableOfflineQueue)
                .setRetryAttempts(retryAttempts)
                .build()

            TapakAsih.initialize(context, config)
            result.success(null)
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing TapakAsih", e)
            result.error("INIT_ERROR", e.message, null)
        }
    }

    private fun trackPage(call: MethodCall, result: Result) {
        try {
            val args = call.arguments as? Map<String, Any>
            val pageName = args?.get("pageName") as? String

            if (pageName == null) {
                result.error("INVALID_ARGUMENT", "Page name is required", null)
                return
            }

            TapakAsih.trackPage(pageName)
            result.success(null)
        } catch (e: Exception) {
            Log.e(TAG, "Error tracking page", e)
            result.error("TRACK_ERROR", e.message, null)
        }
    }

    private fun setSessionId(call: MethodCall, result: Result) {
        try {
            val args = call.arguments as? Map<String, Any>
            val sessionId = args?.get("sessionId") as? String

            if (sessionId == null) {
                result.error("INVALID_ARGUMENT", "Session ID is required", null)
                return
            }

            TapakAsih.setSessionId(sessionId)
            result.success(null)
        } catch (e: Exception) {
            Log.e(TAG, "Error setting session ID", e)
            result.error("SET_SESSION_ERROR", e.message, null)
        }
    }

    private fun getSessionId(result: Result) {
        try {
            val sessionId = TapakAsih.getSessionId()
            result.success(sessionId)
        } catch (e: Exception) {
            Log.e(TAG, "Error getting session ID", e)
            result.error("GET_SESSION_ERROR", e.message, null)
        }
    }

    private fun isInitialized(result: Result) {
        try {
            val initialized = TapakAsih.isInitialized()
            result.success(initialized)
        } catch (e: Exception) {
            Log.e(TAG, "Error checking initialization", e)
            result.error("CHECK_INIT_ERROR", e.message, null)
        }
    }

    private fun showSessionDialog(result: Result) {
        try {
            TapakAsih.showSessionDialog()
            result.success(null)
        } catch (e: Exception) {
            Log.e(TAG, "Error showing session dialog", e)
            result.error("SHOW_DIALOG_ERROR", e.message, null)
        }
    }

    private fun clearSessionId(result: Result) {
        try {
            TapakAsih.clearSessionId()
            result.success(null)
        } catch (e: Exception) {
            Log.e(TAG, "Error clearing session ID", e)
            result.error("CLEAR_SESSION_ERROR", e.message, null)
        }
    }

    private fun destroy(result: Result) {
        try {
            TapakAsih.destroy()
            result.success(null)
        } catch (e: Exception) {
            Log.e(TAG, "Error destroying SDK", e)
            result.error("DESTROY_ERROR", e.message, null)
        }
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }
}