package com.yorhp.tyhjlibrary.view.permisson;

import android.app.Activity;

/**
 * Created by Tyhj on 2018/4/2.
 */

public class PermissonUtil {
    public static final int PERMISSIONS_REQUEST_CODE = 0; // 请求码
    public static final int TAKE_PHOTO = 1;
    public static final int CROP_PHOTO = 2;
    public static final int PICK_PHOTO = 3;



    public static PermissionsChecker mPermissionsChecker; // 权限检测器


    public static boolean checkPermission(Activity activity, String... permissions){
        mPermissionsChecker=new PermissionsChecker(activity);
        // 缺少权限时, 进入权限配置页面
        if (permissions!=null&&mPermissionsChecker.lacksPermissions(permissions)) {
            PermissionsActivity.startActivityForResult(activity, PERMISSIONS_REQUEST_CODE, permissions);
            activity.overridePendingTransition(0, 0);
            return false;
        }
        return true;
    }



}
