package com.hulk.delivery.ui.fragment.management.child;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hulk.delivery.R;
import com.hulk.delivery.adapter.ManagementAdapter;
import com.hulk.delivery.entity.Videos;
import com.hulk.delivery.ui.fragment.cart.CartFragment;
import com.hulk.delivery.ui.fragment.management.ManagementFragment;
import com.hulk.delivery.util.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by hulk-out on 2018/3/19.
 */
public class ManagementHomeFragment extends SupportFragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private Handler handler = new Handler();

    private List<Videos> videosList = new ArrayList<>();

    public static ManagementHomeFragment newInstance() {

        Bundle args = new Bundle();
        ManagementHomeFragment fragment = new ManagementHomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_management_home, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
//RecyclerView的初始化
        mRecyclerView = view.findViewById(R.id.rcv_management);
        //创建线性LinearLayoutManager
        mLayoutManager = new LinearLayoutManager(getActivity());
        //设置LayoutManager
        mRecyclerView.setLayoutManager(mLayoutManager);
        //设置item的动画，可以不设置
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //设置item的分割线
        mRecyclerView.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.VERTICAL));
//        mRecyclerView.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.VERTICAL, R.drawable.divider_mileage));
        ManagementAdapter adapter = new ManagementAdapter(initData());


        //下拉刷新
        mSwipeRefreshLayout = view.findViewById(R.id.SwipeRefreshLayout_management);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.text_blue_color);

        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
//                mSwipeRefreshLayout.setRefreshing(true);
//                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        videosList.clear();
                        initData();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });

        //设置Adapter
        mRecyclerView.setAdapter(adapter);
    }

    private List<Videos> initData() {
        videosList.add(new Videos("1024"));
        videosList.add(new Videos("1025"));
        videosList.add(new Videos("1026"));
        videosList.add(new Videos("1027"));
        videosList.add(new Videos("1028"));
        videosList.add(new Videos("1029"));
        videosList.add(new Videos("1030"));
        videosList.add(new Videos("1031"));
        videosList.add(new Videos("1032"));
        videosList.add(new Videos("1033"));
        videosList.add(new Videos("1034"));
        videosList.add(new Videos("1035"));
        videosList.add(new Videos("1036"));
        videosList.add(new Videos("1037"));
        videosList.add(new Videos("1038"));
        videosList.add(new Videos("1039"));
        return videosList;
    }

    @Override
    public boolean onBackPressedSupport() {
        // 这里实际项目中推荐使用 EventBus接耦
        ((ManagementFragment) getParentFragment()).onBackToFirstFragment();
        return true;
    }

}