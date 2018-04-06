package com.yorhp.baseapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.yorhp.baseapp.R;
import com.yorhp.tyhjlibrary.view.recycleView.BaseRecyclerAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by Tyhj on 2018/4/2.
 */

public class StringAdapter extends BaseRecyclerAdapter {

    int lastPosition = 0;

    ArrayList<String> arrayList;
    Context context;
    LayoutInflater inflater;

    public StringAdapter(ArrayList<String> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public StringHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_string, parent, false);
        return new StringHolder(view);
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final StringHolder stringHolder = (StringHolder) holder;
        lastPosition = position;
        stringHolder.textView.setText(arrayList.get(holder.getPosition()));

        if(position<15){
            stringHolder.checkBox.setChecked(true);
        }else {
            stringHolder.checkBox.setChecked(false);
        }

        stringHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new Integer(holder.getPosition()));
            }
        });
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    class StringHolder extends RecyclerView.ViewHolder {
        TextView textView;
        CheckBox checkBox;
        public StringHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_name);
            checkBox=itemView.findViewById(R.id.ckb);
        }
    }
}
