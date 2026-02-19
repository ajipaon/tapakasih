import 'package:flutter_test/flutter_test.dart';
import 'package:tapak_asih_flutter/tapak_asih_flutter.dart';
import 'package:tapak_asih_flutter/tapak_asih_flutter_platform_interface.dart';
import 'package:tapak_asih_flutter/tapak_asih_flutter_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockTapakAsihFlutterPlatform
    with MockPlatformInterfaceMixin
    implements TapakAsihFlutterPlatform {
  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final TapakAsihFlutterPlatform initialPlatform = TapakAsihFlutterPlatform.instance;

  test('$MethodChannelTapakAsihFlutter is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelTapakAsihFlutter>());
  });

  test('getPlatformVersion', () async {
    TapakAsihFlutter tapakAsihFlutterPlugin = TapakAsihFlutter();
    MockTapakAsihFlutterPlatform fakePlatform = MockTapakAsihFlutterPlatform();
    TapakAsihFlutterPlatform.instance = fakePlatform;

    expect(await tapakAsihFlutterPlugin.getPlatformVersion(), '42');
  });
}
