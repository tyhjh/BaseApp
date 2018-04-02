package com.yorhp.tyhjlibrary.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.View;

/**
 * Created by Tyhj on 2018/4/2.
 */

public class DialogUtil {

    public static Dialog Dialog(Context context, boolean cancelable, View view){
        AlertDialog.Builder di;
        di = new AlertDialog.Builder(context);
        di.setCancelable(cancelable);
        di.setView(view);
        return di.show();
    }

}
