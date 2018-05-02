package com.yorhp.tyhjlibrary.app;

import android.app.Activity;
import android.app.Application;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;

import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;
import com.yorhp.tyhjlibrary.R;
import com.yorhp.tyhjlibrary.util.common.ThreadPoolUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Tyhj on 2018/3/31.
 */

public class MyApplication extends Application {

    public static final String APP_NAME = "base_app_library";
    //手机品牌
    public static int PHONERAND = 0;

    public static String APP_BASE_DIR = "";
    public static ArrayList<Activity> activities = new ArrayList<>();
    public static final boolean ISDEBUG = true;

    private static MyApplication instance;

    private Handler handler;
    private ThreadPoolUtils mThreadPoolUtils;


    //API
    public static String BASEURL = "http://121.46.30.200:10081/mlstudio/";


    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler(Looper.getMainLooper());
        mThreadPoolUtils = new ThreadPoolUtils(ThreadPoolUtils.CachedThread, 5);
        instance = this;
        initDir();
        initPicasso();
    }


    /**
     * 初始化Picasso
     */
    private void initPicasso() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Picasso picasso = new Picasso.Builder(getApplicationContext())
                        .memoryCache(new LruCache(10 << 20))//设置内存缓存大小10M
                        //.indicatorsEnabled(false) //设置左上角标记，主要用于测试
                        .build();
                Picasso.setSingletonInstance(picasso);
            }
        }).start();
    }

    /**
     * 文件夹初始化
     */
    public void initDir() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                File dir = new File(Environment.getExternalStorageDirectory() + "/" + getApplicationContext().getResources().getString(R.string.app_name));
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                APP_BASE_DIR = dir.getAbsolutePath();
            }
        }).start();
    }


    /**
     * 关闭界面
     */
    public static void closeAllActivity() {
        for (int i = 0; i < activities.size(); i++) {
            activities.get(i).finish();
        }
    }


    /**
     * 主线程跑东西
     *
     * @param runnable
     */
    public void runOnUiThread(Runnable runnable) {
        handler.post(runnable);
    }

    public static ThreadPoolUtils getThreadPoolUtils() {
        MyApplication application = (MyApplication) MyApplication.getInstance().getApplicationContext();
        return application.mThreadPoolUtils;
    }


    public static MyApplication getInstance() {
        return instance;
    }

//
//                               _oo0oo_
//                             o8888888o
//                              88" . "88
//                               (|  -_-  |)
//                              0\  =  /0
//                            ___/`---'\___
//                          .' \\|          |// '.
//                        / \\|||     :    |||// \
//                      / _ |||||    -:-   ||||| - \
//                     |   | \\\     -    /// |   |
//                     | \_ |  ''\ --- /''  |_ / |
//                     \  .- \__  '-'  ___/-. /
//                     ___'. .'  /--.--\  `. .'___
//              ."" '<  `.___\_<|>_/___.' >' "".
//            | | :  `- \ `.;` \     _    / `;.` / - ` : | |
//            \  \   `_.   \_  __\ /__  _/   .-`  /  /
// =====`-.____`.___ \_____/___.-`___.-'=====
//                             `=---='
//
//
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//
//               佛祖保佑         永无BUG
//
//
//

}
