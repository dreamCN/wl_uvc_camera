import 'dart:async';

import 'package:flutter/services.dart';

class WlUvcCamera {
  static String picPath = "";
  static bool cameraOpened = false;

  static final MethodChannel _channel = const MethodChannel('wl_uvc_camera')
    ..setMethodCallHandler(_methodChannelHandler);

  static Future<String?> get platformVersion async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<String> takePicture() async {
    picPath = "";
    await _channel.invokeMethod('takePicture');
    int n = 5;
    String path = "";
    while (n > 0) {
      if (picPath.isNotEmpty) {
        path = picPath;
        picPath = "";
        break;
      } else {
        await Future.delayed(const Duration(milliseconds: 100));
      }
      n--;
    }
    return path;
  }

  static startPreview() {
    _channel.invokeMethod("startPreview");
  }

  static stopPreview() {
    _channel.invokeMethod("stopPreview");
  }

  //方法是异步的
  static Future<String> _methodChannelHandler(MethodCall call) async {
    String result = "";
    switch (call.method) {
      //收到Android的调用，并返回数据
      case "callFlutter":
        print('-----收到来自Android的消息');
        print(call.arguments);
        result = "收到来自Android的消息";
        break;
      case "cameraOpened":
        cameraOpened = call.arguments ?? false;
        result = call.arguments.toString();
        break;
      case "takePictureSuccess":
        picPath = call.arguments;
        break;
    }
    return result;
  }
}
