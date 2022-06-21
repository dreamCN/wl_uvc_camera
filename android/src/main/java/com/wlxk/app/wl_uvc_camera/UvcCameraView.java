package com.wlxk.app.wl_uvc_camera;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;

import com.jiangdg.usbcamera.utils.FileUtils;
import com.serenegiant.usb.widget.CameraViewInterface;
import com.serenegiant.usb.widget.UVCCameraTextureView;

import java.util.List;

import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.platform.PlatformView;

public class UvcCameraView implements PlatformView {

    private View mNativeView;
    private MethodChannel mChannel;
    private UVCCameraTextureView mUVCCameraView;
    private UVCCameraHelper mCameraHelper;
    private Boolean isRequest = false;
    private Boolean isPreview = false;
    private Boolean isCameraOpened = false;

    private CameraViewInterface.Callback callback = new CameraViewInterface.Callback() {

        @Override
        public void onSurfaceCreated(CameraViewInterface view, Surface surface) {
            if (!isPreview && mCameraHelper.isCameraOpened()) {
                isCameraOpened = true;
            }
        }

        @Override
        public void onSurfaceChanged(CameraViewInterface view, Surface surface, int width, int height) {

        }

        @Override
        public void onSurfaceDestroy(CameraViewInterface view, Surface surface) {
            if (isPreview && mCameraHelper.isCameraOpened()) {
                mCameraHelper.stopPreview();
                isPreview = false;
            }
        }
    };

    private UVCCameraHelper.OnMyDevConnectListener listener = new UVCCameraHelper.OnMyDevConnectListener() {

        @SuppressLint("NewApi")
        @Override
        public void onAttachDev(UsbDevice device) {
            // request open permission
            if (!isRequest) {
                isRequest = true;
                List<UsbDevice> usbDeviceList = mCameraHelper.getUsbDeviceList();
                int n = 0;
                for (int i = 0; i < usbDeviceList.size(); i++) {
                    UsbDevice usbDevice = usbDeviceList.get(i);
                    if (usbDevice.getProductName().contains("ME-")) {
                        n = i;
                        break;
                    }
                }
                mCameraHelper.requestPermission(n);
            }
        }

        @Override
        public void onDettachDev(UsbDevice device) {
            // close camera
            if (isRequest) {
                isRequest = false;
                mCameraHelper.closeCamera();
//                showShortMsg(device.getDeviceName() + " is out");
            }
        }

        @Override
        public void onConnectDev(UsbDevice device, boolean isConnected) {
            if (!isConnected) {
                isPreview = false;
            } else {
                isPreview = true;
                isCameraOpened = true;
            }
        }

        @Override
        public void onDisConnectDev(UsbDevice device) {
            isCameraOpened = false;
        }
    };


    public UvcCameraView(Context context, MethodChannel channel) {
        mChannel = channel;
        mNativeView = LayoutInflater.from(context).inflate(R.layout.camera_view, null, false);
        mUVCCameraView = mNativeView.findViewById(R.id.texture_view);
        initUVCCamera(context);
    }

    private void initUVCCamera(Context context) {
        mCameraHelper = UVCCameraHelper.getInstance();
        mUVCCameraView.setCallback(callback);
        mCameraHelper.setDefaultPreviewSize(1280, 720);
        mCameraHelper.initUSBMonitor(context, mUVCCameraView, listener);
        mCameraHelper.setOnPreviewFrameListener(nv21Yuv -> {
//            Log.d("", "onPreviewResult: " + nv21Yuv.length);
        });
        mCameraHelper.registerUSB();
    }

    public void takePicture() {
        if(!isPreview) {
            mCameraHelper.startPreview(mUVCCameraView);
            isPreview = true;
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {

        }
        String picPath = com.jiangdg.usbcamera.UVCCameraHelper.ROOT_PATH + "USBCamera/images/"
                + System.currentTimeMillis() + com.jiangdg.usbcamera.UVCCameraHelper.SUFFIX_JPEG;

        mCameraHelper.capturePicture(picPath, path -> {
            new Handler(Looper.getMainLooper()).post(() -> mChannel.invokeMethod("takePictureSuccess", path));
        });
        mChannel.invokeMethod("callFlutter", "takePicture");
    }

    public void startPreview() {
        if (!isPreview) {
            mCameraHelper.startPreview(mUVCCameraView);
            isPreview = true;
        }
    }

    public void stopPreview() {
        if (isPreview) {
            mCameraHelper.stopPreview();
            isPreview = false;
        }
    }

    public Boolean isCameraOpened() {
        return isCameraOpened;
    }

    @Override
    public View getView() {
        return mNativeView;
    }

    @Override
    public void dispose() {
        FileUtils.releaseFile();
        // step.4 release uvc camera resources
        if (mCameraHelper != null) {
            mCameraHelper.release();
        }
    }
}