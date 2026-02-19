# tapak_asih_flutter

TapakAsih SDK Flutter Plugin - Activity tracking SDK for Flutter applications.

## Overview

TapakAsih Flutter plugin allows you to monitor user behavior in your Flutter applications. It automatically tracks page views and sends activity data to your API endpoint.

## Features

- 🚀 **Automatic Activity Tracking** - Track all user activities
- 🔐 **Dual Authentication** - Developer token + User session ID
- 📱 **Flutter Integration** - Seamless Flutter plugin
- 🔄 **Offline Support** - Queue data when offline, send when online
- 🎯 **Easy Integration** - Simple API with minimal setup
- 🔧 **Configurable** - Customize retry attempts, debug logs, etc.

## Installation

Add the plugin to your `pubspec.yaml`:

```yaml
dependencies:
  tapak_asih_flutter: ^1.0.0
```

Then run:

```bash
flutter pub get
```

## Quick Start

1. Initialize in main.dart:

```dart
import 'package:tapak_asih_flutter/tapak_asih_flutter.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();

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

```dart
await TapAsih.showSessionDialog();
```

### Set Session ID Programmatically

```dart
await TapAsih.setSessionId('user-session-123');
```

## Data Sent to API

The SDK sends the following data to `https://api.pycompany.com/actifity/claim`:

```json
{
  "epochtime": 1234567890,
  "pageName": "HomePage",
  "sessionId": "user-session-123"
}
```

### Request Headers

- `Content-Type: application/json`
- `Authorization: Bearer {developer_token}`

## Configuration Options

```dart
final config = TapakAsihConfig(
  developerToken: 'your_token',
  enableDebugLogs: true,
  enableOfflineQueue: true,
  retryAttempts: 3,
);
```

## API Methods

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
  - SDK will show session dialog if no session ID exists
  - Activity tracking is enabled
  - User must provide session ID to use the app
- **NO_DEMAND**: Session ID is NOT required
  - SDK will NOT show session dialog
  - Activity tracking is disabled
  - User can use the app without session ID
  - All `trackPage()` calls are silently ignored

This check happens once during initialization. If the check fails, the SDK defaults to ON_DEMAND (safer option).

## Requirements

- **Flutter**: 3.0+
- **Dart**: 2.17+
- **Android**: 5.0+ (API 21+)

## Example App

A complete example app is included in the `example/` directory. Run it with:

```bash
cd example
flutter run
```

## License

Copyright © 2025 PaonDev. All rights reserved.

## Support

For issues and questions, please contact: [support@paondev.com]

## Changelog

### Version 1.0.0

- Initial release
- Flutter plugin support
- Automatic activity tracking
- Session management
- Offline support
