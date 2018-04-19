package com.hulk.delivery.ui.fragment.order.child;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.alibaba.android.vlayout.layout.StickyLayoutHelper;
import com.hulk.delivery.R;
import com.hulk.delivery.adapter.MainViewHolder;
import com.hulk.delivery.adapter.OrderHomeFragmentAdapter;
import com.hulk.delivery.adapter.SubAdapter;
import com.hulk.delivery.entity.GoogleAddressResponseResult;
import com.hulk.delivery.entity.Results;
import com.hulk.delivery.retrofit.Network;
import com.hulk.delivery.ui.fragment.login.LoginByPasswordFragment;
import com.hulk.delivery.ui.fragment.profile.child.MessageFragment;
import com.hulk.delivery.util.LoginUtil;
import com.hulk.delivery.util.RxLifecycleUtils;
import com.hulk.delivery.util.ScreenUtil;
import com.schibstedspain.leku.LocationPickerActivity;
import com.uber.autodispose.AutoDisposeConverter;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
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
    private TextView addressDesc;
    private String address;

    private DelegateAdapter adapter;
    private SubAdapter adapter_search;
    private SubAdapter adapter_address;

    //定位都要通过LocationManager这个类实现
    private LocationManager locationManager;
    private String provider;
    private String latlng;
    private String formattedAddress;

    public static OrderHomeFragment newInstance() {

        Bundle args = new Bundle();

        OrderHomeFragment fragment = new OrderHomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_frag_home, container, false);
        initView(view);
        //获取当前地址
        getInfoFromLocation();
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

                ImageView message = holder.itemView.findViewById(R.id.message);
                message.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (LoginUtil.checkLogin(_mActivity)) {
                            start(MessageFragment.newInstance());
                        } else {
                            start(LoginByPasswordFragment.newInstance());
                        }
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
                addressDesc = holder.itemView.findViewById(R.id.address_desc);
                addressDesc.setText(R.string.addressDesc);
                final LinearLayout linearLayoutAddress = holder.itemView.findViewById(R.id.ll_address);
                linearLayoutAddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (LoginUtil.checkLogin(_mActivity)) {
                            start(OrderAddressFragment.newInstance());
                        } else {
                            Intent intent = new LocationPickerActivity.Builder()
                                    .withLocation(3.133333, 101.683333)
                                    .withGeolocApiKey("@string/API_KEY")
//                .withSearchZone("ms_MY")
                                    .shouldReturnOkOnBackPressed()
//                .withStreetHidden()
//                .withCityHidden()
//                .withZipCodeHidden()
//                .withSatelliteViewHidden()
                                    .build(getContext());

                            intent.putExtra("test", "hulk");

                            startActivityForResult(intent, 1);
                        }
                    }
                });

            }
        };
        adapter.addAdapter(adapter_address);

        recyclerView.setAdapter(adapter);

        mTab.addTab(mTab.newTab());
        mTab.addTab(mTab.newTab());

    }

    private void getInfoFromLocation() {
        //获取定位服务
        locationManager = (LocationManager) _mActivity.getSystemService(Context.LOCATION_SERVICE);
        //获取当前可用的位置控制器
        List<String> list = locationManager.getProviders(true);

        if (list.contains(LocationManager.GPS_PROVIDER)) {
            //是否为GPS位置控制器
            provider = LocationManager.GPS_PROVIDER;
        } else if (list.contains(LocationManager.NETWORK_PROVIDER)) {
            //是否为网络位置控制器
            provider = LocationManager.NETWORK_PROVIDER;

        } else {
            Toast.makeText(_mActivity, "请检查网络或GPS是否打开",
                    Toast.LENGTH_LONG).show();
            return;
        }

        if (ContextCompat.checkSelfPermission(_mActivity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(_mActivity, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Location location = locationManager.getLastKnownLocation(provider);

        if (location != null) {
            //获取当前位置，这里只用到了经纬度
            String string = "纬度为：" + location.getLatitude() + ",经度为："
                    + location.getLongitude();
            updateView(location);
        }


        //绑定定位事件，监听位置是否改变
        //第一个参数为控制器类型第二个参数为监听位置变化的时间间隔（单位：毫秒）
        //第三个参数为位置变化的间隔（单位：米）第四个参数为位置监听器
        locationManager.requestLocationUpdates(provider, 2000, 2,
                locationListener);

    }

    LocationListener locationListener = new LocationListener() {

        @Override
        public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProviderEnabled(String arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProviderDisabled(String arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onLocationChanged(Location arg0) {
            // TODO Auto-generated method stub
            // 更新当前经纬度
            updateView(arg0);
        }
    };

    private void updateView(Location location) {
        latlng = location.getLatitude() + "," + location.getLongitude();
        String googleAddressApiKey = getString(R.string.GOOGLE_ADDRESS_API_KEY);
        Network.getGoogleApi().getAddressByGoogle(latlng, googleAddressApiKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(bindLifecycle())
                .subscribe(new Consumer<GoogleAddressResponseResult>() {
                    @Override
                    public void accept(@NonNull GoogleAddressResponseResult responseResult) throws Exception {
                        String code = responseResult.getStatus();
                        List<Results> resultsArrayList = responseResult.getResults();

                        //status等于200时为查询成功
                        if ("OK".equals(code)) {
                            formattedAddress = resultsArrayList.get(0).getFormatted_address();
                            addressDesc.setText(formattedAddress);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        System.out.println("************");
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                double latitude = data.getDoubleExtra(LocationPickerActivity.LATITUDE, 0);
                Log.d("LATITUDE****", String.valueOf(latitude));
                double longitude = data.getDoubleExtra(LocationPickerActivity.LONGITUDE, 0);
                Log.d("LONGITUDE****", String.valueOf(longitude));
                address = data.getStringExtra(LocationPickerActivity.LOCATION_ADDRESS);
                addressDesc.setText(address);
                Log.d("ADDRESS****", String.valueOf(address));
                String postalcode = data.getStringExtra(LocationPickerActivity.ZIPCODE);
                Log.d("POSTALCODE****", String.valueOf(postalcode));
                Bundle bundle = data.getBundleExtra(LocationPickerActivity.TRANSITION_BUNDLE);
                Log.d("BUNDLE TEXT****", bundle.getString("test"));
                Address fullAddress = data.getParcelableExtra(LocationPickerActivity.ADDRESS);
                if (fullAddress != null)
                    Log.d("FULL ADDRESS****", fullAddress.toString());
            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        mViewPager.setAdapter(new OrderHomeFragmentAdapter(getChildFragmentManager(),
                getString(R.string.shop_title), getString(R.string.food_title)));
        mTab.setupWithViewPager(mViewPager);
    }

    //解决打开mDrawerLayout之后，点击手机返回键，直接退出activity问题
    @Override
    public boolean onBackPressedSupport() {
        if (mDrawerLayout.isDrawerOpen(mDrawerContent)) {
            mDrawerLayout.closeDrawers();
            return true;
        } else {
            return super.onBackPressedSupport();
        }
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        //返回时隐藏软键盘
        hideSoftInput();
    }

    protected <T> AutoDisposeConverter<T> bindLifecycle() {
        return RxLifecycleUtils.bindLifecycle(this);
    }
}