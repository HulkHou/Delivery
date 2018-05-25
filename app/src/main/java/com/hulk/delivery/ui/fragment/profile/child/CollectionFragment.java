package com.hulk.delivery.ui.fragment.profile.child;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hulk.delivery.R;
import com.hulk.delivery.adapter.CollectionFragmentAdapter;
import com.hulk.delivery.util.RxLifecycleUtils;
import com.uber.autodispose.AutoDisposeConverter;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by hulk-out on 2018/3/19.
 */
public class CollectionFragment extends SupportFragment {
    private TabLayout mTab;
    private ViewPager mViewPager;

    public static CollectionFragment newInstance() {
        Bundle args = new Bundle();
        CollectionFragment fragment = new CollectionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_frag_collection, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mTab = view.findViewById(R.id.collection_tab);
        mViewPager = view.findViewById(R.id.collection_viewPager);
        mTab.addTab(mTab.newTab());
        mTab.addTab(mTab.newTab());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        mViewPager.setAdapter(new CollectionFragmentAdapter(getChildFragmentManager(),
                getString(R.string.shop_title), getString(R.string.food_title)));
        mTab.setupWithViewPager(mViewPager);
    }

    @Override
    public boolean onBackPressedSupport() {
        pop();
        return true;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
    }

    protected <T> AutoDisposeConverter<T> bindLifecycle() {
        return RxLifecycleUtils.bindLifecycle(this);
    }
}