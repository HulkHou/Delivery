package com.hulk.delivery.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.hulk.delivery.R;

/**
 * Created by hulk-out on 2018/3/16.
 */

public class BannerAdapter extends RecyclerView.Adapter<BannerViewHolder> {

    Context context;

    public BannerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public BannerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BannerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_collection_inside, parent, false));
    }

    @Override
    public void onBindViewHolder(BannerViewHolder holder, int position) {
        holder.itemView.setTag(position);
        holder.bannerTitle.setText(context.getResources().getStringArray(R.array.chiyidun)[position]);

    }

    @Override
    public int getItemCount() {
        return context.getResources().getStringArray(R.array.chiyidun).length;
    }

}
