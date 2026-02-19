import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'tapak_asih_flutter_platform_interface.dart';

/// An implementation of [TapakAsihFlutterPlatform] that uses method channels.
class MethodChannelTapakAsihFlutter extends TapakAsihFlutterPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('tapak_asih_flutter');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>(
      'getPlatformVersion',
    );
    return version;
  }
}
