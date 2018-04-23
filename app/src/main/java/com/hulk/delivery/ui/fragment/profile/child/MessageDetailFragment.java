package com.hulk.delivery.ui.fragment.profile.child;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hulk.delivery.R;
import com.hulk.delivery.event.Event;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.yokeyword.eventbusactivityscope.EventBusActivityScope;
import me.yokeyword.fragmentation.SupportFragment;


/**
 * Created by hulk-out on 2017/9/8.
 */

public class MessageDetailFragment extends SupportFragment {

    private View view;
    private static final String TAG = "MessageDetailFragment";

    private String messageTitleValue;
    private String messageDateValue;
    private String messageContentValue;

    @BindView(R.id.tv_profile_message_detail_title)
    TextView messageTitle;

    @BindView(R.id.tv_profile_message_detail_date)
    TextView messageDate;

    @BindView(R.id.tv_profile_message_detail_content)
    TextView messageContent;

    public MessageDetailFragment() {
        // Required empty public constructor
    }

    public static MessageDetailFragment newInstance() {

        Bundle args = new Bundle();
        MessageDetailFragment fragment = new MessageDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.profile_frag_message_detail, container, false);
        ButterKnife.bind(this, view);
        EventBusActivityScope.getDefault(_mActivity).register(this);
        initToolbar();
        initViews();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBusActivityScope.getDefault(_mActivity).unregister(this);
    }

    //初始化工具栏
    private void initToolbar() {

    }

    /**
     * 获取MessageDetail事件
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageInfoEvent(Event.MessageInfoEvent event) {
        if (event != null) {
            messageTitleValue = event.tMessage.getMessageTitle();
            messageDateValue = event.tMessage.getCreateTime();
            messageContentValue = event.tMessage.getMessageContent();
        }
    }

    //初始化页面元素
    private void initViews() {
        messageTitle.setText(messageTitleValue);
        messageDate.setText(messageDateValue);
        messageContent.setText(messageContentValue);
    }

    //当Fragment可见时调用此方法
    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        initToolbar();
    }

    //当Fragment不可见时调用此方法
    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
    }

    @Override
    public boolean onBackPressedSupport() {
        pop();
        return true;
    }
}
