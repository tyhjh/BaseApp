package com.yorhp.tyhjlibrary.app;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.yorhp.tyhjlibrary.util.camera.CameraUtil;
import com.yorhp.tyhjlibrary.util.view.ScreenUtil;
import com.yorhp.tyhjlibrary.view.permisson.PermissionsActivity;
import com.yorhp.tyhjlibrary.view.toast.ToastUtil;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static com.yorhp.tyhjlibrary.app.MyApplication.PHONERAND;
import static com.yorhp.tyhjlibrary.view.permisson.PermissonUtil.CROP_PHOTO;
import static com.yorhp.tyhjlibrary.view.permisson.PermissonUtil.PERMISSIONS_REQUEST_CODE;
import static com.yorhp.tyhjlibrary.view.permisson.PermissonUtil.PICK_PHOTO;
import static com.yorhp.tyhjlibrary.view.permisson.PermissonUtil.TAKE_PHOTO;

/**
 * Created by Tyhj on 2018/3/31.
 */

@EActivity
public class BaseActivity extends AppCompatActivity {

    protected CameraUtil.CamerabakListener camerabakListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (MyApplication.SCREEN_WIDTH == 0) {
            ScreenUtil.getScreenSize(this);
        }
        MyApplication.activities.add(this);
    }

    @UiThread
    protected void toast(String msg) {
        ToastUtil.toast(this,msg);
    }

    @UiThread
    protected void lvBack(Class activity) {
        startActivity(new Intent(this, activity));
    }

    @UiThread
    protected void lvNoback(Class activity) {
        startActivity(new Intent(this, activity));
        finish();
    }

    @UiThread
    protected void lvForever(Class activity) {
        startActivity(new Intent(this, activity));
        MyApplication.closeAllActivity();
    }

    @UiThread
    protected void snkbar(View view, String msg, int time) {
        Snackbar.make(view, msg, time).show();
    }


    protected Context getContext(){
        return this;
    }

    protected void requestPermissionFail(){
        toast("获取权限失败");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == PERMISSIONS_REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            requestPermissionFail();
        }else if((requestCode == PICK_PHOTO||requestCode ==CROP_PHOTO||requestCode ==TAKE_PHOTO)&&camerabakListener!=null){
            CameraUtil.getPhoto(requestCode, resultCode, this, data,camerabakListener);
        }
    }


    //设置状态栏颜色
    protected void setStatusBarFontDark(boolean dark) {
        // 小米MIUI
        if (PHONERAND == 0 || PHONERAND == 1) {
            try {
                Window window = getWindow();
                Class clazz = getWindow().getClass();
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                int darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {    //状态栏亮色且黑色字体
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);
                } else {       //清除黑色字体
                    extraFlagField.invoke(window, 0, darkModeFlag);
                }
                PHONERAND = 1;
            } catch (Exception e) {
                PHONERAND = 0;
            }
        }

        // 魅族FlymeUI
        if (PHONERAND == 0 || PHONERAND == 2) {
            try {
                Window window = getWindow();
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                PHONERAND = 2;
            } catch (Exception e) {
                PHONERAND = 0;
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PHONERAND = 3;
            if (dark) {
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
    }

    protected void backToResult(String key[], String value[]) {
        Intent intent = new Intent();
        //把返回数据存入Intent
        for (int i = 0; i < key.length; i++) {
            intent.putExtra(key[i], value[i]);
        }
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.activities.remove(this);
    }


}
