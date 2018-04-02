package com.yorhp.baseapp;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.yorhp.baseapp.adapter.StringAdapter;
import com.yorhp.tyhjlibrary.app.BaseActivity;
import com.yorhp.tyhjlibrary.util.camera.CameraUtil;
import com.yorhp.tyhjlibrary.util.common.LogUtils;
import com.yorhp.tyhjlibrary.view.dialog.BottomFragment;
import com.yorhp.tyhjlibrary.view.dialog.MyDialog;
import com.yorhp.tyhjlibrary.view.permisson.PermissonUtil;
import com.yorhp.tyhjlibrary.view.recycleView.MRecycleView;
import com.yorhp.tyhjlibrary.view.toast.SnackbarUtil;
import com.yorhp.tyhjlibrary.view.toast.ToastUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    public static final String[] PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.SEND_SMS,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_CALENDAR,
            Manifest.permission.BODY_SENSORS,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO

    };

    ArrayList<String> nameList = new ArrayList<>();
    StringAdapter stringAdapter;

    @ViewById
    MRecycleView rcyl_test;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        camerabakListener = new CameraUtil.CamerabakListener() {
            @Override
            public void getFile(File file) {
                ImageView imageView = new ImageView(getContext());
                MyDialog dialog = new MyDialog(getContext(), imageView, com.yorhp.tyhjlibrary.R.style.dialog2);
                dialog.setCancelable(true);
                dialog.show();
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Picasso.get().load(file.getAbsoluteFile()).into(imageView);
            }
        };
    }

    @AfterViews
    void afterView() {
        stringAdapter = new StringAdapter(nameList, this);
        rcyl_test.init(stringAdapter, 0);
        rcyl_test.setRecycleViewEventListener(new MRecycleView.RecycleViewEventListener() {
            @Override
            public void onScollToBottom() {
                toast("没有更多内容了");
            }
        });
        initData();
    }

    @Background
    void initData() {
        try {
            Thread.sleep(200);
            nameList.add("打印工具测试");
            stringAdapter.addOne(nameList.size() - 1);
            Thread.sleep(200);
            nameList.add("权限工具测试");
            stringAdapter.addOne(nameList.size() - 1);
            nameList.add("相机工具测试");
            stringAdapter.addOne(nameList.size() - 1);
            nameList.add("底部弹窗工具测试");
            nameList.add("SnackBar工具测试");
            nameList.add("停止SnackBar");
            nameList.add("Toast工具测试");
            nameList.add("图片加载工具");
            nameList.add("EventBus");
            nameList.add("数据库");
            nameList.add("属性动画测试");
            nameList.add("界面切换动画测试");
            nameList.add("Activity元素共享测试");
            nameList.add("控件剪裁测试");
            nameList.add("状态栏颜色测试");
            nameList.add("发送邮件测试");
            nameList.add("电话号码测试");
            nameList.add("语音录制");
            nameList.add("视频播放器");
            stringAdapter.update();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Integer event) {
        switch (event){
            case 0://打印测试
                LogUtils.logJson("{ \"code\": 200, \"msg\": \"ok\", \"data\": { \"version\": \"1.0\", \"url\": \"http://ozr6klu3a.bkt.clouddn.com/app_1516350756866\", \"info\": \"第一个版本\", \"imageUrl\": null, \"forced\": false } }");
                ToastUtil.toast(getContext(), "已打印信息");
                break;
            case 1://权限测试
                if (PermissonUtil.checkPermission(this, MainActivity.PERMISSIONS))
                    toast("已获取所有权限");
                break;
            case 2://相机测试
                if (PermissonUtil.checkPermission(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}))
                    CameraUtil.choosePicture(this);
                break;
            case 3://底部弹窗工具测试
                BottomFragment shareFragment = new BottomFragment();
                ImageView imageView=new ImageView(this);
                imageView.setImageResource(R.mipmap.ic_launcher);
                shareFragment.setView(imageView);
                shareFragment.show(this.getFragmentManager(), "ShareFragment");
                break;
            case 4:
                SnackbarUtil.snkbarWait(rcyl_test,"请等待");
                break;
            case 5:
                SnackbarUtil.ShortSnackbar(rcyl_test,"OK",SnackbarUtil.Info).show();
                break;
            case 6:
                toast("嗨，你好呀");
                break;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
