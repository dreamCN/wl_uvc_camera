import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:wl_uvc_camera/wl_uvc_camera.dart';

void main() {
  const MethodChannel channel = MethodChannel('wl_uvc_camera');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await WlUvcCamera.platformVersion, '42');
  });
}
