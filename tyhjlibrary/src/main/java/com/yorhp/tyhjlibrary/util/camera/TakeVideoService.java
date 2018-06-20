package com.yorhp.tyhjlibrary.util.camera;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;

import com.yorhp.tyhjlibrary.app.MyApplication;
import com.yorhp.tyhjlibrary.util.common.LogUtils;

import org.greenrobot.eventbus.EventBus;

public class TakeVideoService extends Service {

    public static WindowManager mWindowManager;

    AutoFitTextureView videoFloat;


    @Override
    public void onCreate() {
        super.onCreate();
        createFloatWindow(this);
    }


    private void takeVideo() {
        final TakeVideoUtil takeVideoUtil = new TakeVideoUtil();
        takeVideoUtil.setContext(this);
        takeVideoUtil.setmTextureView(videoFloat);
        takeVideoUtil.initCamera();

        MyApplication.getThreadPoolUtils().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    MyApplication.getInstance().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                takeVideoUtil.startRecordingVideo(TakeVideoService.this, MyApplication.APP_BASE_DIR + "/movie/" + System.currentTimeMillis());
                            }
                        }
                    });
                    Thread.sleep(11000);
                    MyApplication.getInstance().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                takeVideoUtil.stopRecordingVideo();
                                mWindowManager.removeView(videoFloat);
                                EventBus.getDefault().post(new String(takeVideoUtil.getFilePath()));
                                stopSelf();
                            }
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public void createFloatWindow(Context context) {
        WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
        WindowManager windowManager = getWindowManager(context);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }

        //设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        //调整悬浮窗显示的停靠位置为左侧置顶
        wmParams.gravity = Gravity.START | Gravity.TOP;

        DisplayMetrics dm = new DisplayMetrics();
        //取得窗口属性
        windowManager.getDefaultDisplay().getMetrics(dm);
        //窗口的宽度
        int screenWidth = dm.widthPixels;
        //窗口高度
        int screenHeight = dm.heightPixels;
        //以屏幕左上角为原点，设置x、y初始值，相对于gravity
        wmParams.x = screenWidth;
        wmParams.y = screenHeight;


        //设置悬浮窗口长宽数据
        wmParams.width = 200;
        wmParams.height = 200;

        videoFloat = new AutoFitTextureView(context);
        windowManager.addView(videoFloat, wmParams);
        takeVideo();
    }

    /**
     * 返回当前已创建的WindowManager。
     */
    private static WindowManager getWindowManager(Context context) {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.log("Service关闭了");
    }
}
