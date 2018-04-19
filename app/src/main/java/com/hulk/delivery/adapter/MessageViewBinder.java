package com.hulk.delivery.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hulk.delivery.R;
import com.hulk.delivery.entity.TMessage;

import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by hulk-out on 2018/4/16.
 */

public class MessageViewBinder extends ItemViewBinder<TMessage, MessageViewBinder.ViewHolder> {
    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_profile_message, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull TMessage tMessage) {
        holder.messageTitle.setText(tMessage.getMessageTitle());
        holder.messageContent.setText(tMessage.getMessageContent());
        holder.messageDate.setText(tMessage.getCreateTime().toString());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView messageTitle;
        private final TextView messageContent;
        private final TextView messageDate;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.messageTitle = itemView.findViewById(R.id.tv_item_profile_message_title);
            this.messageContent = itemView.findViewById(R.id.tv_item_profile_message_content);
            this.messageDate = itemView.findViewById(R.id.tv_item_profile_message_date);
        }
    }
}
