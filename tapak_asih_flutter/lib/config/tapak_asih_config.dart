/// Configuration class for TapakAsih Flutter SDK
class TapakAsihConfig {
  final String developerToken;
  final bool enableDebugLogs;
  final bool enableOfflineQueue;
  final int retryAttempts;

  TapakAsihConfig({
    required this.developerToken,
    this.enableDebugLogs = false,
    this.enableOfflineQueue = true,
    this.retryAttempts = 3,
  });

  /// Create configuration from map
  factory TapakAsihConfig.fromMap(Map<String, dynamic> map) {
    return TapakAsihConfig(
      developerToken: map['developerToken'] as String,
      enableDebugLogs: map['enableDebugLogs'] as bool? ?? false,
      enableOfflineQueue: map['enableOfflineQueue'] as bool? ?? true,
      retryAttempts: map['retryAttempts'] as int? ?? 3,
    );
  }

  /// Convert configuration to map
  Map<String, dynamic> toMap() {
    return {
      'developerToken': developerToken,
      'enableDebugLogs': enableDebugLogs,
      'enableOfflineQueue': enableOfflineQueue,
      'retryAttempts': retryAttempts,
    };
  }
}