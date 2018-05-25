package com.hulk.delivery.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hulk.delivery.ui.fragment.order.child.OrderFoodFragment;
import com.hulk.delivery.ui.fragment.order.child.OrderShopFragment;
import com.hulk.delivery.ui.fragment.profile.child.CollectionShopFragment;

/**
 * Created by hulk-out on 2018/3/19.
 */
public class CollectionFragmentAdapter extends FragmentPagerAdapter {
    private String[] mTitles;

    public CollectionFragmentAdapter(FragmentManager fm, String... titles) {
        super(fm);
        mTitles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return CollectionShopFragment.newInstance();
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
