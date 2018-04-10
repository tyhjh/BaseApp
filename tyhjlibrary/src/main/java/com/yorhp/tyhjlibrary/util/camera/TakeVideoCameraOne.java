package com.yorhp.tyhjlibrary.util.camera;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.yorhp.tyhjlibrary.R;
import com.yorhp.tyhjlibrary.app.BaseActivity;
import com.yorhp.tyhjlibrary.app.MyApplication;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

public class TakeVideoCameraOne extends BaseActivity {

    SurfaceView mSurfaceView;
    SurfaceHolder mSurfaceHolder;
    Camera mCamera;
    boolean mIsRecording;
    MediaRecorder mMediaRecorder;
    private File mPhotoFile;
    private String TAG="相机";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_video_camera_one);

        mSurfaceView = findViewById(R.id.surfaceView);

        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                releaseCamera();
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                initPreview();
                MyApplication.getThreadPoolUtils().execute(new Runnable() {
                    @Override
                    public void run() {

                        startMediaRecorder();

                        try {
                            MyApplication.getInstance().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                }
                            });
                            Thread.sleep(1000);
                            MyApplication.getInstance().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    onPhotoClicked();
                                }
                            });
                            Thread.sleep(5000);
                            MyApplication.getInstance().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    stopMediaRecorder();
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                       int height) {
            }
        });



    }


    protected void initPreview() {
        mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setCameraDisplayOrientation(this, Camera.CameraInfo.CAMERA_FACING_BACK, mCamera);
        mCamera.startPreview();



    }


    public static void setCameraDisplayOrientation(Activity activity, int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
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
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }


    public void startMediaRecorder() {
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
        mMediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());

        try {
            mMediaRecorder.prepare();
        } catch (Exception e) {
            mIsRecording = false;
            Toast.makeText(this, "fail", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            mCamera.lock();
        }
        mMediaRecorder.start();
    }

    private File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Camera App");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("linc", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    protected void releaseCamera() {
        if(mCamera!=null){
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
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
                    Toast.makeText(this, "reconect fail", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }
    }


    public void onPhotoClicked() {
        mCamera.takePicture(null, null, mPicture);
    }


    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            mPhotoFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (mPhotoFile == null){
                Log.d(TAG, "Error creating media file, check storage permissions: ");
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(mPhotoFile);
                fos.write(data);
                fos.close();
                Log.d(TAG,"save picture success");
                //notify
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    Intent mediaScanIntent = new Intent(
                            Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri contentUri = Uri.fromFile(mPhotoFile); //out is your output file
                    mediaScanIntent.setData(contentUri);
                    TakeVideoCameraOne.this.sendBroadcast(mediaScanIntent);
                } else {
                    sendBroadcast(new Intent(
                            Intent.ACTION_MEDIA_MOUNTED,
                            Uri.parse("file://"
                                    + Environment.getExternalStorageDirectory())));
                }
                mCamera.reconnect();
            } catch (FileNotFoundException e) {
                Log.d(TAG, "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d(TAG, "Error accessing file: " + e.getMessage());
            }
        }
    };


}
