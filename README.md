# TapakAsih SDK

Multi-platform activity tracking SDK for Android and Flutter applications.

## Overview

TapakAsih SDK is a powerful activity tracking solution that allows developers to monitor user behavior in their applications. The SDK automatically tracks page views and sends activity data to your API endpoint.

## How It Works

TapakAsih SDK uses a dual-mode system based on server response. The behavior depends on what the API returns during the initial activity check.

### Flow Diagram

```
┌─────────────────────────────────────┐
│  TapakAsih.initialize()         │
│  (Developer Token Set)            │
└─────────────┬───────────────────┘
              │
              ↓
┌─────────────────────────────────────┐
│  Activity Demand Check (API)       │
│  GET /activity/check              │
└─────────────┬───────────────────┘
              │
              ↓
       ┌──────┴──────┐
       │             │
   ON_DEMAND    NO_DEMAND
       │             │
       ↓             ↓
┌──────────────┐ ┌──────────────┐
│ Tracking ON  │ │ Tracking OFF │
│ Session: REQ │ │ Session: OPT │
│ Dialog: SHOW │ │ Dialog: HIDE │
└──────────────┘ └──────────────┘
```

### Two Modes Explained

#### 1. ON_DEMAND Mode (Tracking Required)

**When Server Returns:**

```json
{
  "status": "ON_DEMAND"
}
```

**Behavior:**

- ✅ **Tracking is ENABLED** - All `trackPage()` calls send data to API
- ✅ **Session ID is REQUIRED** - User must provide session ID
- ✅ **Dialog CAN be shown** - You can call `showSessionDialog()`
- ⚠️ **No session ID?** - SDK will notify via listener, but dialog won't auto-show
- 🔔 **Use case** - Server wants to track user activity for analytics

**Code Example:**

```java
// In Application class
TapakAsih.initialize(application, config);

// In Activity (when ON_DEMAND)
if (TapakAsih.needsSessionId()) {
    TapAsih.showSessionDialog();  // Show built-in dialog
}

// After user provides session ID
TapakAsih.trackPage("MainActivity");  // ✓ Will send data to API
```

#### 2. NO_DEMAND Mode (Tracking Disabled)

**When Server Returns:**

```json
{
  "status": "NO_DEMAND"
}
```

**Behavior:**

- ❌ **Tracking is DISABLED** - All `trackPage()` calls are silently ignored
- ❌ **Session ID is OPTIONAL** - User can use app without session ID
- ❌ **Dialog NEVER shows** - Even if you call `showSessionDialog()`
- ⚠️ **No session ID?** - SDK won't notify or show dialog
- 🔔 **Use case** - Server doesn't need tracking data (maintenance, beta test, etc.)

**Code Example:**

```java
// In Application class
TapakAsih.initialize(application, config);

// In Activity (when NO_DEMAND)
if (TapakAsih.needsSessionId()) {
    // Returns FALSE - no need to show dialog
}

TapakAsih.trackPage("MainActivity");  // ✗ Silently ignored
```

### Decision Tree

```
Is SDK Initialized?
    │
    ├── NO ──→ Cannot track, return
    │
    └── YES
        │
        ↓
    Is Tracking Enabled? (from API)
        │
        ├── NO (NO_DEMAND) ──→ Ignore all trackPage() calls
        │                         └─ needsSessionId() returns FALSE
        │
        └── YES (ON_DEMAND)
            │
            ↓
        Has Session ID?
            │
            ├── NO ──→ Notify listener: onSessionRequired()
            │           └─ Developer must show dialog or custom UI
            │
            └── YES ──→ Send tracking data to API
```

### Key Points

1. **Initial Check Happens Once** - During `initialize()`, SDK checks activity demand status
2. **Mode is Cached** - Status is cached for app lifetime (no repeated API calls)
3. **Dialog is Manual** - SDK never auto-shows dialog (prevents crashes)
4. **Listener is Optional** - Use `OnSessionRequiredListener` for custom UI handling
5. **Fallback is ON_DEMAND** - If API check fails, SDK defaults to ON_DEMAND (safer)

## Features

- 🚀 **Automatic Activity Tracking** - Track all user activities automatically
- 🔐 **Dual Authentication** - Developer token + User session ID
- 📱 **Multi-Platform** - Support for Android and Flutter
- 🔄 **Offline Support** - Queue data when offline, send when online
- 🎯 **Easy Integration** - Simple API with minimal setup
- 🔧 **Configurable** - Customize retry attempts, debug logs, etc.

