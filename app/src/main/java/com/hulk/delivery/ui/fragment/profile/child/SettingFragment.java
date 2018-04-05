package com.hulk.delivery.ui.fragment.profile.child;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.hulk.delivery.R;
import com.hulk.delivery.util.LoginUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.yokeyword.fragmentation.SupportFragment;


/**
 * Created by hulk-out on 2017/9/8.
 */

public class SettingFragment extends SupportFragment {

    private View view;
    private MaterialDialog.Builder builder;
    private MaterialDialog dialog;
    private static final String TAG = "SettingFragment";


    @BindView(R.id.tv_profile_setting_cache)
    TextView settingCache;

    public SettingFragment() {
        // Required empty public constructor
    }

    public static SettingFragment newInstance() {

        Bundle args = new Bundle();
        SettingFragment fragment = new SettingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.profile_frag_setting, container, false);
        ButterKnife.bind(this, view);
        initToolbar();
        return view;
    }

    //初始化工具栏
    private void initToolbar() {

    }

    @OnClick(R.id.btn_setting_logout)
    public void settingLogout(View view) {
        LoginUtil.loginOut(_mActivity);
        Toast.makeText(getActivity(), R.string.logoutSuccess, Toast.LENGTH_SHORT).show();
        _mActivity.onBackPressed();
    }

    //当Fragment可见时调用此方法
    @Override
    public void onSupportVisible() {
        initToolbar();
    }

    @Override
    public boolean onBackPressedSupport() {
        pop();
        return true;
    }

}
