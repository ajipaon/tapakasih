# Migration Guide for TapakAsih SDK v1.0.0+

## Breaking Change: Session Dialog is No Longer Automatic

### What Changed?

In previous versions, the SDK would automatically show the session dialog when initialized. This caused crashes (`BadTokenException`) when the SDK was initialized in the Application class.

**New Behavior**: The SDK will NOT automatically show the session dialog. You must manually call `showSessionDialog()` in your Activity.

### Why This Change?

The automatic dialog was causing crashes because:

- Dialogs require an Activity context with a valid window token
- Application context does not have a valid window token
- Showing dialogs in Application class is against Android best practices

## How to Migrate

### Before (Old Code - Will Crash)

```kotlin
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val config = TapakAsihConfig.Builder("YOUR_TOKEN")
            .setEnableDebugLogs(true)
            .build()

        TapakAsih.initialize(this, config)

        // ❌ DON'T DO THIS - Will cause BadTokenException
        // Session dialog was shown automatically
    }
}
```

### After (New Code - Safe)

```kotlin
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val config = TapakAsihConfig.Builder("YOUR_TOKEN")
            .setEnableDebugLogs(true)
            .build()

        TapAsih.initialize(this, config)

        // ✅ Safe - No automatic dialog
    }
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ✅ Check if session ID is needed and show dialog
        if (TapakAsih.needsSessionId()) {
            TapAsih.showSessionDialog()
        }
    }
}
```

## Migration Options

### Option 1: Check in Activity (Recommended)

Check if session ID is needed in your Activity and show dialog:

```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (TapakAsih.needsSessionId()) {
            TapAsAsih.showSessionDialog()
        }
    }
}
```

### Option 2: Use Listener

Set up a listener to be notified when session ID is required:

```kotlin
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val config = TapakAsihConfig.Builder("YOUR_TOKEN")
            .setEnableDebugLogs(true)
            .build()

        TapakAsih.initialize(this, config)

        // Set listener
        TapakAsih.setOnSessionRequiredListener(object : TapAsih.OnSessionRequiredListener {
            override fun onSessionRequired() {
                // You can:
                // 1. Save a flag to show dialog later
                // 2. Send event using EventBus/RxBus
                // 3. Use LiveData/Flow to notify Activity
                Log.d("TapakAsih", "Session ID is required")
            }
        })
    }
}
```

### Option 3: Set Session ID Programmatically

If your app already has a session ID (e.g., from login):

```kotlin
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val config = TapakAsihConfig.Builder("YOUR_TOKEN")
            .setEnableDebugLogs(true)
            .build()

        TapakAsih.initialize(this, config)

        // Set session ID from your auth system
        val userSessionId = getUserSessionId()
        if (userSessionId != null) {
            TapakAsih.setSessionId(userSessionId)
        }
    }

    private fun getUserSessionId(): String? {
        // Get session ID from your auth/storage
        val prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        return prefs.getString("session_id", null)
    }
}
```

## New API Methods

### needsSessionId()

Check if the SDK needs a session ID:

```kotlin
if (TapakAsih.needsSessionId()) {
    // Session ID is required
    TapakAsih.showSessionDialog()
}
```

### setOnSessionRequiredListener()

Set a listener to be notified when session ID is required:

```kotlin
TapakAsih.setOnSessionRequiredListener(object : TapAsih.OnSessionRequiredListener {
    override fun onSessionRequired() {
        // Handle session requirement
    }
})
```

### setSessionIdIfEmpty()

Set session ID only if it's not already set:

```kotlin
val wasSet = TapakAsih.setSessionIdIfEmpty("default-session-id")
if (wasSet) {
    Log.d("TapakAsih", "Session ID was set")
}
```

## Common Migration Scenarios

### Scenario 1: Simple App with Login

```kotlin
// MyApplication.kt
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val config = TapakAsihConfig.Builder("YOUR_TOKEN")
            .setEnableDebugLogs(BuildConfig.DEBUG)
            .build()

        TapakAsih.initialize(this, config)

        // Check if user is already logged in
        val sessionFromStorage = getSessionFromStorage()
        if (sessionFromStorage != null) {
            TapakAsih.setSessionId(sessionFromStorage)
        }
    }
}

// LoginActivity.kt
class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginButton.setOnClickListener {
            performLogin { sessionId ->
                saveSession(sessionId)
                TapakAsih.setSessionId(sessionId)
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
    }
}

// MainActivity.kt
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Check if session is needed (shouldn't be if user logged in)
        if (TapakAsih.needsSessionId()) {
            TapakAsih.showSessionDialog()
        }
    }
}
```

### Scenario 2: App Without Login (Manual Session Input)

```kotlin
// MyApplication.kt
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val config = TapakAsihConfig.Builder("YOUR_TOKEN")
            .setEnableDebugLogs(BuildConfig.DEBUG)
            .build()

        TapakAsih.initialize(this, config)
        // No session ID yet
    }
}

// MainActivity.kt
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Show dialog if session ID is needed
        if (TapakAsih.needsSessionId()) {
            TapakAsih.showSessionDialog()
        }
    }

    override fun onResume() {
        super.onResume()
        // Track page
        TapakAsih.trackPage("MainActivity")
    }
}
```

### Scenario 3: Using EventBus for Notification

```kotlin
// MyApplication.kt
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val config = TapakAsihConfig.Builder("YOUR_TOKEN")
            .setEnableDebugLogs(BuildConfig.DEBUG)
            .build()

        TapakAsih.initialize(this, config)

        TapakAsih.setOnSessionRequiredListener(object : TapAsih.OnSessionRequiredListener {
            override fun onSessionRequired() {
                // Post event using EventBus
                EventBus.getDefault().post(SessionRequiredEvent())
            }
        })
    }
}

// MainActivity.kt
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Subscribe to events
        EventBus.getDefault().register(this)
    }

    @Subscribe
    fun onSessionRequired(event: SessionRequiredEvent) {
        // Show dialog when event is received
        TapakAsih.showSessionDialog()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}
```

## Testing Your Migration

1. **First Install**: App should not crash on first install
2. **Session Dialog**: Dialog should appear when you call `showSessionDialog()` in Activity
3. **Tracking**: After setting session ID, `trackPage()` should work
4. **Persistence**: Session ID should persist across app restarts
5. **No Auto-Dialog**: App should not show dialog automatically on startup

## Troubleshooting

### App Still Crashes

**Problem**: App crashes with `BadTokenException`

**Solution**: Make sure you're NOT calling `showSessionDialog()` in Application class. Only call it in Activity.

### Dialog Not Showing

**Problem**: Session dialog doesn't appear

**Solution**:

1. Check if `TapakAsih.needsSessionId()` returns `true`
2. Ensure you're calling `showSessionDialog()` in an Activity
3. Verify Activity is not finishing/destroyed
4. Check debug logs

### Tracking Not Working

**Problem**: `trackPage()` calls don't send data

**Solution**:

1. Verify session ID is set: `TapakAsih.getSessionId()`
2. Check if developer token is valid
3. Enable debug logs and check for errors
4. Verify server returns `ON_DEMAND` status

## Summary

✅ **Do's**:

- Initialize SDK in Application class
- Call `showSessionDialog()` in Activity
- Use `needsSessionId()` to check before showing dialog
- Set listener for custom handling
- Use `setSessionIdIfEmpty()` if you have default session

❌ **Don'ts**:

- Call `showSessionDialog()` in Application class
- Assume dialog will show automatically
- Forget to check `needsSessionId()` before showing dialog
- Show dialog in Fragment without Activity context

## Need Help?

If you need assistance with migration, please contact: [support@paondev.com]