## Architecture

```
User App → TapakAsih SDK → API (api.pycompany.com)
```

## Installation

### Android SDK

#### Option 1: Maven Central (Recommended)

Add dependency to your app's `build.gradle`:

```gradle
dependencies {
    implementation 'com.paondev.lib:tapakasih:1.0.0'
}
```

**Note**: Make sure `mavenCentral()` is included in your repositories:

```gradle
// settings.gradle (for Gradle 7.0+) or build.gradle
repositories {
    google()
    mavenCentral()
}
```

#### Option 2: Local Project

Add dependency to your app's `build.gradle`:

```gradle
dependencies {
    implementation project(':tapakasih')
}
```

#### Option 3: JitPack (Alternative)

Add JitPack repository to your `settings.gradle`:

```gradle
repositories {
    google()
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}
```

Add dependency to your app's `build.gradle`:

```gradle
dependencies {
    implementation 'com.github.paondev:tapakasih:1.0.0'
}
```

### Flutter SDK

#### Option 1: pub.dev (Recommended)

Add plugin to your `pubspec.yaml`:

```yaml
dependencies:
  tapak_asih_flutter: ^1.0.0
```

Then run:

```bash
flutter pub get
```

#### Option 2: GitHub

Add plugin to your `pubspec.yaml`:

```yaml
dependencies:
  tapak_asih_flutter:
    git:
      url: https://github.com/paondev/tapakasih.git
      path: tapak_asih_flutter
```

#### Option 3: Local Path

Add plugin to your `pubspec.yaml`:

```yaml
dependencies:
  tapak_asih_flutter:
    path: ./tapak_asih_flutter
```

## Quick Start

### Android

1. Initialize in your Application class:

```java
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        TapakAsihConfig config = new TapakAsihConfig.Builder("your_developer_token")
            .setEnableDebugLogs(true)
            .build();

        TapakAsih.initialize(this, config);
    }
}
```

2. Add Application class to AndroidManifest.xml:

```xml
<application
    android:name=".MyApplication"
    android:allowBackup="true"
    ... >
    ...
</application>
```

3. Track pages (automatic via lifecycle):

```java
// Automatic tracking is enabled by default
// No additional code needed for basic activity tracking
```

4. Manual tracking (optional):

```java
TapakAsih.trackPage("CustomPageName");
```

5. **Important Notes:**

- ✅ **Developer Token**: Set in code, never exposed to users
- ✅ **Session ID**: User-provided, obtained from your website
- ⚠️ **Dialog**: Only shows if server returns **ON_DEMAND** and no session ID exists
- ⚠️ **NO_DEMAND**: If server returns NO_DEMAND, tracking is disabled, no session needed
- ⚠️ **Never call `showSessionDialog()` in Application class!** It will crash.

### Flutter

1. Initialize in main.dart:

```dart
void main() {
  final config = TapakAsihConfig(
    developerToken: 'your_developer_token',
    enableDebugLogs: true,
  );

  await TapAsih.initialize(config);

  runApp(MyApp());
}
```

2. Track pages:

```dart
// Automatic tracking
await TapAsih.trackPage('HomePage');

// With navigation
Navigator.push(
  context,
  MaterialPageRoute(builder: (context) => SecondPage()),
).then((_) {
  TapAsih.trackPage('SecondPage');
});
```

3. **Important Notes:**

- ✅ **Developer Token**: Set in code, never exposed to users
- ✅ **Session ID**: User-provided, obtained from your website
- ⚠️ **Dialog**: Only shows if server returns **ON_DEMAND** and no session ID exists
- ⚠️ **NO_DEMAND**: If server returns NO_DEMAND, tracking is disabled, no session needed

## Session Management

Session ID is user-specific identifier that links tracking data to individual users. Users obtain their Session ID from your website.

### When is Session ID Required?

Session ID requirement depends on the **Activity Demand Check** result:

| Mode          | Tracking    | Session ID Required | Dialog Behavior    |
| ------------- | ----------- | ------------------- | ------------------ |
| **ON_DEMAND** | ✅ Enabled  | ✅ Yes              | ✅ Can show dialog |
| **NO_DEMAND** | ❌ Disabled | ❌ No               | ❌ Dialog hidden   |

### Built-in Session Dialog

SDK provides a built-in dialog for collecting Session ID. This dialog will **only appear** when:

