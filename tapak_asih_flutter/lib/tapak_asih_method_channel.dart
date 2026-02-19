import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'tapak_asih_platform_interface.dart';
import 'config/tapak_asih_config.dart';

/// Method channel implementation for TapakAsih SDK
class MethodChannelTapakAsih extends TapakAsihPlatform {
  @visibleForTesting
  final methodChannel = const MethodChannel('tapak_asih');

  @override
  Future<void> initialize(TapakAsihConfig config) async {
    await methodChannel.invokeMethod('initialize', config.toMap());
  }

  @override
  Future<void> trackPage(String pageName) async {
    await methodChannel.invokeMethod('trackPage', {'pageName': pageName});
  }

  @override
  Future<void> setSessionId(String sessionId) async {
    await methodChannel.invokeMethod('setSessionId', {'sessionId': sessionId});
  }

  @override
  Future<String?> getSessionId() async {
    final result = await methodChannel.invokeMethod<String>('getSessionId');
    return result;
  }

  @override
  Future<bool> isInitialized() async {
    final result = await methodChannel.invokeMethod<bool>('isInitialized');
    return result ?? false;
  }

  @override
  Future<void> showSessionDialog() async {
    await methodChannel.invokeMethod('showSessionDialog');
  }

  @override
  Future<void> clearSessionId() async {
    await methodChannel.invokeMethod('clearSessionId');
  }

  @override
  Future<void> destroy() async {
    await methodChannel.invokeMethod('destroy');
  }
}