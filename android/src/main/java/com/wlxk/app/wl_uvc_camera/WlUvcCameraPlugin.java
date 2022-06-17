package com.wlxk.app.wl_uvc_camera;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.StandardMessageCodec;

/**
 * WlUvcCameraPlugin
 */
public class WlUvcCameraPlugin implements FlutterPlugin, MethodCallHandler {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private MethodChannel channel;
    private UvcCameraFactory mUvcCameraFactory;

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "wl_uvc_camera");
        channel.setMethodCallHandler(this);
        mUvcCameraFactory = new UvcCameraFactory(StandardMessageCodec.INSTANCE, channel);
        flutterPluginBinding.getPlatformViewRegistry()
                .registerViewFactory("uvc_camera_view", mUvcCameraFactory);
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        if (call.method.equals("getPlatformVersion")) {
            result.success("Android " + android.os.Build.VERSION.RELEASE);
        } else if (call.method.equals("takePicture")) {
            mUvcCameraFactory.takePicture();
            result.success("takePicture");
        } else if (call.method.equals("startPreview")) {
            mUvcCameraFactory.startPreview();
            result.success("startPreview");
        } else if (call.method.equals("stopPreview")) {
            mUvcCameraFactory.stopPreview();
            result.success("stopPreview");
        } else {
            result.notImplemented();
        }
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
    }
}
