package com.hulk.delivery.ui.fragment.profile.child;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hulk.delivery.R;
import com.hulk.delivery.ui.fragment.cart.CartFragment;
import com.hulk.delivery.ui.fragment.profile.ProfileFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
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