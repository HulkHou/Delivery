package com.hulk.delivery.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.hulk.delivery.R;
import com.hulk.delivery.util.ScreenUtil;

/**
 * Created by hulk-out on 2018/3/16.
 */

public abstract class TitleAdapter extends SubAdapter {

    public TitleAdapter(Context context, LayoutHelper layoutHelper) {
        super(context, layoutHelper, 1);
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_title, parent, false));
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        holder.itemView.setLayoutParams(
                new VirtualLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtil.dip2px(mContext, 45)));
        TextView textView = holder.itemView.findViewById(R.id.title);
        textView.setText(getText());
        Drawable drawableLeft = mContext.getResources().getDrawable(getDrawables()[0]);
//        Drawable drawableRight = mContext.getResources().getDrawable(getDrawables()[1]);
        drawableLeft.setBounds(0, 0, drawableLeft.getMinimumWidth(), drawableLeft.getMinimumHeight());
//        drawableRight.setBounds(0, 0, drawableRight.getMinimumWidth(), drawableRight.getMinimumHeight());
//        textView.setCompoundDrawables(drawableLeft, null, drawableRight, null);
        textView.setCompoundDrawables(drawableLeft, null, null, null);
    }

    protected abstract String getText();

    protected abstract int[] getDrawables();
}
