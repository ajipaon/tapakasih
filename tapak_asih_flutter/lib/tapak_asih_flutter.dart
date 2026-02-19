import 'tapak_asih_platform_interface.dart';
import 'config/tapak_asih_config.dart';

/// TapakAsih Flutter SDK - Activity Tracking SDK
///
/// Main class for initializing and using the TapakAsih SDK in Flutter apps.
///
/// Usage:
/// ```dart
/// // Initialize SDK
/// final config = TapakAsihConfig(
///   developerToken: 'your_developer_token',
///   enableDebugLogs: true,
/// );
/// await TapAsih.initialize(config);
///
/// // Track page
/// await TapAsih.trackPage('HomePage');
///
/// // Set session ID
/// await TapAsih.setSessionId('user-session-id');
/// ```
class TapAsih {
  /// Initialize TapakAsih SDK
  ///
  /// [config] - Configuration object containing developer token and settings
  static Future<void> initialize(TapakAsihConfig config) {
    return TapakAsihPlatform.instance.initialize(config);
  }

  /// Initialize TapakAsih SDK with simplified config
  ///
  /// [developerToken] - Developer token obtained from TapakAsih website
  static Future<void> initializeWithToken(String developerToken) {
    final config = TapakAsihConfig(developerToken: developerToken);
    return TapakAsihPlatform.instance.initialize(config);
  }

  /// Track a page/activity
  ///
  /// [pageName] - Name of the page or route being tracked
  static Future<void> trackPage(String pageName) {
    return TapakAsihPlatform.instance.trackPage(pageName);
  }

  /// Set session ID
  ///
  /// [sessionId] - User's session ID obtained from TapakAsih website
  static Future<void> setSessionId(String sessionId) {
    return TapakAsihPlatform.instance.setSessionId(sessionId);
  }

  /// Get current session ID
  ///
  /// Returns the current session ID or null if not set
  static Future<String?> getSessionId() {
    return TapakAsihPlatform.instance.getSessionId();
  }

  /// Check if SDK is initialized
  ///
  /// Returns true if SDK is initialized, false otherwise
  static Future<bool> isInitialized() {
    return TapakAsihPlatform.instance.isInitialized();
  }

  /// Show session dialog manually
  ///
  /// Displays/// session ID input dialog to user
  static Future<void> showSessionDialog() {
    return TapakAsihPlatform.instance.showSessionDialog();
  }

  /// Clear session ID
  ///
  /// Removes the currently stored session ID
  static Future<void> clearSessionId() {
    return TapakAsihPlatform.instance.clearSessionId();
  }

  /// Destroy SDK and release resources
  ///
  /// Cleans up SDK resources and stops tracking
  static Future<void> destroy() {
    return TapakAsihPlatform.instance.destroy();
  }
}