package com.hulk.delivery.ui.fragment.cart.child;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hulk.delivery.R;
import com.hulk.delivery.adapter.CartAdapter;
import com.hulk.delivery.entity.CartBean;
import com.hulk.delivery.ui.fragment.cart.CartFragment;

import java.util.ArrayList;
import java.util.List;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by hulk-out on 2018/3/19.
 */
public class CartHomeFragment extends SupportFragment {

    private TextView tvShopCartSubmit, tvShopCartSelect, tvShopCartTotalNum;
    private View mEmtryView;

    private RecyclerView rlvShopCart, rlvHotProducts;
    private CartAdapter mCartAdapter;
    private LinearLayout llPay;
    private RelativeLayout rlHaveProduct;
    private List<CartBean.CartlistBean> mAllOrderList = new ArrayList<>();
    private ArrayList<CartBean.CartlistBean> mGoPayList = new ArrayList<>();
    private List<String> mHotProductsList = new ArrayList<>();
    private TextView tvShopCartTotalPrice;
    private int mCount, mPosition;
    private float mTotalPrice1;
    private boolean mSelect;

    public static CartHomeFragment newInstance() {

        Bundle args = new Bundle();

        CartHomeFragment fragment = new CartHomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart_home, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {

        tvShopCartSelect = (TextView) view.findViewById(R.id.tv_cart_all_select);
        tvShopCartTotalPrice = (TextView) view.findViewById(R.id.tv_cart_total_price);
        tvShopCartTotalNum = (TextView) view.findViewById(R.id.tv_shopcart_totalnum);

        rlHaveProduct = (RelativeLayout) view.findViewById(R.id.rl_cart);
        rlvShopCart = (RecyclerView) view.findViewById(R.id.rlv_cart);
        mEmtryView = (View) view.findViewById(R.id.emtryview);
        mEmtryView.setVisibility(View.GONE);
        llPay = (LinearLayout) view.findViewById(R.id.ll_pay);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        llPay.setLayoutParams(lp);

        tvShopCartSubmit = (TextView) view.findViewById(R.id.tv_shopcart_submit);

        rlvShopCart.setLayoutManager(new LinearLayoutManager(_mActivity));
        mCartAdapter = new CartAdapter(_mActivity, mAllOrderList);
        rlvShopCart.setAdapter(mCartAdapter);
        //删除商品接口
        mCartAdapter.setOnDeleteClickListener(new CartAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(View view, int position, int cartid) {
                mCartAdapter.notifyDataSetChanged();
            }
        });
        //修改数量接口
        mCartAdapter.setOnEditClickListener(new CartAdapter.OnEditClickListener() {
            @Override
            public void onEditClick(int position, int cartid, int count) {
                mCount = count;
                mPosition = position;
            }
        });
        //实时监控全选按钮
        mCartAdapter.setResfreshListener(new CartAdapter.OnResfreshListener() {
            @Override
            public void onResfresh(boolean isSelect) {
                mSelect = isSelect;
                if (isSelect) {
                    Drawable left = getResources().getDrawable(R.mipmap.ic_cart_selected);
                    tvShopCartSelect.setCompoundDrawablesWithIntrinsicBounds(left, null, null, null);
                } else {
                    Drawable left = getResources().getDrawable(R.mipmap.ic_cart_unselected);
                    tvShopCartSelect.setCompoundDrawablesWithIntrinsicBounds(left, null, null, null);
                }
                float mTotalPrice = 0;
                int mTotalNum = 0;
                mTotalPrice1 = 0;
                mGoPayList.clear();
                for (int i = 0; i < mAllOrderList.size(); i++)
                    if (mAllOrderList.get(i).getIsSelect()) {
                        mTotalPrice += Float.parseFloat(mAllOrderList.get(i).getPrice()) * mAllOrderList.get(i).getCount();
                        mTotalNum += 1;
                        mGoPayList.add(mAllOrderList.get(i));
                    }
                mTotalPrice1 = mTotalPrice;
                tvShopCartTotalPrice.setText("总价：" + mTotalPrice);
                tvShopCartTotalNum.setText("共" + mTotalNum + "件商品");
            }
        });

        //全选
        tvShopCartSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelect = !mSelect;
                if (mSelect) {
                    Drawable left = getResources().getDrawable(R.mipmap.ic_cart_selected);
                    tvShopCartSelect.setCompoundDrawablesWithIntrinsicBounds(left, null, null, null);
                    for (int i = 0; i < mAllOrderList.size(); i++) {
                        mAllOrderList.get(i).setSelect(true);
                        mAllOrderList.get(i).setShopSelect(true);
                    }
                } else {
                    Drawable left = getResources().getDrawable(R.mipmap.ic_cart_unselected);
                    tvShopCartSelect.setCompoundDrawablesWithIntrinsicBounds(left, null, null, null);
                    for (int i = 0; i < mAllOrderList.size(); i++) {
                        mAllOrderList.get(i).setSelect(false);
                        mAllOrderList.get(i).setShopSelect(false);
                    }
                }
                mCartAdapter.notifyDataSetChanged();

            }
        });

        initData();
        mCartAdapter.notifyDataSetChanged();
    }

    private void initData() {
        for (int i = 0; i < 2; i++) {
            CartBean.CartlistBean sb = new CartBean.CartlistBean();
            sb.setShopId(1);
            sb.setPrice("1.0");
            sb.setDefaultPic("http://img2.3lian.com/2014/c7/25/d/40.jpg");
            sb.setProductName("狼牙龙珠鼠标");
            sb.setShopName("狼牙龙珠");
            sb.setColor("蓝色");
            sb.setCount(2);
            mAllOrderList.add(sb);
        }

        for (int i = 0; i < 2; i++) {
            CartBean.CartlistBean sb = new CartBean.CartlistBean();
            sb.setShopId(2);
            sb.setPrice("1.0");
            sb.setDefaultPic("http://img2.3lian.com/2014/c7/25/d/40.jpg");
            sb.setProductName("达尔优鼠标");
            sb.setShopName("达尔优");
            sb.setColor("绿色");
            sb.setCount(2);
            mAllOrderList.add(sb);
        }
        for (int i = 0; i < 2; i++) {
            CartBean.CartlistBean sb = new CartBean.CartlistBean();
            sb.setShopId(3);
            sb.setPrice("1.0");
            sb.setDefaultPic("http://img2.3lian.com/2014/c7/25/d/40.jpg");
            sb.setProductName("达尔优鼠标");
            sb.setShopName("达尔优");
            sb.setColor("绿色");
            sb.setCount(2);
            mAllOrderList.add(sb);
        }
        isSelectFirst(mAllOrderList);
    }

    public static void isSelectFirst(List<CartBean.CartlistBean> list) {
        if (list.size() > 0) {
            list.get(0).setIsFirst(1);
            for (int i = 1; i < list.size(); i++) {
                if (list.get(i).getShopId() == list.get(i - 1).getShopId()) {
                    list.get(i).setIsFirst(2);
                } else {
                    list.get(i).setIsFirst(1);
                }
            }
        }

    }

    @Override
    public boolean onBackPressedSupport() {
        // 这里实际项目中推荐使用 EventBus接耦
        ((CartFragment) getParentFragment()).onBackToFirstFragment();
        return true;
    }
}