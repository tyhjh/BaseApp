package com.yorhp.baseapp.view;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yorhp.baseapp.R;
import com.yorhp.baseapp.adapter.StringAdapter;
import com.yorhp.baseapp.model.AppRecord;
import com.yorhp.tyhjlibrary.app.BaseActivity;
import com.yorhp.tyhjlibrary.app.MyApplication;
import com.yorhp.tyhjlibrary.util.camera.CameraUtil;
import com.yorhp.tyhjlibrary.util.camera.ShootVideoActivity_;
import com.yorhp.tyhjlibrary.util.camera.TakeVideoService;
import com.yorhp.tyhjlibrary.util.common.FormatUtil;
import com.yorhp.tyhjlibrary.util.common.LogUtils;
import com.yorhp.tyhjlibrary.util.common.TimeUtil;
import com.yorhp.tyhjlibrary.util.database.MLiteOrm;
import com.yorhp.tyhjlibrary.util.email.EmailUtil;
import com.yorhp.tyhjlibrary.util.file.FileUtil;
import com.yorhp.tyhjlibrary.util.internet.InternetUtil;
import com.yorhp.tyhjlibrary.util.phone.phoneUtil;
import com.yorhp.tyhjlibrary.util.retrofite.MRetrofite;
import com.yorhp.tyhjlibrary.util.retrofite.UploadApi;
import com.yorhp.tyhjlibrary.util.sound.RecordUtil;
import com.yorhp.tyhjlibrary.util.view.ImageUtil;
import com.yorhp.tyhjlibrary.util.view.ScreenUtil;
import com.yorhp.tyhjlibrary.view.dialog.BottomFragment;
import com.yorhp.tyhjlibrary.view.dialog.DialogUtil;
import com.yorhp.tyhjlibrary.view.dialog.MyDialog;
import com.yorhp.tyhjlibrary.view.dialog.PopupWindowFactory;
import com.yorhp.tyhjlibrary.view.permisson.FloatWindowManager;
import com.yorhp.tyhjlibrary.view.permisson.PermissonUtil;
import com.yorhp.tyhjlibrary.view.recycleView.MRecycleView;
import com.yorhp.tyhjlibrary.view.toast.SnackbarUtil;
import com.yorhp.tyhjlibrary.view.toast.ToastUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Touch;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    PopupWindowFactory mPop;

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
    long logTime;

    @ViewById
    MRecycleView rcyl_test;

    @ViewById()
    Button btn_outlin;


    @ViewById
    Button btn_record;



    int state = 0;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Click
    void btn_outlin() {
        if (state == 0) {
            btn_outlin.setOutlineProvider(ImageUtil.getOutline(false, 10, 10));
            state = 1;
        } else {
            btn_outlin.setOutlineProvider(ImageUtil.getOutline(true, 10, 10));
            state = 0;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logTime = System.currentTimeMillis();
        //Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        //startActivity(intent);

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
            nameList.add("屏幕信息");
            nameList.add("网络监听");
            nameList.add("控件剪裁测试");
            nameList.add("录像工具测试");
            nameList.add("后台录像工具测试");
            nameList.add("发送邮件测试");
            nameList.add("语音录制");
            nameList.add("语音播放");
            nameList.add("自定义快速拍照，摄像");
            nameList.add("界面切换动画测试");
            nameList.add("Activity元素共享测试");
            nameList.add("状态栏颜色测试");
            nameList.add("电话号码测试");
            nameList.add("视频播放器");
            nameList.add("语音播放");
            nameList.add("retrofite测试");
            stringAdapter.update();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Integer event) {
        switch (event) {
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
                ImageView imageView = new ImageView(this);
                imageView.setImageResource(R.mipmap.ic_launcher);
                imageView.setBackgroundColor(getResources().getColor(R.color.gainsboro));
                shareFragment.setView(imageView);
                shareFragment.show(this.getFragmentManager(), "ShareFragment");
                break;
            case 4:
                SnackbarUtil.snkbarWait(rcyl_test, "请等待");
                break;
            case 5:
                SnackbarUtil.ShortSnackbar(rcyl_test, "OK", SnackbarUtil.Info).show();
                break;
            case 6:
                toast("嗨，你好呀");
                break;
            case 7://图片加载
                showImage();
                break;
            case 8://EventBus
                toast("我在这里");
                break;
            case 9://数据库
                showDatabase();
                break;
            case 10://屏幕数据
                toast("屏幕分辨率为：" + ScreenUtil.SCREEN_HEIGHT + "x" + ScreenUtil.SCREEN_WIDTH);
                break;
            case 11://网络监听
                internetStatus();
                break;
            case 12://控件剪裁
                Outline();
                break;
            case 13://录制视频
                lvForSomething(ShootVideoActivity_.class, BaseActivity.TAKE_VIDEO);
                break;
            case 14://后台录像
                if (FloatWindowManager.getInstance().applyOrShowFloatWindow(this) &&
                        PermissonUtil.checkPermission(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}))
                    startService(new Intent(this, TakeVideoService.class));
                break;
            case 15://邮件发送
                sendEmail();
                break;
            case 16://语音录制
                if (PermissonUtil.checkPermission(this, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE})) {
                    record();
                    if (btn_record.isShown())
                        btn_record.setVisibility(View.GONE);
                    else
                        btn_record.setVisibility(View.VISIBLE);
                }
                break;
            case 17://语音播放

                break;
            case 18://自定义快速拍照，摄像
                startActivity(new Intent(MainActivity.this,QuickCameraActivity_.class));
                break;
            case 25:
                uploadFile();
                break;

        }
    }

    //上传图片
    private void uploadFile() {
        UploadApi uploadApi= MRetrofite.getInstance().create(UploadApi.class);
        RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), new File(MyApplication.APP_BASE_DIR+"/test.gif"));
        MultipartBody.Part part = MultipartBody.Part.createFormData("pictureFile", "pictureFile", body);

        uploadApi.update("5fec43b4f8a34d6a9c0efed4953a6593","32345678",
                "000000000001","201805170001",part)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        LogUtils.logJson(s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.log(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(final String path){
        Snackbar.make(rcyl_test,"视频保存在："+path,Snackbar.LENGTH_LONG).setAction("查看", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileUtil.openDIr(getBaseContext(),path);
            }
        }).show();
    }

    /**
     * 语音录制
     */
    private void record() {
        if (mPop != null) {
            return;
        }
        final View view = View.inflate(this, com.yorhp.tyhjlibrary.R.layout.dialog_record, null);
        final ImageView mImageView = (ImageView) view.findViewById(R.id.zeffect_recordbutton_dialog_imageview);
        final TextView mTextView = (TextView) view.findViewById(R.id.zeffect_recordbutton_dialog_time_tv);
        mPop = new PopupWindowFactory(this, view);
        recordUtil = new RecordUtil();
        recordUtil.setOnAudioStatusUpdateListener(new RecordUtil.OnAudioStatusUpdateListener() {
            @Override
            public void onUpdate(double db, long time) {
                //根据分贝值来设置录音时话筒图标的上下波动，下面有讲解
                mImageView.getDrawable().setLevel((int) (3000 + 6000 * db / 100));
                mTextView.setText(TimeUtil.getTimeE(time).substring(14, 19));
            }

            @Override
            public void onStop(final String filePath) {
                Snackbar.make(rcyl_test, "语音保存路径为：" + filePath, Snackbar.LENGTH_SHORT).setAction("查看", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FileUtil.openDIr(getContext(),filePath);
                    }
                }).show();
            }
        });

    }

    float y;
    long time;
    RecordUtil recordUtil;

    @Touch
    void btn_record(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                y = event.getRawY();
                time = System.currentTimeMillis();
                mPop.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
                try {
                    recordUtil.startRecord(this);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    Snackbar.make(btn_record, "先允许调用系统录音权限", Snackbar.LENGTH_SHORT).show();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (System.currentTimeMillis() - time < 1000) {
                    Snackbar.make(btn_record, "录音时间过短，请重试", Snackbar.LENGTH_SHORT).show();
                    recordUtil.cancelRecord(this);
                    mPop.dismiss();
                    break;
                } else if (y - event.getRawY() > 300) {
                    Snackbar.make(btn_record, "已取消录制语音", Snackbar.LENGTH_SHORT).show();
                    recordUtil.cancelRecord(this);
                    mPop.dismiss();
                    break;
                } else {
                    try {
                        recordUtil.stopRecord(this);        //结束录音（保存录音文件）
                    } catch (Exception e) {
                        e.printStackTrace();
                        snkbar(btn_record, "先允许调用系统录音权限", Snackbar.LENGTH_SHORT);
                    }
                    mPop.dismiss();
                    break;
                }
            case MotionEvent.ACTION_CANCEL:
                recordUtil.cancelRecord(this); //取消录音（不保存录音文件）
                mPop.dismiss();
                break;
        }
    }

    /**
     * 控件剪裁
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void Outline() {
        if (!btn_outlin.isShown()) {
            btn_outlin.setVisibility(View.VISIBLE);
            btn_outlin.setClipToOutline(true);
            btn_outlin.setOutlineProvider(ImageUtil.getOutline(true, 10, 10));
        } else {
            btn_outlin.setVisibility(View.GONE);
        }
    }

    /**
     * 图片加载
     */
    private void showImage() {
        ImageView imageView;
        imageView = new ImageView(getContext());
        final MyDialog dialog = new MyDialog(getContext(), imageView, com.yorhp.tyhjlibrary.R.style.dialog2);
        dialog.setCancelable(true);
        dialog.show();
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Picasso.get().load("https://wallscover.com/images/fn-scarl-rifle-wallpaper-7.jpg").into(imageView);
    }

    /**
     * 数据库
     */
    private void showDatabase() {
        List<AppRecord> appRecordList = MLiteOrm.getInstance().query(AppRecord.class);
        if (appRecordList.size() > 0)
            toast("你之前一共打开该APP" + appRecordList.size() + "次，最后一次打开时间为" +
                    appRecordList.get(appRecordList.size() - 1).getLogTimeStr() +
                    "，使用时间为：" + appRecordList.get(appRecordList.size() - 1).getSpendTimeStr());
        else
            toast("未找到相关数据");
    }

    /**
     * 网络状态
     */
    private void internetStatus() {
        switch (InternetUtil.getNetWorkStatus(this)) {
            case InternetUtil.NETWORK_CLASS_4_G:
                toast("当前网络为：4G");
                break;
            case InternetUtil.NETWORK_CLASS_3_G:
                toast("当前网络为：3G");
                break;
            case InternetUtil.NETWORK_CLASS_2_G:
                toast("当前网络为：2G");
                break;
            case InternetUtil.NETWORK_WIFI:
                toast("当前网络为：WIFI");
                break;
            case InternetUtil.NETWORK_CLASS_UNKNOWN:
                toast("当前无网络");
                break;
            default:
                toast("当前网络为：" + InternetUtil.getNetWorkStatus(this));
        }
    }


    /**
     * 发送邮件
     */
    private void sendEmail() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_email_address, null);
        final EditText et_email_address = view.findViewById(R.id.et_email_address);
        Button btn_send_email = view.findViewById(R.id.btn_send_email);
        final Dialog dialogEmail = DialogUtil.Dialog(this, true, view);
        btn_send_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailAdress = et_email_address.getText().toString().trim();
                if (FormatUtil.isEmail(emailAdress)) {
                    dialogEmail.dismiss();
                    EmailUtil.sendEmail(emailAdress, "嗨，你好呀，" + phoneUtil.getModel());
                    snkbar(rcyl_test, "已发送邮件至：" + emailAdress, Snackbar.LENGTH_SHORT);
                } else {
                    snkbar(rcyl_test, "请输入正确的邮箱", Snackbar.LENGTH_SHORT);
                }
            }
        });
    }


    @Override
    protected void takeVideo(String videoPath) {
        snkbar(rcyl_test, "视频保存地址为：" + videoPath, Snackbar.LENGTH_LONG);
    }

    @Override
    protected CameraUtil.CamerabakListener setCamerabakListener() {
        return new CameraUtil.CamerabakListener() {
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

    @Background
    void savaRecord() {
        AppRecord appRecord = new AppRecord(logTime, System.currentTimeMillis());
        MLiteOrm.getInstance().save(appRecord);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        savaRecord();
    }

    long lastClickTime = 0;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - lastClickTime > 2000) {
            lastClickTime = System.currentTimeMillis();
            toast("再次点击退出");
            return;
        }
        //MyApplication.closeAllActivity();
        moveTaskToBack(true);
    }


}
