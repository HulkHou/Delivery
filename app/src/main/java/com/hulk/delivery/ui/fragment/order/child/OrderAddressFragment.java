package com.hulk.delivery.ui.fragment.order.child;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.alibaba.android.vlayout.layout.StickyLayoutHelper;
import com.hulk.delivery.R;
import com.hulk.delivery.adapter.MainViewHolder;
import com.hulk.delivery.adapter.SubAdapter;
import com.hulk.delivery.adapter.TitleAdapter;
import com.hulk.delivery.entity.ResponseDataAddressList;
import com.hulk.delivery.entity.ResponseResult;
import com.hulk.delivery.entity.TAddress;
import com.hulk.delivery.event.Event;
import com.hulk.delivery.retrofit.Network;
import com.hulk.delivery.util.RxLifecycleUtils;
import com.hulk.delivery.util.ScreenUtil;
import com.uber.autodispose.AutoDisposeConverter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.yokeyword.eventbusactivityscope.EventBusActivityScope;
import me.yokeyword.fragmentation.SupportFragment;


/**
 * Created by hulk-out on 2017/9/8.
 */

public class OrderAddressFragment extends SupportFragment {

    private View view;
    private static final String TAG = "OrderAddressFragment";
    private SubAdapter adapter_add_address;
    private SubAdapter adapter_address_list_title;
    private SubAdapter adapter_address_list;
    private DelegateAdapter adapter;
    private RecyclerView recyclerView;
    private List<TAddress> addressList = new ArrayList<>();

    private ResponseDataAddressList responseDataAddressList;

    private Event.AddressInfoEvent addressInfoEvent;

    public OrderAddressFragment() {
        // Required empty public constructor
    }

    public static OrderAddressFragment newInstance() {

        Bundle args = new Bundle();
        OrderAddressFragment fragment = new OrderAddressFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.order_frag_address, container, false);
        getAddressList();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    //初始化页面
    private void initView() {

        recyclerView = view.findViewById(R.id.order_address_view);
        VirtualLayoutManager manager = new VirtualLayoutManager(_mActivity);
        recyclerView.setLayoutManager(manager);
        adapter = new DelegateAdapter(manager, false);

        //新增地址栏
        StickyLayoutHelper stickyLayoutHelper = new StickyLayoutHelper();
        adapter_add_address = new SubAdapter(_mActivity, stickyLayoutHelper, 1,
                new VirtualLayoutManager
                        .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ScreenUtil.dip2px(_mActivity, 50))) {
            @Override
            public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new MainViewHolder(LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.item_add_address, parent, false));
            }

            @Override
            public void onBindViewHolder(MainViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                TextView addAddress = holder.itemView.findViewById(R.id.add_address);
                addAddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        start(OrderAddAddressFragment.newInstance());
                    }
                });
            }
        };
        adapter.addAdapter(adapter_add_address);


        //结果列表标题
        adapter_address_list_title = new TitleAdapter(_mActivity, getTitleHelper()) {
            @Override
            public void onBindViewHolder(MainViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
            }

            //设置标题名称
            @Override
            protected String getText() {
                return getString(R.string.address_list_title);
            }

            //设置标题左右图标
            @Override
            protected int[] getDrawables() {
                return new int[]{R.mipmap.ic_index_left, R.mipmap.ic_index_right};
            }

        };
        adapter.addAdapter(adapter_address_list_title);

        //结果列表
        LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
        adapter_address_list = new SubAdapter(_mActivity, linearLayoutHelper, addressList.size()) {
            @Override
            public void onBindViewHolder(MainViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                RelativeLayout addDetail = holder.itemView.findViewById(R.id.rl_address_detail);
                TextView mTddressTag = holder.itemView.findViewById(R.id.address_tag);
                TextView mConsignee = holder.itemView.findViewById(R.id.consignee);
                TextView mPhone = holder.itemView.findViewById(R.id.phone);
                TextView mUnitNo = holder.itemView.findViewById(R.id.unit_no);
                TextView mBuildName = holder.itemView.findViewById(R.id.build_name);
                TextView mStreet = holder.itemView.findViewById(R.id.street);

                Integer id = addressList.get(position).getId();
                String addressTag = addressList.get(position).getAddressTag();
                String consignee = addressList.get(position).getConsignee();
                String phone = addressList.get(position).getPhone();
                String unitNo = addressList.get(position).getUnitNo();
                String buildName = addressList.get(position).getBuildName();
                String street = addressList.get(position).getStreet();

                mTddressTag.setText(addressList.get(position).getAddressTag());
                mConsignee.setText(addressList.get(position).getConsignee());
                mPhone.setText(addressList.get(position).getPhone());
                mUnitNo.setText(addressList.get(position).getUnitNo());
                mBuildName.setText(addressList.get(position).getBuildName());
                mStreet.setText(addressList.get(position).getStreet());

                addDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addressInfoEvent = new Event.AddressInfoEvent();
                        addressInfoEvent.tAddress = new TAddress(id, addressTag, consignee, phone, street, buildName, unitNo);
                        EventBusActivityScope.getDefault(_mActivity).postSticky(addressInfoEvent);
                        start(OrderUpdateAddressFragment.newInstance());
                    }
                });

            }

            @Override
            public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_address_detail, parent, false);
                return new MainViewHolder(itemView);
            }

            @Override
            public int getItemCount() {
                return addressList.size();
            }
        };
        adapter.addAdapter(adapter_address_list);

        recyclerView.setAdapter(adapter);

    }

    private SingleLayoutHelper getTitleHelper() {
        SingleLayoutHelper helper = new SingleLayoutHelper();
        helper.setMarginTop(20);
        return helper;
    }

    @Override
    public boolean onBackPressedSupport() {
        pop();
        return true;
    }

    //获取AddressList
    private void getAddressList() {
        String authorization = Network.getAuthorization();
        Network.getUserApi().getAddressList(authorization)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(bindLifecycle())
                .subscribe(new Consumer<ResponseResult<ResponseDataAddressList>>() {
                    @Override
                    public void accept(@NonNull ResponseResult responseResult) throws Exception {
                        String code = responseResult.getCode();

                        //status等于200时为查询成功
                        if ("200".equals(code)) {
                            responseDataAddressList = (ResponseDataAddressList) responseResult.getData();
                            addressList = responseDataAddressList.getList();
                            initView();
                        } else {
                            _mActivity.onBackPressed();
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
    public void onSupportVisible() {
        super.onSupportVisible();
        getAddressList();
    }

    protected <T> AutoDisposeConverter<T> bindLifecycle() {
        return RxLifecycleUtils.bindLifecycle(this);
    }
}