1. ✅ Server returned **ON_DEMAND**
2. ✅ Session ID doesn't exist yet
3. ✅ You manually call `showSessionDialog()` in an Activity

**⚠️ Important:** Dialog will **NOT** show if:

- Server returned **NO_DEMAND**
- Session ID already exists
- Developer token is expired
- Called in Application class (will crash)

#### Showing the Built-in Dialog

**Android:**

```java
// In your Activity (e.g., MainActivity.onCreate())
if (TapAsih.needsSessionId()) {
    // Only shows if ON_DEMAND and no session ID exists
    TapAsih.showSessionDialog();
}
```

**Flutter:**

```dart
// In your Flutter app
if (await TapAsih.needsSessionId()) {
  // Only shows if ON_DEMAND and no session ID exists
  await TapAsih.showSessionDialog();
}
```

**⚠️ CRITICAL:** Never call `showSessionDialog()` in Application class! It will crash with `BadTokenException`.

### Using Session Listener for Custom UI

If you prefer to build your own UI for Session ID input, use the listener pattern:

**Android:**

```java
// In Application class
TapAsih.setOnSessionRequiredListener(new TapAsih.OnSessionRequiredListener() {
    @Override
    public void onSessionRequired() {
        // Only called when ON_DEMAND and no session ID
        // Launch your custom Activity or show custom UI here
        Intent intent = new Intent(context, CustomSessionActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
});
```

**Flutter:**

```dart
// In your Flutter app
TapAsih.setOnSessionRequiredListener(() {
  // Only called when ON_DEMAND and no session ID
  // Navigate to your custom session screen
  Navigator.pushNamed(context, '/session');
});
```

### Setting Session ID Programmatically

If you have your own authentication system (e.g., login), you can set Session ID directly:

**Android:**

```java
// After user logs in
String userSessionId = getUserSessionFromBackend();
TapAsih.setSessionId(userSessionId);
```

**Flutter:**

```dart
// After user logs in
final userSessionId = await getUserSessionFromBackend();
await TapAsih.setSessionId(userSessionId);
```

### Conditional Session ID Set

Use `setSessionIdIfEmpty()` to set a default Session ID only if user hasn't provided one:

**Android:**

```java
// Set default session ID for anonymous users
boolean wasSet = TapAsih.setSessionIdIfEmpty("anonymous-default-session");
```

**Flutter:**

```dart
// Set default session ID for anonymous users
final wasSet = await TapAsih.setSessionIdIfEmpty('anonymous-default-session');
```

### Session ID Lifecycle

```
App Install
    ↓
[No Session ID]
    ↓
SDK Checks: ON_DEMAND?
    ↓
    ├── YES → Notify listener / Show dialog
    │           ↓
    │       User inputs Session ID
    │           ↓
    │       [Session ID Saved]
    │           ↓
    │       Tracking Enabled
    │
    └── NO → Tracking Disabled (no session needed)
                ↓
            User can use app without session ID
```

## Data Sent to API

The SDK sends the following data to `https://api.pycompany.com/actifity/claim`:

```json
{
  "epochtime": 1234567890,
  "pageName": "MainActivity",
  "sessionId": "user-session-123"
}
```

### Request Headers

- `Content-Type: application/json`
- `Authorization: Bearer {developer_token}`

## Complete Usage Examples

### Example 1: Built-in Dialog (Simplest)

**Use case:** Quick setup with built-in session dialog.

**1. Application Class:**

```java
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize SDK with developer token
        TapakAsihConfig config = new TapakAsihConfig.Builder("your_developer_token")
            .setEnableDebugLogs(true)
            .build();

        TapakAsih.initialize(this, config);
    }
}
```

**2. MainActivity:**

```java
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check if session ID is required (only if ON_DEMAND)
        if (TapakAsih.needsSessionId()) {
            // Show built-in dialog
            // Dialog will only appear if server returned ON_DEMAND
            TapakAsih.showSessionDialog();
        }

        // Track page manually
        TapakAsih.trackPage("MainActivity");
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Track page onResume
        TapakAsih.trackPage("MainActivity");
    }
}
```

**3. AndroidManifest.xml:**

```xml
<application
    android:name=".MyApplication"
    android:allowBackup="true"
    ... >
    ...
</application>
```

### Example 2: Custom UI with Listener

**Use case:** Build your own session input UI with Jetpack Compose.

**1. Application Class:**

