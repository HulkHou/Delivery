package com.hulk.delivery.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.hulk.delivery.R;

/**
 * Created by hulk-out on 2018/3/16.
 */

public class BannerViewHolder extends RecyclerView.ViewHolder {
    ImageView headimg;

    public BannerViewHolder(View itemView) {
        super(itemView);
        headimg = itemView.findViewById(R.id.headimg);

    }

}
