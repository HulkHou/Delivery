package com.hulk.delivery;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.hulk.delivery.base.BaseMainFragment;
import com.hulk.delivery.event.TabSelectedEvent;
import com.hulk.delivery.ui.fragment.cart.CartFragment;
import com.hulk.delivery.ui.fragment.cart.child.CartHomeFragment;
import com.hulk.delivery.ui.fragment.management.ManagementFragment;
import com.hulk.delivery.ui.fragment.management.child.ManagementHomeFragment;
import com.hulk.delivery.ui.fragment.order.OrderFragment;
import com.hulk.delivery.ui.fragment.order.child.OrderHomeFragment;
import com.hulk.delivery.ui.fragment.profile.ProfileFragment;
import com.hulk.delivery.ui.fragment.profile.child.ProfileHomeFragment;

import me.yokeyword.eventbusactivityscope.EventBusActivityScope;
import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.SupportFragment;

public class MainActivity extends SupportActivity implements BaseMainFragment.OnBackToFirstListener, BottomNavigationBar.OnTabSelectedListener {

    private static final String TAG = "MainActivity";
    public static final int FIRST = 0;
    public static final int SECOND = 1;
    public static final int THIRD = 2;
    public static final int FOURTH = 3;

    private SupportFragment[] mFragments = new SupportFragment[4];

    private BottomNavigationBar bottomNavigationBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportFragment firstFragment = findFragment(OrderFragment.class);
        if (firstFragment == null) {
            mFragments[FIRST] = OrderFragment.newInstance();
            mFragments[SECOND] = CartFragment.newInstance();
            mFragments[THIRD] = ManagementFragment.newInstance();
            mFragments[FOURTH] = ProfileFragment.newInstance();

            loadMultipleRootFragment(R.id.fl_container, FIRST,
                    mFragments[FIRST],
                    mFragments[SECOND],
                    mFragments[THIRD],
                    mFragments[FOURTH]);
        } else {
            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题

            // 这里我们需要拿到mFragments的引用
            mFragments[FIRST] = firstFragment;
            mFragments[SECOND] = findFragment(CartFragment.class);
            mFragments[THIRD] = findFragment(ManagementFragment.class);
            mFragments[FOURTH] = findFragment(ProfileFragment.class);
        }

