package com.hulk.delivery.ui.fragment.profile.child;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hulk.delivery.R;
import com.hulk.delivery.ui.fragment.login.LoginByPasswordFragment;
import com.hulk.delivery.ui.fragment.management.child.ManagementHomeFragment;
import com.hulk.delivery.ui.fragment.order.child.OrderAddressFragment;
import com.hulk.delivery.ui.fragment.profile.ProfileFragment;
import com.hulk.delivery.util.LoginUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by hulk-out on 2017/9/8.
 */

public class ProfileHomeFragment extends SupportFragment {

    private View view;
    private static final String TAG = "ProfileHomeFragment";

    //名字
    @BindView(R.id.tv_profile_name)
    TextView profileName;


    public ProfileHomeFragment() {
        // Required empty public constructor
    }

    public static ProfileHomeFragment newInstance() {

        Bundle args = new Bundle();
        ProfileHomeFragment fragment = new ProfileHomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.profile_frag_home, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    //订单管理
    @OnClick(R.id.rl_profile_orders_management)
    public void orders(View view) {
        if (LoginUtil.checkLogin(_mActivity)) {
            start(ManagementHomeFragment.newInstance());
        } else {
            start(LoginByPasswordFragment.newInstance());
        }
    }

    //收藏管理
    @OnClick(R.id.rl_profile_collection_management)
    public void collection(View view) {
        if (LoginUtil.checkLogin(_mActivity)) {
            start(CollectionFragment.newInstance());
        } else {
            start(LoginByPasswordFragment.newInstance());
        }
    }

    //地址管理
    @OnClick(R.id.rl_profile_address_management)
    public void address(View view) {
        if (LoginUtil.checkLogin(_mActivity)) {
            start(OrderAddressFragment.newInstance());
        } else {
            start(LoginByPasswordFragment.newInstance());
        }
    }

    //设置
    @OnClick(R.id.rl_profile_setting_management)
    public void setting(View view) {
        if (LoginUtil.checkLogin(_mActivity)) {
            start(SettingFragment.newInstance());
        } else {
            start(LoginByPasswordFragment.newInstance());
        }
    }

    //密码管理
    @OnClick(R.id.rl_profile_password_management)
    public void password(View view) {
        if (LoginUtil.checkLogin(_mActivity)) {
            start(PasswordFragment.newInstance());
        } else {
            start(LoginByPasswordFragment.newInstance());
        }
    }

    //消息管理
    @OnClick(R.id.rl_profile_message_management)
    public void message(View view) {
        if (LoginUtil.checkLogin(_mActivity)) {
            start(MessageFragment.newInstance());
        } else {
            start(LoginByPasswordFragment.newInstance());
        }
    }

    //当Fragment可见时调用此方法
    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
    }

    @Override
    public boolean onBackPressedSupport() {
        // 这里实际项目中推荐使用 EventBus接耦
        ((ProfileFragment) getParentFragment()).onBackToFirstFragment();
        return true;
    }
}