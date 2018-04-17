package com.hulk.delivery.ui.fragment.login;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.hulk.delivery.MyApplication;
import com.hulk.delivery.R;
import com.hulk.delivery.base.BaseMainFragment;
import com.hulk.delivery.entity.ResponseResult;
import com.hulk.delivery.entity.User;
import com.hulk.delivery.event.Event;
import com.hulk.delivery.retrofit.Network;
import com.hulk.delivery.util.AlertDialogUtils;
import com.hulk.delivery.util.CountDownTimerUtils;
import com.hulk.delivery.util.RxLifecycleUtils;
import com.hulk.delivery.util.StateButton;
import com.uber.autodispose.AutoDisposeConverter;

import org.apache.commons.lang3.StringUtils;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.yokeyword.eventbusactivityscope.EventBusActivityScope;

/**
 * Created by hulk-out on 2017/9/8.
 */

public class LoginByCodeFragment extends BaseMainFragment {

    private View view;
    private final MyHandler mHandler = new MyHandler(this);
    private static final String TAG = "LoginByCodeFragment";
    private AlertDialogUtils alertDialogUtils = AlertDialogUtils.getInstance();

    //SharedPreferences key
    private static final String AUTHORIZATION = "AUTHORIZATION";
    private static final String IS_LOGIN = "IS_LOGIN";
    private final SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;

    private Event.UserInfoEvent userInfoEvent;

    //手机号
    private String phone;
    //验证码
    private String code;

    //手机号
    @BindView(R.id.et_login_phone)
    EditText mEtPhone;

    //验证码
    @BindView(R.id.et_login_code)
    EditText mEtCode;

    @BindView(R.id.btn_login_code_send)
    StateButton mCodeSend;


    public LoginByCodeFragment() {
        // Required empty public constructor
        //初始化
        mPref = PreferenceManager.getDefaultSharedPreferences(MyApplication.getInstance());
    }

    public static LoginByCodeFragment newInstance() {
        Bundle args = new Bundle();
        LoginByCodeFragment fragment = new LoginByCodeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.login_frag_code, container, false);
        ButterKnife.bind(this, view);

        EventHandler eh = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                mHandler.sendMessage(msg);
            }

        };
        SMSSDK.registerEventHandler(eh);

        return view;
    }

    //当Fragment可见时调用此方法
    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
    }

    @Override
    public boolean onBackPressedSupport() {
        pop();
        return true;
    }

    //发送验证码
    @OnClick(R.id.btn_login_code_send)
    public void loginSendCode(View view) {
        phone = mEtPhone.getText().toString();
        //校验输入框
        if (StringUtils.isBlank(phone)) {
            //获取焦点
            mEtPhone.requestFocus();
            //提示框
            alertDialogUtils.showBasicDialogNoTitle(_mActivity, R.string.phoneIsnull);
            return;
        }
        CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(mCodeSend, 60000, 1000); //倒计时1分钟
        mCountDownTimerUtils.start();
        // 触发操作
        SMSSDK.getVerificationCode("86", phone);
    }

    //登录提交
    @OnClick(R.id.btn_login_submit)
    public void loginSubmit(View view) {

        //获取手机号以及验证码
        phone = mEtPhone.getText().toString();
        code = mEtCode.getText().toString();

        //校验输入框
        if (StringUtils.isBlank(phone)) {
            //获取焦点
            mEtPhone.requestFocus();
            //提示框
            alertDialogUtils.showBasicDialogNoTitle(_mActivity, R.string.phoneIsnull);
            return;
        } else if (StringUtils.isBlank(code)) {
            mEtCode.requestFocus();
            alertDialogUtils.showBasicDialogNoTitle(_mActivity, R.string.codeIsnull);
            return;
        }

        // 触发操作
        SMSSDK.submitVerificationCode("86", phone, code);
    }

    //跳转到密码登录
    @OnClick(R.id.btn_login_to_password)
    public void loginToPassword(View view) {
        pop();
        start(LoginByPasswordFragment.newInstance());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //用完回调要注销掉，否则可能会出现内存泄露
        SMSSDK.unregisterAllEventHandler();
    }

    //处理验证码发送以及验证之后的逻辑
    private class MyHandler extends Handler {
        private final WeakReference<LoginByCodeFragment> mFragment;

        public MyHandler(LoginByCodeFragment loginByCodeFragment) {
            mFragment = new WeakReference<LoginByCodeFragment>(loginByCodeFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            LoginByCodeFragment fragment = mFragment.get();
            if (fragment != null) {
                int event = msg.arg1;
                int result = msg.arg2;
                Object data = msg.obj;

                if (result == SMSSDK.RESULT_COMPLETE) {
                    if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        Toast.makeText(MyApplication.getInstance(), R.string.codeSendSuccess, Toast.LENGTH_SHORT).show();
                    } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //检查是否存在该用户
                        Network.getUserApi().getUser(phone)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .as(bindLifecycle())
                                .subscribe(new Consumer<ResponseResult<User>>() {
                                    @Override
                                    public void accept(@NonNull ResponseResult responseResult) throws Exception {
                                        String code = responseResult.getCode();
                                        //code等于200时为查询成功
                                        //如果Data不为空，则有用户，直接进行登录，否则跳转到设置密码页面
                                        if ("200".equals(code) && responseResult.getData() != null) {
                                            userInfoEvent = new Event.UserInfoEvent();
                                            userInfoEvent.user = (User) responseResult.getData();
                                            EventBusActivityScope.getDefault(_mActivity).postSticky(userInfoEvent);
                                            doLogin(phone);
                                        } else {
                                            start(LoginSettingPasswordFragment.newInstance());
                                        }
                                    }
                                }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(@NonNull Throwable throwable) throws Exception {
                                        alertDialogUtils.showBasicDialogNoTitle(_mActivity, R.string.networkError);
                                    }
                                });
                    }
                } else if (result == SMSSDK.RESULT_ERROR) {
                    alertDialogUtils.showBasicDialogNoTitle(_mActivity, R.string.codeIsError);
                }
            }
        }
    }

    //执行登录
    private void doLogin(String phone) {
        Network.getUserApi().doLoginByCode(phone)
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

    protected <T> AutoDisposeConverter<T> bindLifecycle() {
        return RxLifecycleUtils.bindLifecycle(this);
    }
}