```java
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        TapakAsihConfig config = new TapakAsihConfig.Builder("your_developer_token")
            .setEnableDebugLogs(true)
            .build();

        TapakAsih.initialize(this, config);

        // Set listener for custom UI handling
        TapakAsih.setOnSessionRequiredListener(new TapakAsih.OnSessionRequiredListener() {
            @Override
            public void onSessionRequired() {
                // Called when ON_DEMAND and no session ID exists
                // You can send event to Activity or show custom UI here
                // Example: Launch custom session screen
                Intent intent = new Intent(getApplicationContext(), SessionInputActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
}
```

**2. SessionInputActivity (Kotlin with Jetpack Compose):**

```kotlin
class SessionInputActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SessionInputScreen(
                onSessionSubmitted = { sessionId ->
                    // Save session ID to SDK
                    TapakAsih.setSessionId(sessionId)

                    // Navigate to main app
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            )
        }
    }
}

@Composable
fun SessionInputScreen(
    onSessionSubmitted: (String) -> Unit
) {
    var sessionId by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Masukkan Session ID",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Silakan masukkan Session ID dari website kami",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = sessionId,
                onValueChange = {
                    sessionId = it
                    showError = false
                },
                label = { Text("Session ID") },
                isError = showError,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                supportingText = if (showError) {
                    { Text("Session ID tidak boleh kosong") }
                } else null
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (sessionId.isNotBlank()) {
                        onSessionSubmitted(sessionId.trim())
                    } else {
                        showError = true
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = sessionId.isNotBlank()
            ) {
                Text("Simpan")
            }
        }
    }
}
```

**3. MainActivity:**

```java
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // No need to check needsSessionId() here
        // Custom UI is handled by listener in Application class

        // Track page
        TapakAsih.trackPage("MainActivity");
    }
}
```

### Example 3: Manual Session ID from Login

**Use case:** Your app has its own authentication system.

**1. Application Class:**

```java
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        TapakAsihConfig config = new TapakAsihConfig.Builder("your_developer_token")
            .setEnableDebugLogs(true)
            .build();

        TapakAsih.initialize(this, config);

        // Check if user is already logged in
        String userSessionId = getUserSessionFromStorage();
        if (userSessionId != null) {
            // Set session ID from your auth system
            TapakAsih.setSessionId(userSessionId);
        }
    }

    private String getUserSessionFromStorage() {
        SharedPreferences prefs = getSharedPreferences("AuthPrefs", MODE_PRIVATE);
        return prefs.getString("user_session_id", null);
    }
}
```

**2. LoginActivity:**

```java
public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    private void onLoginSuccess(String userSessionId) {
        // Save to your auth system
        saveUserSession(userSessionId);

        // Set to TapakAsih SDK
        TapakAsih.setSessionId(userSessionId);

        // Navigate to main app
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void saveUserSession(String sessionId) {
        SharedPreferences prefs = getSharedPreferences("AuthPrefs", MODE_PRIVATE);
        prefs.edit().putString("user_session_id", sessionId).apply();
    }
}
```

**3. MainActivity:**

```java
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Session ID is already set from login system
        // No need to show dialog

        // Track page
        TapakAsih.trackPage("MainActivity");
    }
}
```

**4. Logout (Optional):**

```java
public void logout() {
    // Clear your auth system
    SharedPreferences prefs = getSharedPreferences("AuthPrefs", MODE_PRIVATE);
    prefs.edit().remove("user_session_id").apply();

    // Clear TapakAsih session
    TapakAsih.clearSessionId();

    // Navigate to login
    startActivity(new Intent(this, LoginActivity.class));
    finish();
}
```

## Custom Session UI

If you prefer to build your own UI for session ID input instead of using the built-in dialog, here are some examples:

### Jetpack Compose Example

```kotlin
@Composable
fun SessionInputDialog(
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var sessionId by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Session ID Required") },
        text = {
            Column {
                Text(
                    "Please enter your Session ID from our website to continue.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = sessionId,
                    onValueChange = {
                        sessionId = it
                        isError = false
                    },
                    label = { Text("Session ID") },
                    isError = isError,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    supportingText = if (isError) {
                        { Text("Session ID cannot be empty") }
                    } else null
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (sessionId.isBlank()) {
                        isError = true
                    } else {
                        onSave(sessionId.trim())
                    }
                },
                enabled = sessionId.isNotBlank()
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

// Usage in your Activity
setContent {
    EMoneyCheckerTheme {
        if (TapakAsih.needsSessionId()) {
            SessionInputDialog(
                onDismiss = { /* Handle dismiss */ },
                onSave = { sessionId ->
                    TapakAsih.setSessionId(sessionId)
                    // Navigate to main app
                }
            )
        }
    }
}
```

