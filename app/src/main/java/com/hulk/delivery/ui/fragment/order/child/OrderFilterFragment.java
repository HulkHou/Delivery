package com.hulk.delivery.ui.fragment.order.child;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.hulk.delivery.R;
import com.hulk.delivery.adapter.PriceRangeAdapter;
import com.hulk.delivery.ui.view.RangeSeekBar;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.yokeyword.fragmentation.SupportFragment;


/**
 * Created by hulk-out on 2017/9/8.
 */

public class OrderFilterFragment extends SupportFragment {

    private View view;
    private static final String TAG = "OrderFilterFragment";

    TimePickerDialog picker;

    @BindView(R.id.rb_item_chinese)
    CheckBox itemChinese;

    @BindView(R.id.rb_item_western)
    CheckBox itemWestern;

    @BindView(R.id.rb_item_japanese)
    CheckBox itemJapanese;

    @BindView(R.id.rb_item_thai)
    CheckBox itemThai;

    @BindView(R.id.rb_item_chuang)
    CheckBox itemChuang;

    @BindView(R.id.rb_item_xiang)
    CheckBox itemXiang;

    @BindView(R.id.rb_delivery_fee_start_no)
    RadioButton deliveryFeeStartNo;

    @BindView(R.id.rb_delivery_fee_start_yes)
    RadioButton deliveryFeeStartYes;

    @BindView(R.id.rb_delivery_fee_no)
    RadioButton deliveryFeeNo;

    @BindView(R.id.rb_delivery_fee_yes)
    RadioButton deliveryFeeYes;

    @BindView(R.id.rb_delivery_free_no)
    RadioButton deliveryFreeNo;

    @BindView(R.id.rb_delivery_free_yes)
    RadioButton deliveryFreeYes;


    @BindView(R.id.text_range_min)
    TextView mTextPriceRangeMin;

    @BindView(R.id.text_range_max)
    TextView mTextPriceRangeMax;

    @BindView(R.id.range_seek_bar_price)
    RangeSeekBar mRangeSeekBarPrice;

    @BindView(R.id.et_delivery_time_start)
    EditText deliveryTimeStart;

    @BindView(R.id.et_delivery_time_end)
    EditText deliveryTimeEnd;

    public OrderFilterFragment() {
        // Required empty public constructor
    }

    public static OrderFilterFragment newInstance() {

        Bundle args = new Bundle();
        OrderFilterFragment fragment = new OrderFilterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.item_filter, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    //初始化页面
    private void initView() {

        mRangeSeekBarPrice.setNotifyWhileDragging(true);
        mRangeSeekBarPrice.setAdapter(new PriceRangeAdapter());
        mRangeSeekBarPrice.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, int minValue, int maxValue) {
                mTextPriceRangeMin.setText(String.format("%1$sRM", minValue));
                mTextPriceRangeMax.setText(String.format("%1$sRM", maxValue));
            }
        });

        deliveryTimeStart.setInputType(InputType.TYPE_NULL);
        deliveryTimeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                picker = new TimePickerDialog(_mActivity,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                deliveryTimeStart.setText(sHour + ":" + String.format("%02d", sMinute));
                            }
                        }, hour, minutes, true);
                picker.show();
            }
        });

        deliveryTimeEnd.setInputType(InputType.TYPE_NULL);
        deliveryTimeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                picker = new TimePickerDialog(_mActivity,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                deliveryTimeEnd.setText(sHour + ":" + String.format("%02d", sMinute));
                            }
                        }, hour, minutes, true);
                picker.show();
            }
        });
    }

    @OnClick(R.id.iv_back)
    public void back(View view) {
        _mActivity.onBackPressed();
    }

    @OnClick(R.id.btn_reset)
    public void reset(View view) {
        itemChinese.setChecked(false);
        itemWestern.setChecked(false);
        itemJapanese.setChecked(false);
        itemThai.setChecked(false);
        itemChuang.setChecked(false);
        itemXiang.setChecked(false);
        deliveryFeeStartNo.setChecked(false);
        deliveryFeeStartYes.setChecked(false);
        deliveryFeeNo.setChecked(false);
        deliveryFeeYes.setChecked(false);
        deliveryFreeNo.setChecked(false);
        deliveryFreeYes.setChecked(false);
        mRangeSeekBarPrice.setSelectedMinValue(0);
        mRangeSeekBarPrice.setSelectedMaxValue(100);
        mTextPriceRangeMin.setText("0RM");
        mTextPriceRangeMax.setText("100RM");
        deliveryTimeStart.setText("09:00");
        deliveryTimeEnd.setText("18:00");
    }
}
