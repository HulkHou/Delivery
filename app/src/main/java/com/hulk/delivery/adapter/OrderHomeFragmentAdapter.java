package com.hulk.delivery.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hulk.delivery.ui.fragment.order.child.OrderFoodFragment;
import com.hulk.delivery.ui.fragment.order.child.OrderShopFragment;

/**
 * Created by hulk-out on 2018/3/19.
 */
public class OrderHomeFragmentAdapter extends FragmentPagerAdapter {
    private String[] mTitles;

    public OrderHomeFragmentAdapter(FragmentManager fm, String... titles) {
        super(fm);
        mTitles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return OrderShopFragment.newInstance();
        } else {
            return OrderFoodFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