### Traditional Android (XML) Example

```xml
<!-- res/layout/dialog_session_input.xml -->
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical"
    android:padding="24dp">

    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Enter Session ID"
        android:textSize="18sp"
        android:textStyle="bold" />

    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="8dp"
        android:text="Please enter your Session ID from our website" />

    <EditText
        android:id="@+id/sessionIdInput"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="16dp"
        android:hint="Session ID"
        android:inputType="text" />

    <Button
        android:id="@+id/saveButton"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="16dp"
        android:text="Save" />

</LinearLayout>
```

```java
// In your Activity
public void showCustomSessionDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("Session ID Required");

    // Inflate custom layout
    View dialogView = getLayoutInflater().inflate(R.layout.dialog_session_input, null);
    EditText sessionIdInput = dialogView.findViewById(R.id.sessionIdInput);
    Button saveButton = dialogView.findViewById(R.id.saveButton);

    builder.setView(dialogView);

    // Create dialog
    AlertDialog dialog = builder.create();

    saveButton.setOnClickListener(v -> {
        String sessionId = sessionIdInput.getText().toString().trim();
        if (sessionId.isEmpty()) {
            Toast.makeText(this, "Session ID cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save to SDK
        TapakAsih.setSessionId(sessionId);

        // Dismiss dialog
        dialog.dismiss();

        // Navigate to main app
        startActivity(new Intent(this, MainActivity.class));
        finish();
    });

    dialog.show();
}
```

## Configuration Options

### Android

```java
TapakAsihConfig config = new TapakAsihConfig.Builder("developer_token")
    .setEnableDebugLogs(true)      // Enable debug logging
    .setEnableOfflineQueue(true)     // Enable offline queue
    .setRetryAttempts(3)            // Number of retry attempts
    .build();
```

### Flutter

```dart
final config = TapakAsihConfig(
  developerToken: 'your_token',
  enableDebugLogs: true,
  enableOfflineQueue: true,
  retryAttempts: 3,
);
```

## API Methods

### Android

```java
// Initialize SDK
TapAsih.initialize(application, config);

// Check if session ID is needed
if (TapAsih.needsSessionId()) {
    TapAsih.showSessionDialog();
}

// Track page
TapAsih.trackPage("PageName");

// Set session ID
TapAsih.setSessionId("session-id");

// Set session ID only if not already set
boolean wasSet = TapAsih.setSessionIdIfEmpty("default-session-id");

// Get session ID
String sessionId = TapAsih.getSessionId();

// Check if initialized
boolean initialized = TapAsih.isInitialized();

// Show session dialog (call this in Activity, not Application)
TapAsih.showSessionDialog();

// Set session listener
TapAsih.setOnSessionRequiredListener(listener);

// Clear session ID
TapAsih.clearSessionId();

// Destroy SDK
TapAsih.destroy();
```

### Flutter

```dart
// Initialize SDK
await TapAsih.initialize(config);

// Check if session ID is needed
if (await TapAsih.needsSessionId()) {
  await TapAsih.showSessionDialog();
}

// Track page
await TapAsih.trackPage('PageName');

// Set session ID
await TapAsih.setSessionId('session-id');

// Set session ID only if not already set
final wasSet = await TapAsih.setSessionIdIfEmpty('default-session-id');

// Get session ID
final sessionId = await TapAsih.getSessionId();

// Check if initialized
final initialized = await TapAsih.isInitialized();

// Show session dialog
await TapAsih.showSessionDialog();

// Set session listener
TapAsih.setOnSessionRequiredListener(() {
  // Handle session requirement
});

// Clear session ID
await TapAsih.clearSessionId();

// Destroy SDK
await TapAsih.destroy();
```

## Token Management

The SDK automatically handles token validation:

- If the developer token is valid: Data is sent to API
- If the developer token is expired (HTTP 401/403): Data is not sent
- When you update the developer token: SDK automatically resumes sending data

**No manual intervention required** - the SDK handles everything automatically.

### Activity Demand Check

On first launch, the SDK automatically checks the activity demand status from the server:

**Endpoint**: `GET https://api.pycompany.com/activity/check`

