package com.hulk.delivery.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hulk.delivery.R;

/**
 * Created by hulk-out on 2018/3/16.
 */

public class BannerViewHolder extends RecyclerView.ViewHolder {
    ImageView bannerImg;
    TextView bannerTitle;

    public BannerViewHolder(View itemView) {
        super(itemView);
        bannerImg = itemView.findViewById(R.id.banner_img);
        bannerTitle = itemView.findViewById(R.id.banner_title);
    }

}
