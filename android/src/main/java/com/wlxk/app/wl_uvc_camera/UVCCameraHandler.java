package com.wlxk.app.wl_uvc_camera;


import android.app.Activity;
import android.content.Context;

import com.serenegiant.usb.UVCCamera;
import com.serenegiant.usb.widget.CameraViewInterface;

public class UVCCameraHandler extends AbstractUVCCameraHandler {

	/**
	 * create UVCCameraHandler, use MediaVideoEncoder, try MJPEG, default bandwidth
	 * @param parent
	 * @param cameraView
	 * @param width
	 * @param height
	 * @return
	 */
	public static final UVCCameraHandler createHandler(
			final Activity parent, final CameraViewInterface cameraView,
			final int width, final int height) {

		return createHandler(parent, cameraView, 1, width, height, UVCCamera.FRAME_FORMAT_MJPEG, UVCCamera.DEFAULT_BANDWIDTH);
	}

	/**
	 * create UVCCameraHandler, use MediaVideoEncoder, try MJPEG
	 * @param parent
	 * @param cameraView
	 * @param width
	 * @param height
	 * @param bandwidthFactor
	 * @return
	 */
	public static final UVCCameraHandler createHandler(
			final Activity parent, final CameraViewInterface cameraView,
			final int width, final int height, final float bandwidthFactor) {

		return createHandler(parent, cameraView, 1, width, height, UVCCamera.FRAME_FORMAT_MJPEG, bandwidthFactor);
	}

	/**
	 * create UVCCameraHandler, try MJPEG, default bandwidth
	 * @param parent
	 * @param cameraView
	 * @param encoderType 0: use MediaSurfaceEncoder, 1: use MediaVideoEncoder, 2: use MediaVideoBufferEncoder
	 * @param width
	 * @param height
	 * @return
	 */
	public static final UVCCameraHandler createHandler(
			final Activity parent, final CameraViewInterface cameraView,
			final int encoderType, final int width, final int height) {

		return createHandler(parent, cameraView, encoderType, width, height, UVCCamera.FRAME_FORMAT_MJPEG, UVCCamera.DEFAULT_BANDWIDTH);
	}

	/**
	 * create UVCCameraHandler, default bandwidth
	 * @param parent
	 * @param cameraView
	 * @param encoderType 0: use MediaSurfaceEncoder, 1: use MediaVideoEncoder, 2: use MediaVideoBufferEncoder
	 * @param width
	 * @param height
	 * @param format either UVCCamera.FRAME_FORMAT_YUYV(0) or UVCCamera.FRAME_FORMAT_MJPEG(1)
	 * @return
	 */
	public static final UVCCameraHandler createHandler(
			final Context parent, final CameraViewInterface cameraView,
			final int encoderType, final int width, final int height, final int format) {

		return createHandler(parent, cameraView, encoderType, width, height, format, UVCCamera.DEFAULT_BANDWIDTH);
	}

	/**
	 * create UVCCameraHandler
	 * @param parent
	 * @param cameraView
	 * @param encoderType 0: use MediaSurfaceEncoder, 1: use MediaVideoEncoder, 2: use MediaVideoBufferEncoder
	 * @param width
	 * @param height
	 * @param format either UVCCamera.FRAME_FORMAT_YUYV(0) or UVCCamera.FRAME_FORMAT_MJPEG(1)
	 * @param bandwidthFactor
	 * @return
	 */
	public static final UVCCameraHandler createHandler(
			final Context parent, final CameraViewInterface cameraView,
			final int encoderType, final int width, final int height, final int format, final float bandwidthFactor) {

		final CameraThread thread = new CameraThread(UVCCameraHandler.class, parent, cameraView, encoderType, width, height, format, bandwidthFactor);
		thread.start();
		return (UVCCameraHandler)thread.getHandler();
	}

	protected UVCCameraHandler(final CameraThread thread) {
		super(thread);
	}

	@Override
	public void startPreview(final Object surface) {
		super.startPreview(surface);
	}

//	@Override
//	public void captureStill() {
//		super.captureStill();
//	}

	@Override
	public void captureStill(final String path,OnCaptureListener listener) {
		super.captureStill(path,listener);
	}

	@Override
	public void startCameraFoucs() {
		super.startCameraFoucs();
	}
}
