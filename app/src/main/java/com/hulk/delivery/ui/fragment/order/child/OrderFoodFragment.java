package com.hulk.delivery.ui.fragment.order.child;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
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
import com.hulk.delivery.adapter.BannerFoodAdapter;
import com.hulk.delivery.adapter.MainViewHolder;
import com.hulk.delivery.adapter.SubAdapter;
import com.hulk.delivery.adapter.TitleAdapter;
import com.hulk.delivery.entity.ResponseDataObjectList;
import com.hulk.delivery.entity.ResponseResult;
import com.hulk.delivery.entity.TCollect;
import com.hulk.delivery.entity.TFood;
import com.hulk.delivery.retrofit.Network;
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

public class OrderFoodFragment extends SupportFragment
        implements SwipeRefreshLayout.OnRefreshListener {

    static OrderFoodFragment fragment;
    private View view;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView recyclerView;
    private DelegateAdapter adapter;

    private SubAdapter adapter_collection_title;
    private SubAdapter adapter_collection;
    private SubAdapter adapter_food_list_title;
    private SubAdapter adapter_food_list;
    private SubAdapter adapter_footer;

    private BannerFoodAdapter bAdapter;

    private boolean hasmore = true;
    private int page = 1;
    private Handler handler = new Handler();

    private List<TFood> foodList = new ArrayList<>();
    private List<TCollect> collectFoodList = new ArrayList<>();
    private ResponseDataObjectList<TFood> responseDataFoodList;
    private ResponseDataObjectList<TCollect> responseDataCollectFoodList;

    private Boolean isLogin = false;

    private AlertDialogUtils alertDialogUtils = AlertDialogUtils.getInstance();


    public static OrderFoodFragment newInstance() {

        Bundle args = new Bundle();
        OrderFoodFragment fragment = new OrderFoodFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.order_frag_shop, container, false);
        if (isLogin) {
            getCollectFoodList(view);
        }
        getFoodList(view);
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
                                    getFoodList(view);
                                    adapter_food_list.notifyDataSetChanged();
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

        //未登录不显示收藏列表
        if (isLogin) {
            //我的收藏标题
            adapter_collection_title = new TitleAdapter(_mActivity, getTitleHelper()) {
                @Override
                public void onBindViewHolder(MainViewHolder holder, int position) {
                    super.onBindViewHolder(holder, position);
                }

                //设置标题名称
                @Override
                protected String getText() {
                    return getString(R.string.food_collection);
                }

                //设置标题左右图标
                @Override
                protected int[] getDrawables() {
                    return new int[]{R.mipmap.ic_index_left, R.mipmap.ic_index_right};
                }

            };
            adapter.addAdapter(adapter_collection_title);

            //我的收藏
            SingleLayoutHelper layoutHelper = new SingleLayoutHelper();
            adapter_collection = new SubAdapter(_mActivity, layoutHelper, 1) {
                @Override
                public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    return new MainViewHolder(LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.item_collection, parent, false));
                }

                @Override
                public void onBindViewHolder(MainViewHolder holder, int position) {
                    super.onBindViewHolder(holder, position);
                    RecyclerView recyclerView = holder.itemView.findViewById(R.id.banner);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(_mActivity);
                    linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    recyclerView.setLayoutManager(linearLayoutManager);

                    bAdapter = new BannerFoodAdapter(_mActivity, fragment, collectFoodList);
                    recyclerView.setAdapter(bAdapter);

                    recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                            super.onScrollStateChanged(recyclerView, newState);
                        }

                        @Override
                        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);
                        }
                    });

                }
            };
            adapter.addAdapter(adapter_collection);

        }

        //结果列表标题
        adapter_food_list_title = new TitleAdapter(_mActivity, getTitleHelper()) {
            @Override
            public void onBindViewHolder(MainViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
            }

            //设置标题名称
            @Override
            protected String getText() {
                return getString(R.string.food_list_title);
            }

            //设置标题左右图标
            @Override
            protected int[] getDrawables() {
                return new int[]{R.mipmap.ic_index_left, R.mipmap.ic_index_right};
            }

        };
        adapter.addAdapter(adapter_food_list_title);

        //结果列表
        LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
        adapter_food_list = new SubAdapter(_mActivity, linearLayoutHelper, foodList.size()) {

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

                String shopName = foodList.get(position).getShopName();
                String shippingStartFee = foodList.get(position).getShippingStartFee().toString();
                String shippingFee = foodList.get(position).getShippingFee().toString();
                String shippingFreeFee = foodList.get(position).getShippingFreeFee().toString();
                String shopDesc = foodList.get(position).getShopDesc();

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
                return foodList.size();
            }
        };
        adapter.addAdapter(adapter_food_list);

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
                        if (isLogin) {
                            collectFoodList.clear();
                            getCollectFoodList(view);
                        }
                        foodList.clear();
                        getFoodList(view);
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
        isLogin = LoginUtil.checkLogin(_mActivity);
        if (isLogin) {
            collectFoodList.clear();
            getCollectFoodList(view);
        }
        foodList.clear();
        getFoodList(view);
    }

    private SingleLayoutHelper getTitleHelper() {
        SingleLayoutHelper helper = new SingleLayoutHelper();
        helper.setMarginTop(20);
        return helper;
    }

    protected <T> AutoDisposeConverter<T> bindLifecycle() {
        return RxLifecycleUtils.bindLifecycle(this);
    }

    //获取collectFoodList
    private void getCollectFoodList(View view) {
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
                            responseDataCollectFoodList = (ResponseDataObjectList<TCollect>) responseResult.getData();
                            collectFoodList = responseDataCollectFoodList.getList();
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

    //获取foodList
    private void getFoodList(View view) {
        Network.getUserApi().getFoodList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(bindLifecycle())
                .subscribe(new Consumer<ResponseResult<ResponseDataObjectList<TFood>>>() {
                    @Override
                    public void accept(@NonNull ResponseResult responseResult) throws Exception {
                        String code = responseResult.getCode();

                        //status等于200时为查询成功
                        if ("200".equals(code)) {
                            responseDataFoodList = (ResponseDataObjectList<TFood>) responseResult.getData();
                            foodList = responseDataFoodList.getList();
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

