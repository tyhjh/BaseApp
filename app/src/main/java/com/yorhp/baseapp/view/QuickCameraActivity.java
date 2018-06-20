package com.yorhp.baseapp.view;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.yorhp.baseapp.R;
import com.yorhp.tyhjlibrary.app.BaseActivity;
import com.yorhp.tyhjlibrary.app.MyApplication;
import com.yorhp.tyhjlibrary.util.camera.CameraUtil2;
import com.yorhp.tyhjlibrary.util.common.LogUtils;
import com.yorhp.tyhjlibrary.view.permisson.PermissonUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_quick_camera)
public class QuickCameraActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @ViewById
    LinearLayout mainLayout;


    @ViewById
    SurfaceView surfaceView;

    @AfterViews
    void afterView(){

    }


    @Click
    void btn_take_photo() {

        if(!PermissonUtil.checkPermission(this,new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})){
            return;
        }

        SurfaceView surfaceView = getSurfaceView();
        mainLayout.addView(surfaceView);
        if (!CameraUtil2.getInstance().isOpen()) {
            CameraUtil2.getInstance().onResume(MyApplication.getInstance(), surfaceView);
        }

        MyApplication.getThreadPoolUtils().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(900);
                    //LogUtils.log(CameraUtil2.getInstance().takePic());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @NonNull
    private SurfaceView getSurfaceView() {
        //初始化相机
        View view = mainLayout.findViewWithTag("surfaceView");
        if (view != null) {
            mainLayout.removeView(view);
        }
        SurfaceView surfaceView = new SurfaceView(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(50, 50);
        surfaceView.setLayoutParams(params);
        surfaceView.setTag("surfaceView");
        return surfaceView;
    }


    @Click
    void btn_take_video() {
        if(!PermissonUtil.checkPermission(this,new String[]{Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO})){
            return;
        }


        SurfaceView surfaceView = getSurfaceView();
        mainLayout.addView(surfaceView);
        if (!CameraUtil2.getInstance().isOpen()) {
            CameraUtil2.getInstance().onResume(MyApplication.getInstance(), surfaceView);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                CameraUtil2.getInstance().startMediaRecorder(3000);
            }
        }).start();

    }


    @Override
    protected void onDestroy() {
        CameraUtil2.getInstance().close();
        super.onDestroy();
    }
}