**Response**:

```json
{
  "status": "ON_DEMAND" | "NO_DEMAND"
}
```

**Behavior**:

- **ON_DEMAND**: Session ID is required for tracking
  - SDK will notify via listener if session ID doesn't exist
  - Activity tracking is enabled
  - You must show session dialog manually (e.g., in your Activity)
  - User must provide session ID to use tracking features
- **NO_DEMAND**: Session ID is NOT required
  - SDK will NOT notify or show session dialog
  - Activity tracking is disabled
  - User can use the app without session ID
  - All `trackPage()` calls are silently ignored
  - `needsSessionId()` will return false

This check happens once during initialization. If the check fails, the SDK defaults to ON_DEMAND (safer option).

## Error Handling

The SDK handles errors gracefully:

### Network Errors

- **Behavior**: Automatic retry with exponential backoff
- **Impact**: No user action needed, SDK will retry automatically
- **Configurable**: Retry attempts can be configured via `setRetryAttempts()`

### Expired Developer Token

- **Behavior**: SDK automatically stops sending data
- **Impact**: All `trackPage()` calls are silently ignored
- **Solution**: Update developer token in your app's code, SDK will resume automatically
- **Note**: Session ID is NOT required when token is expired

### Missing Session ID

**Depends on Mode:**

- **ON_DEMAND Mode**:
  - SDK notifies via `OnSessionRequiredListener` if set
  - You must manually call `showSessionDialog()` or show custom UI
  - All `trackPage()` calls fail until session ID is provided
  - **Important**: Dialog does NOT auto-show (prevents crashes)

- **NO_DEMAND Mode**:
  - No notification, no dialog
  - Tracking is disabled anyway, session ID is not needed
  - App works normally without session ID

### Offline Mode

- **Behavior**: Queues tracking data for later transmission
- **Impact**: No data loss, all queued data will be sent when online
- **Solution**: No action needed, SDK handles automatically

### Activity Demand Check Failure

- **Behavior**: Defaults to ON_DEMAND mode (safer option)
- **Impact**: Tracking will be enabled even if check fails
- **Solution**: Check network connectivity, ensure API endpoint is accessible

## Requirements

### Android

- **Min SDK**: 21 (Android 5.0 Lollipop)
- **Compile SDK**: 36
- **Java Version**: 11

### Flutter

- **Flutter**: 3.0+
- **Dart**: 2.17+
- **Android**: 5.0+ (API 21+)

## Dependencies

### Android

- OkHttp 4.12.0
- Gson 2.10.1
- AndroidX AppCompat

### Flutter

- TapakAsih Android SDK (included)

## Troubleshooting

### App crashes on initialization with BadTokenException

**Cause**: Calling `showSessionDialog()` in Application class instead of Activity.

**Solution**: Only call `TapAsih.showSessionDialog()` in your Activity (e.g., in `onCreate()` or `onResume()`). Never call it in Application class.

### SDK not tracking activities

**Check the following in order:**

1. ✅ **Is SDK initialized?**

   ```java
   if (!TapAsih.isInitialized()) {
       // SDK not initialized
   }
   ```

2. ✅ **Is tracking enabled?** (Server returned ON_DEMAND?)
   - If server returned **NO_DEMAND**, tracking is disabled by design
   - Check debug logs: Look for "Tracking is disabled by server" or "Tracking is enabled by server"
   - This is expected behavior when server doesn't need data

3. ✅ **Is developer token valid?**
   - Check if token is expired
   - Verify token matches your website's dashboard
   - If expired, update token in your app's code

4. ✅ **Does user have Session ID?**

   ```java
   if (TapAsih.needsSessionId()) {
       // No session ID - user must provide it
   }
   ```

   - If ON_DEMAND mode: Session ID is REQUIRED
   - If NO_DEMAND mode: Session ID is NOT required

5. ✅ **Check debug logs**
   - Enable debug logs: `.setEnableDebugLogs(true)`
   - Look for error messages
   - Check if "Failed to track" appears in logs

### Data not being sent

**Check the following:**

1. ✅ **Verify developer token is not expired**
   - Check your website's dashboard
   - If expired, update token in your app's code (SDK will automatically resume)

2. ✅ **Ensure network connectivity**
   - Device must be online to send data
   - Check if device has internet access
   - Offline data is queued and sent when online

