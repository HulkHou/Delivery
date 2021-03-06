package com.hulk.delivery.ui.fragment.order.child;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.hulk.delivery.R;
import com.hulk.delivery.adapter.BannerShopAdapter;
import com.hulk.delivery.adapter.MainViewHolder;
import com.hulk.delivery.adapter.SubAdapter;
import com.hulk.delivery.adapter.TitleAdapter;
import com.hulk.delivery.entity.ResponseDataObjectList;
import com.hulk.delivery.entity.ResponseResult;
import com.hulk.delivery.entity.TCollect;
import com.hulk.delivery.retrofit.Network;
import com.hulk.delivery.util.AlertDialogUtils;
import com.hulk.delivery.util.LoginUtil;
import com.hulk.delivery.util.RxLifecycleUtils;
import com.uber.autodispose.AutoDisposeConverter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by hulk-out on 2017/9/8.
 */

public class ShopDetailFragment extends SupportFragment {

    private View view;
    private static final String TAG = "ShopDetailFragment";
    static ShopDetailFragment fragment;

    private DelegateAdapter shopDetailAdapter;
    private RecyclerView shopDetailRecyclerView;
    private SubAdapter adapter_shop_detail_title;
    private SubAdapter getAdapter_shop_detail;
    private BannerShopAdapter bannerShopAdapter;

    private RecyclerView recyclerView;
    private DelegateAdapter adapter;
    private SubAdapter adapter_food_list_title;
    private SubAdapter adapter_food_list;
    private SubAdapter adapter_footer;

    private List<String> list = new ArrayList<>();
    private List<TCollect> collectShopList = new ArrayList<>();
    private ResponseDataObjectList<TCollect> responseDataCollectShopList;
    private boolean hasmore = true;
    private int page = 1;
    private Handler handler = new Handler();
    private AlertDialogUtils alertDialogUtils = AlertDialogUtils.getInstance();

    @BindView(R.id.expandButton)
    RelativeLayout mExpandButton;

    @BindView(R.id.expandableLayout)
    ExpandableRelativeLayout mExpandLayout;

    @BindView(R.id.ic_info_angel)
    ImageView icInfoAngel;

    public ShopDetailFragment() {
        // Required empty public constructor
    }

    public static ShopDetailFragment newInstance() {

        Bundle args = new Bundle();
        ShopDetailFragment fragment = new ShopDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.shop_detail, container, false);
        ButterKnife.bind(this, view);
//        EventBusActivityScope.getDefault(_mActivity).register(this);
        initToolbar();
        getCollectShopList(view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        EventBusActivityScope.getDefault(_mActivity).unregister(this);
    }

    //初始化工具栏
    private void initToolbar() {

    }


    //初始化页面元素
    private void initViews() {
        mExpandLayout.collapse();
        shopDetailRecyclerView = view.findViewById(R.id.rl_shop_detail);
        VirtualLayoutManager shopDetailManager = new VirtualLayoutManager(_mActivity);
        shopDetailRecyclerView.setLayoutManager(shopDetailManager);
        shopDetailAdapter = new DelegateAdapter(shopDetailManager, false);

        recyclerView = view.findViewById(R.id.rl_shop_list);
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
                                    for (int i = 0; i < 10; i++) {
                                        list.add("123");
                                    }
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

//        //店铺信息标题
//        adapter_shop_detail_title = new TitleAdapter(_mActivity, getTitleHelper()) {
//            @Override
//            public void onBindViewHolder(MainViewHolder holder, int position) {
//                super.onBindViewHolder(holder, position);
//            }
//
//            //设置标题名称
//            @Override
//            protected String getText() {
//                return getString(R.string.shop_detail);
//            }
//
//            //设置标题左右图标
//            @Override
//            protected int[] getDrawables() {
//                return new int[]{R.mipmap.ic_index_left, R.mipmap.ic_index_right};
//            }
//
//        };
//        shopDetailAdapter.addAdapter(adapter_shop_detail_title);

        //店铺信息列表
        SingleLayoutHelper layoutHelper = new SingleLayoutHelper();
        getAdapter_shop_detail = new SubAdapter(_mActivity, layoutHelper, 1) {
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

                bannerShopAdapter = new BannerShopAdapter(_mActivity, fragment, collectShopList);
                recyclerView.setAdapter(bannerShopAdapter);

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
        shopDetailAdapter.addAdapter(getAdapter_shop_detail);

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
                return new int[]{R.mipmap.ic_food_menu};
            }

        };
        adapter.addAdapter(adapter_food_list_title);

        //结果列表
        for (int i = 0; i < 10; i++) {
            list.add("abc");
        }
        LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
        adapter_food_list = new SubAdapter(_mActivity, linearLayoutHelper, list.size()) {

            @Override
            public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_food, parent, false);
                return new MainViewHolder(itemView);
            }

            @Override
            public void onBindViewHolder(MainViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
            }

            @Override
            public int getItemCount() {
                return list.size();
            }
        };
        adapter.addAdapter(adapter_food_list);

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

        shopDetailRecyclerView.setAdapter(shopDetailAdapter);
        recyclerView.setAdapter(adapter);

    }

    @OnClick(R.id.expandButton)
    public void setmExpandButton(View view) {
        mExpandLayout.toggle();
        if (icInfoAngel.getTag().equals("down")) {
            icInfoAngel.setImageResource(R.mipmap.ic_info_angel_up);
            icInfoAngel.setTag("up");
        } else {
            icInfoAngel.setImageResource(R.mipmap.ic_info_angel_down);
            icInfoAngel.setTag("down");
        }
    }


    //当Fragment可见时调用此方法
    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        initToolbar();
    }

    //当Fragment不可见时调用此方法
    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
    }

    @Override
    public boolean onBackPressedSupport() {
        pop();
        return true;
    }

    private SingleLayoutHelper getTitleHelper() {
        SingleLayoutHelper helper = new SingleLayoutHelper();
        helper.setMarginTop(20);
        return helper;
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
                            initViews();
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

    protected <T> AutoDisposeConverter<T> bindLifecycle() {
        return RxLifecycleUtils.bindLifecycle(this);
    }
}
