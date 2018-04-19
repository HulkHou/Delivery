package com.hulk.delivery.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hulk.delivery.R;
import com.hulk.delivery.entity.TOrder;

import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by hulk-out on 2018/4/16.
 */

public class OrderManagementViewBinder extends ItemViewBinder<TOrder, OrderManagementViewBinder.ViewHolder> {
    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_management, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull TOrder tOrder) {
        holder.orderSn.setText(tOrder.getOrderSn());
        holder.shopName.setText(tOrder.getShopName());
        holder.shopPhone.setText(tOrder.getShopMobile());
        holder.shippingTime.setText(tOrder.getShippingTime());
        holder.shippingAddress.setText(tOrder.getConsignee());
        holder.orderAmount.setText(tOrder.getOrderAmount().toString());
        holder.orderStatus.setText(tOrder.getOrderStatus().toString());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @NonNull
        private final CardView cardViewManagement;
        private final TextView orderSn;
        private final TextView shopName;
        private final TextView shopPhone;
        private final TextView shippingTime;
        private final TextView shippingAddress;
        private final TextView orderAmount;
        private final TextView orderStatus;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.cardViewManagement = itemView.findViewById(R.id.card_view_management);
            this.orderSn = itemView.findViewById(R.id.order_sn);
            this.shopName = itemView.findViewById(R.id.shop_name);
            this.shopPhone = itemView.findViewById(R.id.shop_phone);
            this.shippingTime = itemView.findViewById(R.id.shipping_time);
            this.shippingAddress = itemView.findViewById(R.id.shipping_address);
            this.orderAmount = itemView.findViewById(R.id.order_amount);
            this.orderStatus = itemView.findViewById(R.id.order_status);

            cardViewManagement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
