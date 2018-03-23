package com.hulk.delivery.ui.fragment.management;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hulk.delivery.R;
import com.hulk.delivery.base.BaseMainFragment;
import com.hulk.delivery.ui.fragment.management.child.ManagementHomeFragment;

/**
 * Created by hulk-out on 2017/9/8.
 */

public class ManagementFragment extends BaseMainFragment {

    private View mView;

    public ManagementFragment() {
        // Required empty public constructor
    }

    public static ManagementFragment newInstance() {

        Bundle args = new Bundle();
        ManagementFragment fragment = new ManagementFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_management, container, false);
        return mView;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);

        if (findChildFragment(ManagementHomeFragment.class) == null) {
            loadRootFragment(R.id.fl_management_container, ManagementHomeFragment.newInstance());
        }
    }

    public void onBackToFirstFragment() {
        _mBackToFirstListener.onBackToFirstFragment();
    }
}
