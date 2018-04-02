package com.yorhp.tyhjlibrary.app;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.yorhp.tyhjlibrary.view.toast.ToastUtil;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;


/**
 * Created by Tyhj on 2018/3/31.
 */

@EFragment
public class BaseFragement extends Fragment{

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @UiThread
    protected void toast(String msg) {
        ToastUtil.toast(getActivity(),msg);
    }

    @UiThread
    protected void lvBack(Class activity) {
        startActivity(new Intent(getActivity(), activity));
    }

    @UiThread
    protected void lvNoback(Class activity) {
        startActivity(new Intent(getActivity(), activity));
        getActivity().finish();
    }

    @UiThread
    protected void lvForever(Class activity) {
        startActivity(new Intent(getActivity(), activity));
        MyApplication.closeAllActivity();
    }

    @UiThread
    protected void snkbar(View view, String msg, int time) {
        Snackbar.make(view, msg, time).show();
    }
    
}
