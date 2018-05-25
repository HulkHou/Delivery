package com.hulk.delivery.ui.fragment.profile.child;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.hulk.delivery.MyApplication;
import com.hulk.delivery.R;
import com.hulk.delivery.entity.ResponseResult;
import com.hulk.delivery.retrofit.Network;
import com.hulk.delivery.util.AlertDialogUtils;
import com.hulk.delivery.util.CountDownTimerUtils;
import com.hulk.delivery.util.LoginUtil;
import com.hulk.delivery.util.RxLifecycleUtils;
import com.hulk.delivery.util.StateButton;
import com.hulk.delivery.util.StringUtilsCustomize;
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
import me.yokeyword.fragmentation.SupportFragment;


/**
 * Created by hulk-out on 2017/9/8.
 */

public class PasswordResetFragment extends SupportFragment {

    private View view;

    private final MyHandler mHandler = new MyHandler(this);

    //SharedPreferences key
    private static final String AUTHORIZATION = "AUTHORIZATION";
    private static final String IS_LOGIN = "IS_LOGIN";
    private final SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;

    private AlertDialogUtils alertDialogUtils = AlertDialogUtils.getInstance();

    //重置新密码
    private String passwordNew;
    //重置确认密码
    private String passwordConfirm;

    //手机号
    private String phone;
    //验证码
    private String code;

    //手机号
    @BindView(R.id.et_profile_password_reset_phone)
    EditText mEtPhone;

    //验证码
    @BindView(R.id.et_profile_password_reset_code)
    EditText mEtCode;

    //发送验证码按钮
    @BindView(R.id.btn_profile_password_reset_code_send)
    StateButton mCodeSend;

    //重置新密码
    @BindView(R.id.et_profile_password_reset_new)
    EditText mPasswordResetNew;

    //重置新密码明文密文图片
    @BindView(R.id.iv_profile_password_reset_new_hide)
    ImageView passwordResetNewHide;

    //重置确认密码
    @BindView(R.id.et_profile_password_reset_confirm)
    EditText mPasswordResetConfirm;

    //重置确认密码明文密文图片
    @BindView(R.id.iv_profile_password_reset_confirm_hide)
    ImageView passwordResetConfirmHide;

    public PasswordResetFragment() {
        // Required empty public constructor
        //初始化
        mPref = PreferenceManager.getDefaultSharedPreferences(MyApplication.getInstance());
    }

    public static PasswordResetFragment newInstance() {

        Bundle args = new Bundle();
        PasswordResetFragment fragment = new PasswordResetFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.profile_frag_password_reset, container, false);
        ButterKnife.bind(this, view);
        initToolbar();

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

    //初始化工具栏
    private void initToolbar() {
        //设置 Toolbar 相关操作
    }

    @Override
    public boolean onBackPressedSupport() {
        pop();
        return true;
    }

