package com.yorhp.tyhjlibrary.view.recycleView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;

/**
 * Created by Tyhj on 2018/4/2.
 */

public class MRecycleView extends RecyclerView {

    public RecycleViewEventListener recycleViewEventListener;

    public MRecycleView(Context context) {
        super(context);
    }

    public MRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    public void setLayoutManager(int LayoutManagerType) {
        switch (LayoutManagerType) {
            case 0:
                setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                break;
            case 1:
                setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                break;
        }
    }


    //添加滑动到底部的监听
    public void initRecycleView() {
        this.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
                //全部item的数量
                int totalItemCount = recyclerView.getAdapter().getItemCount();
                //最后一个可见item的position
                int lastVisibleItemPosition;
                //可见item的数量
                int visibleItemCount = recyclerView.getChildCount();
                LayoutManager layoutManager = getLayoutManager();

                if (layoutManager instanceof GridLayoutManager) {
                    lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                    int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                    ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
                    lastVisibleItemPosition = findMax(into);
                } else {
                    lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                }

                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItemPosition == totalItemCount - 1
                        && totalItemCount>5&&totalItemCount>visibleItemCount) {
                    if (recycleViewEventListener != null) {
                        recycleViewEventListener.onScollToBottom();
                    }
                }
            }
        });
    }

    /**
     * 设置监听事件
     *
     * @param recycleViewEventListener
     */
    public void setRecycleViewEventListener(RecycleViewEventListener recycleViewEventListener) {
        this.recycleViewEventListener = recycleViewEventListener;
    }


    public void init(Adapter adapter, int LayoutManagerType) {
        setLayoutManager(LayoutManagerType);
        setAdapter(adapter);
        initRecycleView();
    }


    public interface RecycleViewEventListener {
        void onScollToBottom();
    }


    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }



}