3. ✅ **Check if tracking is enabled**
   - If server returned **NO_DEMAND**, data won't be sent (by design)
   - Check debug logs for tracking status

4. ✅ **Check debug logs for API errors**
   - Look for HTTP errors (401, 403, 500, etc.)
   - Check if "Failed to track" appears in logs
   - Verify API endpoint is accessible

### Session dialog not showing

**Possible causes and solutions:**

1. ✅ **Called in Application class?**
   - ❌ **CRITICAL ERROR**: Never call `showSessionDialog()` in Application class
   - ✅ Only call in Activity (e.g., `onCreate()`, `onResume()`)

2. ✅ **Session ID already exists?**

   ```java
   if (!TapAsih.needsSessionId()) {
       // Session ID exists - no need to show dialog
   }
   ```

3. ✅ **Server returned NO_DEMAND?**
   - If server returned **NO_DEMAND**, dialog will never show (by design)
   - This is expected behavior when tracking is disabled
   - Check debug logs for "Tracking is disabled by server"

4. ✅ **Developer token expired?**
   - If token is expired, dialog won't show (no point collecting session ID)
   - Update token in your app's code

5. ✅ **SDK not initialized?**

   ```java
   if (!TapAsih.isInitialized()) {
       // Initialize SDK first
   }
   ```

6. ✅ **Activity in invalid state?**
   - Activity might be finishing or destroyed
   - Check if Activity is still running
   - Try calling dialog in `onResume()` instead of `onCreate()`

### Understanding ON_DEMAND vs NO_DEMAND

**If you're confused why tracking works sometimes but not others:**

1. **Check debug logs for tracking status:**
   - "Tracking is enabled by server (ON_DEMAND)" → Session ID required
   - "Tracking is disabled by server (NO_DEMAND)" → No session needed

2. **Why does server return NO_DEMAND?**
   - Server doesn't need tracking data right now
   - Could be: maintenance mode, beta testing, etc.
   - This is normal and expected

3. **What to do in each mode:**

   **ON_DEMAND:**
   - ✅ Show session dialog or custom UI
   - ✅ Collect session ID from user
   - ✅ Tracking will work after session ID is provided
   - ✅ All `trackPage()` calls send data to API

   **NO_DEMAND:**
   - ❌ Don't show session dialog
   - ❌ Don't collect session ID
   - ❌ Tracking is disabled (all `trackPage()` calls ignored)
   - ✅ App works normally without session ID

4. **How to verify current mode:**

   ```java
   // Check if session ID is needed
   boolean needsSession = TapAsih.needsSessionId();

   if (needsSession) {
       // Server returned ON_DEMAND
       // Show dialog or custom UI
   } else {
       // Server returned NO_DEMAND
       // Don't show dialog
   }
   ```

### Dialog shows but tracking doesn't work

**Check the following:**

1. ✅ **Did user provide session ID?**
   - Check if session ID was saved
   - `TapAsih.getSessionId()` should return non-null

2. ✅ **Is tracking enabled?**
   - Check debug logs for tracking status
   - If NO_DEMAND, tracking won't work (by design)

3. ✅ **Is developer token valid?**
   - Check if token is expired
   - If expired, update in app code

4. ✅ **Check debug logs**
   - Look for "Successfully tracked" or "Failed to track"
   - Check for API errors (401, 403, 500, etc.)

### How to test if SDK is working correctly

1. **Enable debug logs:**

   ```java
   .setEnableDebugLogs(true)
   ```

2. **Check initialization:**

   ```java
   Log.d("TapakAsih", "Initialized: " + TapAsih.isInitialized());
   ```

3. **Check tracking status:**
   - Look for "Tracking is enabled by server" or "Tracking is disabled by server"
   - This tells you which mode SDK is in

4. **Check session status:**

   ```java
   Log.d("TapakAsih", "Needs Session: " + TapAsih.needsSessionId());
   Log.d("TapakAsih", "Session ID: " + TapAsih.getSessionId());
   ```

5. **Test tracking:**
   ```java
   TapAsih.trackPage("TestPage");
   ```

   - Check logs for "Successfully tracked: TestPage"
   - If "Failed to track", check why (no session ID, tracking disabled, etc.)

## License

Copyright © 2025 PaonDev. All rights reserved.

## Support

For issues and questions, please contact: [support@paondev.com]

## Changelog

### Version 1.0.0

- Initial release
- Android SDK support
- Flutter SDK support
- Automatic activity tracking
- Session management
- Offline support
