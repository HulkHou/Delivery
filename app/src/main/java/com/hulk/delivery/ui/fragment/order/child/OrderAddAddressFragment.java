package com.hulk.delivery.ui.fragment.order.child;

import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.hulk.delivery.R;
import com.schibstedspain.leku.LocationPickerActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.yokeyword.fragmentation.SupportFragment;


/**
 * Created by hulk-out on 2017/9/8.
 */

public class OrderAddAddressFragment extends SupportFragment {

    private View view;
    private static final String TAG = "OrderAddAddressFragment";
    private String address;

    @BindView(R.id.et_order_address_add_street)
    EditText street;

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
                .withGeolocApiKey("AIzaSyD7AX_UBOgvnoIQFum7kCg-huJWB1chxbM")
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
                street.setText(address);
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
        _mActivity.onBackPressed();
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
    }
}
