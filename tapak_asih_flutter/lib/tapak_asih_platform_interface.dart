import 'package:plugin_platform_interface/plugin_platform_interface.dart';
import 'tapak_asih_method_channel.dart';
import 'models/activity_model.dart';
import 'config/tapak_asih_config.dart';

/// Platform interface for TapakAsih SDK
abstract class TapakAsihPlatform extends PlatformInterface {
  /// Constructs a TapakAsihPlatform.
  TapakAsihPlatform() : super(token: _token);

  static final Object _token = Object();

  static TapakAsihPlatform _instance = MethodChannelTapakAsih();

  /// The default instance of [TapakAsihPlatform] to use.
  ///
  /// Defaults to [MethodChannelTapakAsih].
  static TapakAsihPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [TapakAsihPlatform] when they
  /// register themselves.
  static set instance(TapakAsihPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  /// Initialize TapakAsih SDK
  Future<void> initialize(TapakAsihConfig config) {
    throw UnimplementedError('initialize() has not been implemented.');
  }

  /// Track a page/activity
  Future<void> trackPage(String pageName) {
    throw UnimplementedError('trackPage() has not been implemented.');
  }

  /// Set session ID
  Future<void> setSessionId(String sessionId) {
    throw UnimplementedError('setSessionId() has not been implemented.');
  }

  /// Get current session ID
  Future<String?> getSessionId() {
    throw UnimplementedError('getSessionId() has not been implemented.');
  }

  /// Check if SDK is initialized
  Future<bool> isInitialized() {
    throw UnimplementedError('isInitialized() has not been implemented.');
  }

  /// Check if developer token is expired
  Future<bool> isTokenExpired() {
    throw UnimplementedError('isTokenExpired() has not been implemented.');
  }

  /// Check if SDK can perform tracking
  /// Returns true if SDK is initialized, has developer token, has session ID, and token is not expired
  Future<bool> canTrack() {
    throw UnimplementedError('canTrack() has not been implemented.');
  }

  /// Show session dialog
  Future<void> showSessionDialog() {
    throw UnimplementedError('showSessionDialog() has not been implemented.');
  }

  /// Clear session ID
  Future<void> clearSessionId() {
    throw UnimplementedError('clearSessionId() has not been implemented.');
  }

  /// Destroy SDK
  Future<void> destroy() {
    throw UnimplementedError('destroy() has not been implemented.');
  }
}