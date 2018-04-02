package com.hulk.delivery.ui.fragment.order.child;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.hulk.delivery.retrofit.Network;
import com.hulk.delivery.util.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
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
        view = inflater.inflate(R.layout.fragment_order_address, container, false);
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
                TextView addressTag = holder.itemView.findViewById(R.id.address_tag);
                TextView consignee = holder.itemView.findViewById(R.id.consignee);
                TextView phone = holder.itemView.findViewById(R.id.phone);
                TextView unitNo = holder.itemView.findViewById(R.id.unit_no);
                TextView buildName = holder.itemView.findViewById(R.id.build_name);
                TextView street = holder.itemView.findViewById(R.id.street);

                addressTag.setText(addressList.get(position).getAddressTag());
                consignee.setText(addressList.get(position).getConsignee());
                phone.setText(addressList.get(position).getPhone());
                unitNo.setText(addressList.get(position).getUnitNo());
                buildName.setText(addressList.get(position).getBuildName());
                street.setText(addressList.get(position).getStreet());
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
                .subscribe(new Consumer<ResponseResult<ResponseDataAddressList>>() {
                    @Override
                    public void accept(@NonNull ResponseResult responseResult) throws Exception {
                        String code = responseResult.getCode();

                        //status等于200时为查询成功
                        if ("200".equals(code)) {
                            responseDataAddressList = (ResponseDataAddressList) responseResult.getData();
                            addressList = responseDataAddressList.getList();
                            initView();
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
}
