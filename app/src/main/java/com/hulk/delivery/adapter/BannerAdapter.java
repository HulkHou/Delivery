package com.hulk.delivery.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hulk.delivery.R;
import com.hulk.delivery.entity.TCollect;
import com.hulk.delivery.ui.fragment.order.child.OrderShopFragment;
import com.hulk.delivery.ui.fragment.order.child.ShopDetailFragment;

import java.util.List;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by hulk-out on 2018/3/16.
 */

public class BannerAdapter extends RecyclerView.Adapter<BannerViewHolder> {

    Context context;
    OrderShopFragment orderShopFragment;
    List<TCollect> collectShopList;
    private int mCount = 0;

    public BannerAdapter(Context context, OrderShopFragment orderShopFragment, List<TCollect> collectShopList) {
        this.context = context;
        this.orderShopFragment = orderShopFragment;
        this.collectShopList = collectShopList;
        mCount = collectShopList.size();
    }

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
        holder.bannerTitle.setText(collectShopList.get(position).getShopName());
        holder.mRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SupportFragment) orderShopFragment.getParentFragment()).start(ShopDetailFragment.newInstance());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCount;
    }

}