    //切换重置新密码明文密文
    @OnClick(R.id.iv_profile_password_reset_new_hide)
    public void passwordResetNewHide() {
        if (passwordResetNewHide.getTag().equals("密文")) {
            passwordResetNewHide.setTag("明文");
            passwordResetNewHide.setImageResource(R.mipmap.ic_password_show);
            mPasswordResetNew.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else if (passwordResetNewHide.getTag().equals("明文")) {
            passwordResetNewHide.setTag("密文");
            passwordResetNewHide.setImageResource(R.mipmap.ic_password_hide);
            mPasswordResetNew.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }

    //切换确认密码明文密文
    @OnClick(R.id.iv_profile_password_reset_confirm_hide)
    public void passwordResetConfirmHide() {
        if (passwordResetConfirmHide.getTag().equals("密文")) {
            passwordResetConfirmHide.setTag("明文");
            passwordResetConfirmHide.setImageResource(R.mipmap.ic_password_show);
            mPasswordResetConfirm.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else if (passwordResetConfirmHide.getTag().equals("明文")) {
            passwordResetConfirmHide.setTag("密文");
            passwordResetConfirmHide.setImageResource(R.mipmap.ic_password_hide);
            mPasswordResetConfirm.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }

    //发送验证码
    @OnClick(R.id.btn_profile_password_reset_code_send)
    public void passwordResetSendCode(View view) {
        phone = mEtPhone.getText().toString();
        //校验输入框
        if (StringUtils.isBlank(phone)) {
            //获取焦点
            mEtPhone.requestFocus();
            //提示框
            alertDialogUtils.showBasicDialogNoTitle(_mActivity, R.string.phoneIsnull);
            return;
        }
        //发送验证码倒计时1分钟
        CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(mCodeSend, 60000, 1000);
        mCountDownTimerUtils.start();
        //请求验证码
        SMSSDK.getVerificationCode("86", phone);
    }

    //提交重置密码
    @OnClick(R.id.btn_profile_password_reset_submit)
    public void passwordResetSubmit() {
        //获取新密码以及确认密码
        passwordNew = mPasswordResetNew.getText().toString();
        passwordConfirm = mPasswordResetConfirm.getText().toString();

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

        //校验输入框
        if (StringUtils.isBlank(passwordNew)) {
            //获取焦点
            mPasswordResetNew.requestFocus();
            //提示框
            alertDialogUtils.showBasicDialogNoTitle(_mActivity, R.string.newPasswordIsNull);
            return;
        }
        if (!StringUtilsCustomize.isAlphanumeric(passwordNew)) {
            //获取焦点
            mPasswordResetNew.requestFocus();
            //提示框
            alertDialogUtils.showBasicDialogNoTitle(_mActivity, R.string.passwordIsAlphanumeric);
            return;
        }
        if (StringUtils.isBlank(passwordConfirm)) {
            //获取焦点
            mPasswordResetConfirm.requestFocus();
            //提示框
            alertDialogUtils.showBasicDialogNoTitle(_mActivity, R.string.confirmPasswordIsNull);
            return;
        }
        if (!StringUtilsCustomize.isAlphanumeric(passwordConfirm)) {
            //获取焦点
            mPasswordResetConfirm.requestFocus();
            //提示框
            alertDialogUtils.showBasicDialogNoTitle(_mActivity, R.string.passwordIsAlphanumeric);
            return;
        }
        if (!passwordNew.equals(passwordConfirm)) {
            //获取焦点
            mPasswordResetConfirm.requestFocus();
            //提示框
            alertDialogUtils.showBasicDialogNoTitle(_mActivity, R.string.passwordIsDifferent);
            return;
        }

        //验证验证码
        SMSSDK.submitVerificationCode("86", phone, code);
    }

    //处理验证码发送以及验证之后的逻辑
    private class MyHandler extends Handler {
        private final WeakReference<PasswordResetFragment> mFragment;

        public MyHandler(PasswordResetFragment passwordResetFragment) {
            mFragment = new WeakReference<PasswordResetFragment>(passwordResetFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            PasswordResetFragment fragment = mFragment.get();
            if (fragment != null) {
                int event = msg.arg1;
                int result = msg.arg2;
                Object data = msg.obj;

                if (result == SMSSDK.RESULT_COMPLETE) {
                    if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        Toast.makeText(MyApplication.getInstance(), R.string.codeSendSuccess, Toast.LENGTH_SHORT).show();
                    } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        resetPassword();
                    }
                } else if (result == SMSSDK.RESULT_ERROR) {
                    alertDialogUtils.showBasicDialogNoTitle(_mActivity, R.string.codeIsError);
                }
            }
        }
    }

    //重置密码
    private void resetPassword() {
        //获取token
        String authorization = LoginUtil.getAuthorization();
        //请求重置密码API
        Network.getUserApi().doResetPassword(authorization, passwordNew, passwordConfirm)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(bindLifecycle())
                .subscribe(new Consumer<ResponseResult>() {
                    @Override
                    public void accept(@NonNull ResponseResult responseResult) throws Exception {
                        String code = responseResult.getCode();
                        //status等于0时为查询成功
                        if ("200".equals(code)) {
                            String authorization = responseResult.getData().toString();
                            //设置token
                            mEditor = mPref.edit();
                            mEditor.putString(AUTHORIZATION, authorization);
                            mEditor.putBoolean(IS_LOGIN, true);
                            mEditor.commit();//提交修改

                            alertDialogUtils.showBasicDialogNoTitle(_mActivity, R.string.changeSuccess);
                            popTo(ProfileHomeFragment.class, false);
                        } else if ("400".equals(code)) {
                            alertDialogUtils.showBasicDialogNoTitle(_mActivity, R.string.oldPasswordFailure);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        alertDialogUtils.showBasicDialogNoTitle(_mActivity, R.string.loginAgain);
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //用完回调要注销掉，否则可能会出现内存泄露
        SMSSDK.unregisterAllEventHandler();
    }

    protected <T> AutoDisposeConverter<T> bindLifecycle() {
        return RxLifecycleUtils.bindLifecycle(this);
    }
}