        initView();
        initBottomNavigationBar();
    }

    private void initView() {

    }

    //初始化底部导航条
    private void initBottomNavigationBar() {

        bottomNavigationBar = findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar.setTabSelectedListener(this);

        /**
         * 设置模式
         * 1、BottomNavigationBar.MODE_DEFAULT 默认
         * 如果Item的个数<=3就会使用MODE_FIXED模式，否则使用MODE_SHIFTING模式
         *
         * 2、BottomNavigationBar.MODE_FIXED 固定大小
         * 填充模式，未选中的Item会显示文字，没有换挡动画。
         *
         * 3、BottomNavigationBar.MODE_SHIFTING 不固定大小
         * 换挡模式，未选中的Item不会显示文字，选中的会显示文字。在切换的时候会有一个像换挡的动画
         */
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        /**
         * 设置背景的样式
         * 1、BottomNavigationBar.BACKGROUND_STYLE_DEFAULT 默认
         * 如果设置的Mode为MODE_FIXED，将使用BACKGROUND_STYLE_STATIC 。
         * 如果设置的Mode为MODE_SHIFTING,将使用BACKGROUND_STYLE_RIPPLE。
         *
         * 2、BottomNavigationBar.BACKGROUND_STYLE_STATIC 导航条的背景色是白色，
         * 加上setBarBackgroundColor（）可以设置成你所需要的任何背景颜色
         * 点击的时候没有水波纹效果
         *
         * 3、BottomNavigationBar.BACKGROUND_STYLE_RIPPLE 导航条的背景色是你设置的处于选中状态的
         * Item的颜色（ActiveColor），也就是setActiveColorResource这个设置的颜色
         * 点击的时候有水波纹效果
         */
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        //设置导航条背景颜色
        //在BACKGROUND_STYLE_STATIC下，表示整个容器的背景色。
        // 而在BACKGROUND_STYLE_RIPPLE下，表示选中Item的图标和文本颜色。默认 Color.WHITE
        bottomNavigationBar.setBarBackgroundColor(R.color.backgroundSecondary);
        //选中时的颜色,在BACKGROUND_STYLE_STATIC下，表示选中Item的图标和文本颜色。
        // 而在BACKGROUND_STYLE_RIPPLE下，表示整个容器的背景色。默认Theme's Primary Color
        bottomNavigationBar.setActiveColor(R.color.navigationBarTitleSelectedColor);
        //未选中时的颜色，表示未选中Item中的图标和文本颜色。默认为 Color.LTGRAY
        bottomNavigationBar.setInActiveColor(R.color.navigationBarTitleColor);

        //如果使用颜色的变化不足以展示一个选项Item的选中与非选中状态，
        // 可以使用BottomNavigationItem.setInActiveIcon()方法来设置。
        bottomNavigationBar.addItem(new BottomNavigationItem(R.mipmap.ic_food, "点餐")
                .setInactiveIcon(getResources().getDrawable(R.mipmap.ic_food)))
                .addItem(new BottomNavigationItem(R.mipmap.ic_cart, "购物车")
                        .setInactiveIcon(getResources().getDrawable(R.mipmap.ic_cart)))
                .addItem(new BottomNavigationItem(R.mipmap.ic_orders, "订单")
                        .setInactiveIcon(getResources().getDrawable(R.mipmap.ic_orders)))
                .addItem(new BottomNavigationItem(R.mipmap.ic_profile, "我的")
                        .setInactiveIcon(getResources().getDrawable(R.mipmap.ic_profile)))
                .setFirstSelectedPosition(0)
                .initialise(); //所有的设置需在调用该方法前完成
    }

    @Override
    public void onTabSelected(int position) {
        showHideFragment(mFragments[position]);
    }

    //实现tab页返回后仍然为主页面
    @Override
    public void onTabUnselected(int position) {
        final SupportFragment currentFragment = mFragments[position];

        if (currentFragment instanceof OrderFragment) {
            currentFragment.popToChild(OrderHomeFragment.class, false);
        } else if (currentFragment instanceof CartFragment) {
            currentFragment.popToChild(CartHomeFragment.class, false);
        } else if (currentFragment instanceof ManagementFragment) {
            currentFragment.popToChild(ManagementHomeFragment.class, false);
        } else if (currentFragment instanceof ProfileFragment) {
            currentFragment.popToChild(ProfileHomeFragment.class, false);
        }

    }

    @Override
    public void onTabReselected(int position) {
        final SupportFragment currentFragment = mFragments[position];
        int count = currentFragment.getChildFragmentManager().getBackStackEntryCount();

        // 如果不在该类别Fragment的主页,则回到主页;
        if (count > 1) {
            if (currentFragment instanceof OrderFragment) {
                currentFragment.popToChild(OrderHomeFragment.class, false);
            } else if (currentFragment instanceof CartFragment) {
                currentFragment.popToChild(CartHomeFragment.class, false);
            } else if (currentFragment instanceof ManagementFragment) {
                currentFragment.popToChild(ManagementHomeFragment.class, false);
            } else if (currentFragment instanceof ProfileFragment) {
                currentFragment.popToChild(ProfileHomeFragment.class, false);
            }
            return;
        }

        // 这里推荐使用EventBus来实现 -> 解耦
        if (count == 1) {
            // 在FirstPagerFragment中接收, 因为是嵌套的孙子Fragment 所以用EventBus比较方便
            // 主要为了交互: 重选tab 如果列表不在顶部则移动到顶部,如果已经在顶部,则刷新
            EventBusActivityScope.getDefault(MainActivity.this).post(new TabSelectedEvent(position));
        }
    }

    @Override
    public void onBackPressedSupport() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            pop();
        } else {
            ActivityCompat.finishAfterTransition(this);
        }
    }

    @Override
    public void onBackToFirstFragment() {
        bottomNavigationBar.selectTab(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "destroy");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "stop");
    }
}
