# TapakAsih SDK

Multi-platform activity tracking SDK for Android and Flutter applications.

## Overview

TapakAsih SDK is a powerful activity tracking solution that allows developers to monitor user behavior in their applications. The SDK automatically tracks page views and sends activity data to your API endpoint.

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

2. Track pages (automatic via lifecycle):

```java
// Automatic tracking is enabled by default
// No additional code needed for basic activity tracking
```

3. Manual tracking (optional):

```java
TapakAsih.trackPage("CustomPageName");
```

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

## Session Management

When a user installs your app, they need to provide a Session ID obtained from your website.

### Session ID Input

The SDK will automatically show a dialog asking for the Session ID if:

- User has not provided a Session ID
- Session ID has been cleared

You can also manually show the dialog:

**Android:**

```java
TapAsih.showSessionDialog();
```

**Flutter:**

```dart
await TapAsih.showSessionDialog();
```

### Set Session ID Programmatically

**Android:**

```java
TapAsih.setSessionId("user-session-123");
```

**Flutter:**

```dart
await TapAsih.setSessionId('user-session-123');
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

// Track page
TapAsih.trackPage("PageName");

// Set session ID
TapAsih.setSessionId("session-id");

// Get session ID
String sessionId = TapAsih.getSessionId();

// Check if initialized
boolean initialized = TapAsih.isInitialized();

// Show session dialog
TapAsih.showSessionDialog();

// Clear session ID
TapAsih.clearSessionId();

// Destroy SDK
TapAsih.destroy();
```

### Flutter

```dart
// Initialize SDK
await TapAsih.initialize(config);

// Track page
await TapAsih.trackPage('PageName');

// Set session ID
await TapAsih.setSessionId('session-id');

// Get session ID
final sessionId = await TapAsih.getSessionId();

// Check if initialized
final initialized = await TapAsih.isInitialized();

// Show session dialog
await TapAsih.showSessionDialog();

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

## Error Handling

The SDK handles errors gracefully:

- **Network Errors**: Automatic retry with exponential backoff
- **Expired Token**: SDK automatically stops sending data (no user action needed)
- **Missing Session ID**: Shows dialog to user automatically
- **Offline Mode**: Queues data for later transmission

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

### SDK not tracking activities

1. Check if SDK is initialized: `TapAsih.isInitialized()`
2. Verify developer token is valid
3. Ensure user has provided Session ID
4. Check debug logs for errors

### Data not being sent

- Verify developer token is not expired on your website
- If expired, update token in your app's code (SDK will automatically resume)
- Ensure network connectivity
- Check debug logs for API errors

### Session dialog not showing

- Check if session ID already exists
- Ensure SDK is initialized before calling `showSessionDialog()`
- Check debug logs for errors

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
