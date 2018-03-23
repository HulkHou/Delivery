package com.hulk.delivery.ui.fragment.order.child;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.alibaba.android.vlayout.layout.StickyLayoutHelper;
import com.hulk.delivery.R;
import com.hulk.delivery.adapter.MainViewHolder;
import com.hulk.delivery.adapter.OrderHomeFragmentAdapter;
import com.hulk.delivery.adapter.SubAdapter;
import com.hulk.delivery.util.ScreenUtil;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by hulk-out on 2018/3/19.
 */
public class OrderHomeFragment extends SupportFragment {
    private DrawerLayout mDrawerLayout;
    private RecyclerView recyclerView;
    private FrameLayout mDrawerContent;
    private TabLayout mTab;
    private ViewPager mViewPager;

    private DelegateAdapter adapter;
    private SubAdapter adapter_search;
    private SubAdapter adapter_address;

    public static OrderHomeFragment newInstance() {

        Bundle args = new Bundle();

        OrderHomeFragment fragment = new OrderHomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_home, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mDrawerLayout = view.findViewById(R.id.main_layout);
        recyclerView = view.findViewById(R.id.order_home_view);
        mDrawerContent = view.findViewById(R.id.drawer_filter_content);
        mTab = view.findViewById(R.id.order_tab);
        mViewPager = view.findViewById(R.id.order_viewPager);

        VirtualLayoutManager manager = new VirtualLayoutManager(_mActivity);
        recyclerView.setLayoutManager(manager);
        adapter = new DelegateAdapter(manager, false);

        if (findChildFragment(OrderFilterFragment.class) == null) {
            loadRootFragment(R.id.drawer_filter_content, OrderFilterFragment.newInstance());
        }
        //搜索栏
        StickyLayoutHelper stickyLayoutHelper = new StickyLayoutHelper();
        adapter_search = new SubAdapter(_mActivity, stickyLayoutHelper, 1,
                new VirtualLayoutManager
                        .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ScreenUtil.dip2px(_mActivity, 50))) {
            @Override
            public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new MainViewHolder(LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.item_search, parent, false));
            }

            @Override
            public void onBindViewHolder(MainViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                ImageView filter = holder.itemView.findViewById(R.id.filter);
                filter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDrawerLayout.openDrawer(mDrawerContent);
                    }
                });
            }
        };
        adapter.addAdapter(adapter_search);

        //地址栏
        SingleLayoutHelper addressLayoutHelper = new SingleLayoutHelper();
        adapter_address = new SubAdapter(_mActivity, addressLayoutHelper, 1) {
            @Override
            public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new MainViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_address, parent, false));
            }

            @Override
            public void onBindViewHolder(MainViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                final TextView addressDesc = holder.itemView.findViewById(R.id.address_desc);
                final LinearLayout linearLayoutAddress = holder.itemView.findViewById(R.id.ll_address);
                addressDesc.setText(R.string.address);
                linearLayoutAddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        start(OrderAddressFragment.newInstance());
                    }
                });

            }
        };
        adapter.addAdapter(adapter_address);

        recyclerView.setAdapter(adapter);

        mTab.addTab(mTab.newTab());
        mTab.addTab(mTab.newTab());

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        mViewPager.setAdapter(new OrderHomeFragmentAdapter(getChildFragmentManager(),
                getString(R.string.shop_title), getString(R.string.food_title)));
        mTab.setupWithViewPager(mViewPager);
    }

}