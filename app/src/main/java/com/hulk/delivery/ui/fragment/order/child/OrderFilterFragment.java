package com.hulk.delivery.ui.fragment.order.child;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hulk.delivery.R;

import me.yokeyword.eventbusactivityscope.EventBusActivityScope;
import me.yokeyword.fragmentation.SupportFragment;


/**
 * Created by hulk-out on 2017/9/8.
 */

public class OrderFilterFragment extends SupportFragment {

    private View view;
    private static final String TAG = "OrderFilterFragment";

    public OrderFilterFragment() {
        // Required empty public constructor
    }

    public static OrderFilterFragment newInstance() {

        Bundle args = new Bundle();
        OrderFilterFragment fragment = new OrderFilterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.item_filter, container, false);
        initView();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    //初始化页面
    private void initView() {

    }

}
