package com.hulk.delivery.ui.fragment.order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hulk.delivery.R;
import com.hulk.delivery.base.BaseMainFragment;
import com.hulk.delivery.ui.fragment.order.child.OrderHomeFragment;


/**
 * Created by hulk-out on 2018/3/14.
 */
public class OrderFragment extends BaseMainFragment {

    public static OrderFragment newInstance() {

        Bundle args = new Bundle();

        OrderFragment fragment = new OrderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_frag, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (findChildFragment(OrderHomeFragment.class) == null) {
            loadRootFragment(R.id.fl_order_container, OrderHomeFragment.newInstance());
        }
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);

        if (findChildFragment(OrderHomeFragment.class) == null) {
            loadRootFragment(R.id.fl_order_container, OrderHomeFragment.newInstance());
        }
    }

}
