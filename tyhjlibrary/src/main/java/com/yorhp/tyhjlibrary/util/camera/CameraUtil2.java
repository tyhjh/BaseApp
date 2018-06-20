package com.yorhp.tyhjlibrary.util.camera;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;


import com.yorhp.tyhjlibrary.app.MyApplication;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

public class CameraUtil2 implements SurfaceHolder.Callback {
    private static CameraUtil2 instance;
    private SurfaceView mSurfaceView;
    private Context mContext;
    private Display mDisplay;
    private Camera mCamera;
    private SurfaceHolder mHolder;
    private int mWidthPicture = 1920;
    private int mHeightPicture = 1080;
    private int mWidthPreview = 1920;
    private int mHeightPreview = 1080;
    private boolean isOpen = false;


    /**
     * 声音
     */
    private Camera.ShutterCallback shutter = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {

        }
    };

    private CameraUtil2() {
    }

    public static CameraUtil2 getInstance() {
        if (instance == null) {
            synchronized (CameraUtil2.class) {
                if (instance == null) {
                    instance = new CameraUtil2();
                }
            }
        }
        return instance;
    }

    /**
     * 开启第一个摄像头
     *
     * @param context
     */
    public void onResume(Context context, SurfaceView surfaceView) {
        this.mContext = context;
        WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        if (manager != null) {
            this.mDisplay = manager.getDefaultDisplay();
        } else {
            throw new NullPointerException("WindowManager is null");
        }
        if (mCamera == null) {
            mCamera = Camera.open(0);
        }
        if (isOpen) {
            throw new IllegalStateException("照相机已经开启");
        }
        isOpen = true;
        this.mSurfaceView = surfaceView;
        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mCamera.startPreview();
    }

    public boolean isOpen() {
        return isOpen;
    }

    /**
     * 拍照
     */
    public synchronized String takePic() {

        if (mSurfaceView == null) {
            isOpen = false;
            mHolder = null;
        }
        String filePath =MyApplication.APP_BASE_DIR+"/"+Calendar.getInstance().getTimeInMillis() + ".jpeg";
        try {
            if (null != mCamera) {
                mCamera.takePicture(shutter, null, new MyJpeg(filePath));
            }
        } catch (Exception e) {
            isOpen = false;
            e.printStackTrace();
        }
        return filePath;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        try {
            if (mCamera == null) {
                mCamera = Camera.open(0);
            }
            mCamera.setPreviewDisplay(holder);
        } catch (Exception e) {
            isOpen = false;
            e.printStackTrace();

        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stopPreview();
        closeCamera();
    }

    /**
     * 调整预览画面角度
     *
     * @param cameraId
     * @param camera
     */
    private void setCameraDisplayOrientation(int cameraId, Camera camera) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = mDisplay.getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
            default:
                degrees = 0;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        } else {
            result = (info.orientation - degrees + 360 + 180) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    /**
     * 开始预览
     */
    private void startPreview() {
        try {
            Camera.Parameters para;
            if (null != mCamera) {
                para = mCamera.getParameters();
            } else {
                return;
            }

            /*List<Camera.Size> previewSizes = para.getSupportedPreviewSizes();
            for (int i = 0; i < previewSizes.size(); i++) {
                Log.e("支持的预览尺寸大小", "x：" + previewSizes.get(i).width + " y" + previewSizes.get(i).height);
        }*/

            para.setPreviewSize(1920, 1080);


            /*List<Camera.Size> pictureSizes = para.getSupportedPictureSizes();
            for (int i = 0; i < pictureSizes.size(); i++) {
                Log.e("支持的照片大小", "x：" + pictureSizes.get(i).width + " y" + pictureSizes.get(i).height);
            }*/

            para.setPictureSize(2592, 1944);


            int numberOfCameras = Camera.getNumberOfCameras();
            for (int cameranum = 0; cameranum < numberOfCameras; cameranum++) {
                Camera.CameraInfo info = new Camera.CameraInfo();
                Camera.getCameraInfo(cameranum, info);
            }

            //setPreviewSize(para, mWidthPreview, mHeightPreview);
            //setPictureSize(para, mWidthPicture, mHeightPicture);
            setCameraDisplayOrientation(0, mCamera);
            para.setPreviewFormat(ImageFormat.NV21);
            mCamera.setParameters(para);
            mCamera.startPreview();
        } catch (Exception e) {
            isOpen = false;
            e.printStackTrace();
            //EventBus.getDefault().post(new ShowToastEvent("摄像头错误"));
        }
    }

    /**
     * 停止预览
     */
    private void stopPreview() {
        try {
            if (null != mCamera) {
                mCamera.stopPreview();
            }
        } catch (Exception e) {
            isOpen = false;
            e.printStackTrace();
        }
    }

    /**
     * 关闭相机
     */
    private void closeCamera() {
        if (null != mCamera) {
            try {
                mCamera.setPreviewCallback(null);
                mCamera.setPreviewDisplay(null);
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            } catch (Exception e) {
                isOpen = false;
                e.printStackTrace();

            }
        }
    }

    /**
     * 关闭相机
     */
    public void close() {
        isOpen = false;
        stopPreview();
        closeCamera();
        this.mSurfaceView = null;
        mHolder = null;
    }

    /**
     * 设置保存图片尺寸
     *
     * @param para
     * @param width
     * @param height
     */
    private void setPictureSize(Camera.Parameters para, int width, int height) {
        int absWidth = 0;
        int absHeight = 0;
        List<Camera.Size> supportedPictureSizes = para.getSupportedPictureSizes();
        for (Camera.Size size : supportedPictureSizes) {
            if (Math.abs(width - size.width) < Math.abs(width - absWidth)) {
                absWidth = size.width;
                absHeight = size.height;
            }
        }
        para.setPictureSize(absWidth, absHeight);
    }

    /**
     * 设置预览图片尺寸
     *
     * @param para
     * @param width
     * @param height
     */
    private void setPreviewSize(Camera.Parameters para, int width, int height) {
        int absWidth = 0;
        int absHeight = 0;
        List<Camera.Size> supportedPreviewSizes = para.getSupportedPreviewSizes();
        for (Camera.Size size : supportedPreviewSizes) {
            if (Math.abs(width - size.width) < Math.abs(width - absWidth)) {
                absWidth = size.width;
                absHeight = size.height;
            }
        }
        para.setPreviewSize(absWidth, absHeight);
    }

    /**
     * 发送广播刷新图片到相册
     *
     * @param filePath
     */
    private void sendBroadcastToRefresh(String filePath) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(new File(filePath));
        intent.setData(uri);
        mContext.sendBroadcast(intent);
    }

    /**
     * 保存图片旋转角度使摆正
     *
     * @param bm
     * @return
     */
    private Bitmap rotateBitmap(Bitmap bm) {
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.setRotate((float) 90);
        return Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
    }

    private class MyJpeg implements Camera.PictureCallback {
        private String filePath;

        public MyJpeg(String filePath) {
            this.filePath = filePath;
        }

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            CameraUtil2.getInstance().close();
            Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
            //TODO 标柜不转角度,值班柜转90度
//            bm = rotateBitmap(bm);

            BufferedOutputStream bos = null;
            try {
                bos = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
                bm.compress(Bitmap.CompressFormat.JPEG, 60, bos);
                bos.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                bm.recycle();
                sendBroadcastToRefresh(filePath);
                try {
                    camera.startPreview();
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    boolean mIsRecording;
    MediaRecorder mMediaRecorder;
    private String videoPath;

    public String getVideoPath() {
        return videoPath;
    }

    public boolean ismIsRecording() {
        return mIsRecording;
    }

    public void startMediaRecorder(final long time) {
        mCamera.unlock();
        mIsRecording = true;
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.reset();
        mMediaRecorder.setCamera(mCamera);
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        CamcorderProfile mCamcorderProfile = CamcorderProfile.get(Camera.CameraInfo.CAMERA_FACING_BACK,
                CamcorderProfile.QUALITY_HIGH);
        mMediaRecorder.setProfile(mCamcorderProfile);
        mMediaRecorder.setOutputFile(getOutputMediaFile(MEDIA_TYPE_VIDEO).toString());
        mMediaRecorder.setPreviewDisplay(mHolder.getSurface());

        try {
            mMediaRecorder.prepare();
        } catch (Exception e) {
            mIsRecording = false;
            e.printStackTrace();
            mCamera.lock();
        }
        mMediaRecorder.start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(time);
                    MyApplication.getInstance().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (CameraUtil2.getInstance().isOpen() && CameraUtil2.getInstance().ismIsRecording()) {
                                CameraUtil2.getInstance().stopMediaRecorder();
                                //Log.e("videoPath", CameraUtil2.getInstance().getVideoPath() + "");
                            }
                        }
                    });
                } catch (InterruptedException e) {
                    mIsRecording = false;
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public void stopMediaRecorder() {
        if (mMediaRecorder != null) {
            if (mIsRecording) {
                mMediaRecorder.stop();
                //mCamera.lock();
                mMediaRecorder.reset();
                mMediaRecorder.release();
                mMediaRecorder = null;
                mIsRecording = false;
                try {
                    mCamera.reconnect();
                } catch (IOException e) {
                    mIsRecording = false;
                    e.printStackTrace();
                }
            }
        }
    }


    private File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Camera App");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("linc", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        if (mediaFile != null) {
            videoPath = mediaFile.getPath();
        }

        return mediaFile;
    }


}




