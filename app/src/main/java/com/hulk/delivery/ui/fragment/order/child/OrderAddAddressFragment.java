package com.hulk.delivery.ui.fragment.order.child;

import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.hulk.delivery.R;
import com.hulk.delivery.entity.ResponseResult;
import com.hulk.delivery.retrofit.Network;
import com.schibstedspain.leku.LocationPickerActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.yokeyword.fragmentation.SupportFragment;
import okhttp3.MediaType;
import okhttp3.RequestBody;


/**
 * Created by hulk-out on 2017/9/8.
 */

public class OrderAddAddressFragment extends SupportFragment {

    private View view;
    private static final String TAG = "OrderAddAddressFragment";
    private String address;

    private String userId;
    private String addressTag;
    private Integer isDefault;
    private String consignee;
    private String phone;
    private String street;
    private String buildName;
    private String unitNo;

    @BindView(R.id.et_order_address_add_tag)
    EditText tagView;

    @BindView(R.id.et_order_address_add_name)
    EditText nameView;

    @BindView(R.id.et_order_address_add_phone)
    EditText phoneView;

    @BindView(R.id.et_order_address_add_unit)
    EditText unitNoView;

    @BindView(R.id.et_order_address_add_building_name)
    EditText buildNameView;

    @BindView(R.id.et_order_address_add_street)
    EditText streetView;

    public OrderAddAddressFragment() {
        // Required empty public constructor
    }

    public static OrderAddAddressFragment newInstance() {

        Bundle args = new Bundle();
        OrderAddAddressFragment fragment = new OrderAddAddressFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_order_address_add, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    //初始化页面
    private void initView() {
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public boolean onBackPressedSupport() {
        pop();
        return true;
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
                streetView.setText(address);
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

    @OnClick(R.id.btn_order_address_add_submit)
    public void addressAdd(View view) {
        userId = "47";
        addressTag = "my office";
        isDefault = 1;
        consignee = "hulk";
        phone = "0172023067";
        street = "jalan ampang";
        buildName = "klcc";
        unitNo = "1-1-11";

        JSONObject result = new JSONObject();
        try {
            result.put("userId", userId);
            result.put("addressTag", addressTag);
            result.put("isDefault", isDefault);
            result.put("consignee", consignee);
            result.put("phone", phone);
            result.put("street", street);
            result.put("buildName", buildName);
            result.put("unitNo", unitNo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //获取token
        String authorization = Network.getAuthorization();
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), result.toString());

        Network.getUserApi().doAddAddress(authorization, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseResult>() {
                    @Override
                    public void accept(@NonNull ResponseResult responseResult) throws Exception {
                        String code = responseResult.getCode();

                        //status等于200时为查询成功
                        if ("200".equals(code)) {
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
    }
}
