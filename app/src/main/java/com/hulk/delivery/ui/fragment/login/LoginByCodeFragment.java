package com.hulk.delivery.ui.fragment.login;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
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
import com.hulk.delivery.util.StateButton;

import org.apache.commons.lang3.StringUtils;

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
//        sendCode("86", phone);
    }

    // 请求验证码，其中country表示国家代码，如“86”；phone表示手机号码，如“13800138000”
    public void sendCode(String country, String phone) {
        // 注册一个事件回调，用于处理发送验证码操作的结果
        SMSSDK.registerEventHandler(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // TODO 处理成功得到验证码的结果
                    // 请注意，此时只是完成了发送验证码的请求，验证码短信还需要几秒钟之后才送达
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Looper.prepare();
                            Toast.makeText(_mActivity, R.string.codeSendSuccess, Toast.LENGTH_SHORT).show();
                            Looper.loop();
                            Looper.myLooper().quit();
                        }
                    }).start();
                } else {
                    // TODO 处理错误的结果
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Looper.prepare();
                            alertDialogUtils.showBasicDialogNoTitle(_mActivity, R.string.codeSendFailure);
                            Looper.loop();
                            Looper.myLooper().quit();
                        }
                    }).start();
                }

            }
        });
        // 触发操作
        SMSSDK.getVerificationCode(country, phone);
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

        submitCode("86", phone, code);
    }

    // 提交验证码，其中的code表示验证码，如“1357”
    public void submitCode(String country, String phone, String code) {
        // 注册一个事件回调，用于处理提交验证码操作的结果
        SMSSDK.registerEventHandler(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // TODO 处理验证成功的结果
                    userInfoEvent = new Event.UserInfoEvent();
                    userInfoEvent.user = new User(phone);
                    EventBusActivityScope.getDefault(_mActivity).postSticky(userInfoEvent);

                    //检查是否存在该用户
                    Network.getUserApi().getUser(phone)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<ResponseResult>() {
                                @Override
                                public void accept(@NonNull ResponseResult responseResult) throws Exception {
                                    String code = responseResult.getCode();
                                    //code等于200时为查询成功
                                    //如果Data不为空，则有用户，直接进行登录，否则跳转到设置密码页面
                                    if ("200".equals(code) && responseResult.getData() != null) {
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
                } else {
                    // TODO 处理错误的结果
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Looper.prepare();
                            alertDialogUtils.showBasicDialogNoTitle(_mActivity, R.string.codeIsError);
                            Looper.loop();
                            Looper.myLooper().quit();
                        }
                    }).start();
                }

            }
        });
        // 触发操作
        SMSSDK.submitVerificationCode(country, phone, code);
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

    //执行登录
    private void doLogin(String phone) {
        Network.getUserApi().doLoginByCode(phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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
}
