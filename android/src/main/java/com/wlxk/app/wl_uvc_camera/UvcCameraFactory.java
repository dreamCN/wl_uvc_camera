package com.wlxk.app.wl_uvc_camera;

import android.content.Context;

import io.flutter.plugin.common.MessageCodec;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.platform.PlatformView;
import io.flutter.plugin.platform.PlatformViewFactory;

public class UvcCameraFactory extends PlatformViewFactory {

    private MethodChannel mChannel;
    public UvcCameraView mUvcCameraView;
    public int mViewId;


    public UvcCameraFactory(MessageCodec<Object> createArgsCodec, MethodChannel channel) {
        super(createArgsCodec);
        mChannel = channel;
    }

    /**
     * @param context context
     * @param viewId  在Flutter端AndroidView的唯一识别id
     * @param args    Flutter端AndroidView传递过来的参数
     * @return 结果
     */
    @Override
    public PlatformView create(Context context, int viewId, Object args) {
        mViewId = viewId;
        mUvcCameraView = new UvcCameraView(context, mChannel);
        return mUvcCameraView;
    }

    public void takePicture() {
        mUvcCameraView.takePicture();
    }

    public void startPreview() {
        mUvcCameraView.startPreview();
    }

    public void stopPreview() {
        mUvcCameraView.stopPreview();
    }

}
