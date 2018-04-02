package com.yorhp.tyhjlibrary.view.recycleView;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.yorhp.tyhjlibrary.app.MyApplication;

/**
 * Created by Tyhj on 2018/4/2.
 */

public class BaseRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public void update() {
        MyApplication.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }


    public void addOne(final int positon){
        MyApplication.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyItemInserted(positon);
            }
        });
    }

    public void deleteOne(final int positon){
        MyApplication.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyItemRemoved(positon);
            }
        });
    }





}
