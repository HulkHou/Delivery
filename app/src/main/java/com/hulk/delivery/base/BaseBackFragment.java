package com.hulk.delivery.base;


import android.support.v7.widget.Toolbar;
import android.view.View;

import com.hulk.delivery.R;

import me.yokeyword.fragmentation.SupportFragment;


/**
 * Created by hulk-out on 2018/3/14.
 */
public class BaseBackFragment extends SupportFragment {

    protected void initToolbarNav(Toolbar toolbar) {
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mActivity.onBackPressed();
            }
        });
    }
}
