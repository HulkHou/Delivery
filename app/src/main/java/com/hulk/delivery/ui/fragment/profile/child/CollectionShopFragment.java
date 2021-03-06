package com.hulk.delivery.ui.fragment.profile.child;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.hulk.delivery.R;
import com.hulk.delivery.adapter.MainViewHolder;
import com.hulk.delivery.adapter.SubAdapter;
import com.hulk.delivery.adapter.TitleAdapter;
import com.hulk.delivery.entity.ResponseDataObjectList;
import com.hulk.delivery.entity.ResponseResult;
import com.hulk.delivery.entity.TCollect;
import com.hulk.delivery.retrofit.Network;
import com.hulk.delivery.ui.fragment.order.child.ShopDetailFragment;
import com.hulk.delivery.util.AlertDialogUtils;
import com.hulk.delivery.util.LoginUtil;
import com.hulk.delivery.util.RxLifecycleUtils;
import com.uber.autodispose.AutoDisposeConverter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by hulk-out on 2018/3/14.
 */

public class CollectionShopFragment extends SupportFragment
        implements SwipeRefreshLayout.OnRefreshListener {

    static CollectionShopFragment fragment;
    private View view;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView recyclerView;
    private DelegateAdapter adapter;
    private SubAdapter adapter_collection_title;
    private SubAdapter adapter_collection;
    private SubAdapter adapter_footer;

    private boolean hasmore = true;
    private int page = 1;
    private Handler handler = new Handler();

    private List<TCollect> collectShopList = new ArrayList<>();
    private ResponseDataObjectList<TCollect> responseDataCollectShopList;

    private AlertDialogUtils alertDialogUtils = AlertDialogUtils.getInstance();


    public static CollectionShopFragment newInstance() {

        Bundle args = new Bundle();
        fragment = new CollectionShopFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.collection_frag_shop, container, false);
        getCollectShopList(view);
        return view;
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.main_view);
        VirtualLayoutManager manager = new VirtualLayoutManager(_mActivity);
        recyclerView.setLayoutManager(manager);
        adapter = new DelegateAdapter(manager, false);

        //设置RecyclerView滚动监听
        RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            }

            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (hasmore) {
                    VirtualLayoutManager lm = (VirtualLayoutManager) recyclerView.getLayoutManager();
                    int last = 0, total = 0;
                    last = lm.findLastVisibleItemPosition();
                    total = recyclerView.getAdapter().getItemCount();
                    if (last > 0 && last >= total - 1) {
                        hasmore = false;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (page < 2) {
                                    getCollectShopList(view);
                                    adapter_collection.notifyDataSetChanged();
                                    page++;
                                    hasmore = true;
                                } else {
                                    hasmore = false;
                                    adapter_footer.notifyDataSetChanged();
                                }
                            }
                        }, 2000);

                    }
                }
            }
        };
        recyclerView.addOnScrollListener(onScrollListener);

        //我的收藏标题
        adapter_collection_title = new TitleAdapter(_mActivity, getTitleHelper()) {
            @Override
            public void onBindViewHolder(MainViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
            }

            //设置标题名称
            @Override
            protected String getText() {
                return getString(R.string.shop_collection);
            }

            //设置标题左右图标
            @Override
            protected int[] getDrawables() {
                return new int[]{R.mipmap.ic_index_left, R.mipmap.ic_index_right};
            }

        };
        adapter.addAdapter(adapter_collection_title);

        //收藏列表
        LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
        adapter_collection = new SubAdapter(_mActivity, linearLayoutHelper, collectShopList.size()) {

            @Override
            public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_shop, parent, false);
                return new MainViewHolder(itemView);
            }

            @Override
            public void onBindViewHolder(MainViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                RelativeLayout mShopDetail = holder.itemView.findViewById(R.id.rl_shop_detail);
                TextView mShopName = holder.itemView.findViewById(R.id.shop_name);
                TextView mShippingStartFee = holder.itemView.findViewById(R.id.shipping_start_fee);
                TextView mShippingFee = holder.itemView.findViewById(R.id.shipping_fee);
                TextView mShippingFreeFee = holder.itemView.findViewById(R.id.shipping_free_fee);
                TextView mShopDesc = holder.itemView.findViewById(R.id.shop_desc);

                String shopName = collectShopList.get(position).getShopName();
                String shippingStartFee = collectShopList.get(position).getShippingStartFee().toString();
                String shippingFee = collectShopList.get(position).getShippingFee().toString();
                String shippingFreeFee = collectShopList.get(position).getShippingFreeFee().toString();
                String shopDesc = collectShopList.get(position).getShopDesc();

                mShopName.setText(shopName);
                mShippingStartFee.setText(shippingStartFee);
                mShippingFee.setText(shippingFee);
                mShippingFreeFee.setText(shippingFreeFee);
                mShopDesc.setText(shopDesc);

                mShopDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((SupportFragment) getParentFragment()).start(ShopDetailFragment.newInstance());
                    }
                });
            }

            @Override
            public int getItemCount() {
                return collectShopList.size();
            }
        };
        adapter.addAdapter(adapter_collection);

        //下拉刷新
        mSwipeRefreshLayout = view.findViewById(R.id.SwipeRefreshLayout_shop);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.text_blue_color);

//        mSwipeRefreshLayout.post(new Runnable() {
//            @Override
//            public void run() {
////                mSwipeRefreshLayout.setRefreshing(true);
////                mSwipeRefreshLayout.setRefreshing(false);
//            }
//        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        collectShopList.clear();
                        getCollectShopList(view);
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });

        //加载更多布局
        SingleLayoutHelper moreLayoutHelper = new SingleLayoutHelper();
        adapter_footer = new SubAdapter(_mActivity, moreLayoutHelper, 1,
                new VirtualLayoutManager
                        .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100)) {
            @Override
            public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new MainViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_footer, parent, false));
            }

            @Override
            public void onBindViewHolder(MainViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                TextView textView = holder.itemView.findViewById(R.id.textview);
                ProgressBar progressBar = holder.itemView.findViewById(R.id.progressbar);
                if (hasmore) {
                    textView.setText(R.string.loading);
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    textView.setText(R.string.loaded);
                    progressBar.setVisibility(View.GONE);
                }
            }
        };
        adapter.addAdapter(adapter_footer);

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        collectShopList.clear();
        getCollectShopList(view);
    }

    private SingleLayoutHelper getTitleHelper() {
        SingleLayoutHelper helper = new SingleLayoutHelper();
        helper.setMarginTop(20);
        return helper;
    }

    protected <T> AutoDisposeConverter<T> bindLifecycle() {
        return RxLifecycleUtils.bindLifecycle(this);
    }

    //获取collectShopList
    private void getCollectShopList(View view) {
        //获取token
        String authorization = LoginUtil.getAuthorization();
        Network.getUserApi().getCollectShopList(authorization)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(bindLifecycle())
                .subscribe(new Consumer<ResponseResult<ResponseDataObjectList<TCollect>>>() {
                    @Override
                    public void accept(@NonNull ResponseResult responseResult) throws Exception {
                        String code = responseResult.getCode();

                        //status等于200时为查询成功
                        if ("200".equals(code)) {
                            responseDataCollectShopList = (ResponseDataObjectList<TCollect>) responseResult.getData();
                            collectShopList = responseDataCollectShopList.getList();
                            initView(view);
                        } else {
                            _mActivity.onBackPressed();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        alertDialogUtils.showBasicDialogNoTitle(_mActivity, R.string.networkError);
                    }
                });
    }

}

