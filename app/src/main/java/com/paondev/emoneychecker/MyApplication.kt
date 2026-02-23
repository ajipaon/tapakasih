package com.paondev.emoneychecker

import android.app.Application
import com.paondev.lib.tapakasih.TapakAsih
import com.paondev.lib.tapakasih.config.TapakAsihConfig

/**
 * Application class for EMoneyChecker
 * Handles SDK initialization and app-wide setup
 */
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize TapakAsih SDK
        val config = TapakAsihConfig.Builder(
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0aWNrZXRfaWQiOiJhZDMwODBlZC05MDFkLTRhOTQtOTIzOC1kNzFjNGZmNTQ4ZjYiLCJ0aWNrZXRfbmFtZSI6ImVtb25leSBjaGVja2VyIiwicGFja2FnZV9uYW1lIjoiY29tLnBhb25kZXYuZW1vbmV5Y2hlY2tlciIsInVzZXJfaWQiOiI1OTExMDA0Yy01YzVjLTQ5NGQtYjY1NS1kZDBlNzgxZGZjYjgiLCJyZXF1ZXN0ZWRfYnkiOiJhamlzZXRpYXdhbjg4M0BnbWFpbC5jb20iLCJpYXQiOjE3NzE4MjY4NzgsImV4cCI6MTgwMzM2Mjg3OH0.QGf_n8o61GxhYMFJL_fIyBP5xyDfnmG3nH9p6goPXgU"
        )
            .setEnableDebugLogs(true)
            .build()

        TapakAsih.initialize(this, config)
    }
}