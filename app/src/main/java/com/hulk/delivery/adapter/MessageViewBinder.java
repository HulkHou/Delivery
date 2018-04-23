package com.hulk.delivery.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hulk.delivery.R;
import com.hulk.delivery.entity.TMessage;
import com.hulk.delivery.event.Event;
import com.hulk.delivery.ui.fragment.profile.child.MessageDetailFragment;
import com.hulk.delivery.ui.fragment.profile.child.MessageFragment;

import java.util.List;

import me.drakeet.multitype.ItemViewBinder;
import me.yokeyword.eventbusactivityscope.EventBusActivityScope;

/**
 * Created by hulk-out on 2018/4/16.
 */

public class MessageViewBinder extends ItemViewBinder<TMessage, MessageViewBinder.ViewHolder> {

    MessageFragment messageFragment;
    List<TMessage> messageList;
    private Event.MessageInfoEvent messageInfoEvent;

    public MessageViewBinder(List<TMessage> messageList, MessageFragment messageFragment) {
        this.messageList = messageList;
        this.messageFragment = messageFragment;
    }

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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    messageInfoEvent = new Event.MessageInfoEvent();
                    messageInfoEvent.tMessage = messageList.get(getAdapterPosition());
                    EventBusActivityScope.getDefault(messageFragment.getActivity()).postSticky(messageInfoEvent);
                    messageFragment.start(MessageDetailFragment.newInstance());
                }
            });
        }
    }
}
