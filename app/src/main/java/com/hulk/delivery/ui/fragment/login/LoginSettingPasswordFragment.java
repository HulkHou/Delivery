package com.hulk.delivery.ui.fragment.login;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.hulk.delivery.MyApplication;
import com.hulk.delivery.R;
import com.hulk.delivery.entity.ResponseResult;
import com.hulk.delivery.event.Event;
import com.hulk.delivery.retrofit.Network;
import com.hulk.delivery.util.AlertDialogUtils;
import com.hulk.delivery.util.RxLifecycleUtils;
import com.hulk.delivery.util.StringUtilsCustomize;
import com.uber.autodispose.AutoDisposeConverter;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.yokeyword.eventbusactivityscope.EventBusActivityScope;
import me.yokeyword.fragmentation.SupportFragment;
import okhttp3.MediaType;
import okhttp3.RequestBody;


/**
 * Created by hulk-out on 2017/9/8.
 */

public class LoginSettingPasswordFragment extends SupportFragment {

    private View view;
    private MaterialDialog.Builder builder;
    private MaterialDialog dialog;

    //SharedPreferences key
    private static final String AUTHORIZATION = "AUTHORIZATION";
    private static final String IS_LOGIN = "IS_LOGIN";
    private final SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;

    private AlertDialogUtils alertDialogUtils = AlertDialogUtils.getInstance();

    private String phone;
    private String loginNewPassword;
    private String loginConfirmPassword;

    @BindView(R.id.et_login_new_password)
    EditText mLoginNewPassword;

    @BindView(R.id.et_login_confirm_password)
    EditText mLoginConfirmPassword;

    public LoginSettingPasswordFragment() {
        // Required empty public constructor
        //初始化
        mPref = PreferenceManager.getDefaultSharedPreferences(MyApplication.getInstance());
    }

    public static LoginSettingPasswordFragment newInstance() {

        Bundle args = new Bundle();
        LoginSettingPasswordFragment fragment = new LoginSettingPasswordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.login_frag_setting_password, container, false);
        ButterKnife.bind(this, view);
        EventBusActivityScope.getDefault(_mActivity).register(this);
        initToolbar();
        return view;
    }

    /**
     * 获取Phone事件
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onUserInfoEvent(Event.UserInfoEvent event) {
        phone = event.user.getPhone();
    }

    //初始化工具栏
    private void initToolbar() {

    }

    //当Fragment可见时调用此方法
    @Override
    public void onSupportVisible() {
        initToolbar();
    }

    @Override
    public boolean onBackPressedSupport() {
        pop();
        return true;
    }


    @OnClick(R.id.btn_login_password_submit)
    public void loginPasswordSubmit() {
        loginNewPassword = mLoginNewPassword.getText().toString();
        loginConfirmPassword = mLoginConfirmPassword.getText().toString();
        //校验输入框
        if (StringUtils.isBlank(loginNewPassword)) {
            //获取焦点
            mLoginNewPassword.requestFocus();
            //提示框
            alertDialogUtils.showBasicDialogNoTitle(_mActivity, R.string.passwordIsnull);
            return;
        }
        if (!StringUtilsCustomize.isAlphanumeric(loginNewPassword)) {
            //获取焦点
            mLoginNewPassword.requestFocus();
            //提示框
            alertDialogUtils.showBasicDialogNoTitle(_mActivity, R.string.passwordIsAlphanumeric);
            return;
        }
        if (StringUtils.isBlank(loginConfirmPassword)) {
            //获取焦点
            mLoginConfirmPassword.requestFocus();
            //提示框
            alertDialogUtils.showBasicDialogNoTitle(_mActivity, R.string.confirmPasswordIsNull);
            return;
        }
        if (!StringUtilsCustomize.isAlphanumeric(loginConfirmPassword)) {
            //获取焦点
            mLoginConfirmPassword.requestFocus();
            //提示框
            alertDialogUtils.showBasicDialogNoTitle(_mActivity, R.string.passwordIsAlphanumeric);
            return;
        }
        if (!loginNewPassword.equals(loginConfirmPassword)) {
            //获取焦点
            mLoginConfirmPassword.requestFocus();
            //提示框
            alertDialogUtils.showBasicDialogNoTitle(_mActivity, R.string.passwordIsDifferent);
            return;
        }

        JSONObject result = new JSONObject();
        try {
            result.put("phone", phone);
            result.put("password", loginNewPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), result.toString());

        Network.getUserApi().doAdd(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(bindLifecycle())
                .subscribe(new Consumer<ResponseResult>() {
                    @Override
                    public void accept(@NonNull ResponseResult responseResult) throws Exception {
                        String code = responseResult.getCode();
                        //status等于0时为查询成功
                        if ("200".equals(code)) {
                            doLogin(phone, loginNewPassword);
                        }
                        if ("400".equals(code)) {
                            alertDialogUtils.showBasicDialogNoTitle(_mActivity, R.string.phoneIsUsed);
                        } else {

                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        alertDialogUtils.showBasicDialogNoTitle(_mActivity, R.string.networkError);
                    }
                });
    }

    private void doLogin(String phone, String password) {
        Network.getUserApi().doLogin(phone, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(bindLifecycle())
                .subscribe(new Consumer<ResponseResult>() {
                    @Override
                    public void accept(@NonNull ResponseResult responseResult) throws Exception {
                        String code = responseResult.getCode();
                        String authorization = responseResult.getData().toString();
                        //status等于0时为登录成功
                        if ("200".equals(code)) {

                            //设置token
                            mEditor = mPref.edit();
                            mEditor.putString(AUTHORIZATION, authorization);
                            mEditor.putBoolean(IS_LOGIN, true);
                            mEditor.commit();//提交修改

                            Toast.makeText(getActivity(), R.string.loginSuccess, Toast.LENGTH_SHORT).show();
                            //返回登录前操作页面
                            pop();
                            _mActivity.onBackPressed();
                        } else {
                            alertDialogUtils.showBasicDialogNoTitle(_mActivity, R.string.loginFailure);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        alertDialogUtils.showBasicDialogNoTitle(_mActivity, R.string.networkError);
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBusActivityScope.getDefault(_mActivity).unregister(this);
    }

    protected <T> AutoDisposeConverter<T> bindLifecycle() {
        return RxLifecycleUtils.bindLifecycle(this);
    }
}
