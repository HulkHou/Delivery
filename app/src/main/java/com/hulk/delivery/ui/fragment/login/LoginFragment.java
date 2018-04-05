package com.hulk.delivery.ui.fragment.login;

import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.hulk.delivery.base.BaseMainFragment;
import com.hulk.delivery.entity.ResponseResult;
import com.hulk.delivery.retrofit.Network;
import com.hulk.delivery.util.AlertDialogUtils;

import org.apache.commons.lang3.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hulk-out on 2017/9/8.
 */

public class LoginFragment extends BaseMainFragment {

    private View view;
    private static final String TAG = "LoginFragment";
    private boolean mbDisplayFlg = false;
    private AlertDialogUtils alertDialogUtils = AlertDialogUtils.getInstance();

    //SharedPreferences key
    private static final String AUTHORIZATION = "AUTHORIZATION";
    private static final String IS_LOGIN = "IS_LOGIN";
    private final SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;


    //用户名
    private String username;
    //密码
    private String password;

    //用户名
    @BindView(R.id.et_login_username)
    EditText mEtUsername;

    //密码
    @BindView(R.id.et_login_password)
    EditText mEtPassword;

    //显示隐藏密码
    @BindView(R.id.iv_login_password_hide)
    ImageView mIvLoginPasswordHide;

    public LoginFragment() {
        // Required empty public constructor
        //初始化
        mPref = PreferenceManager.getDefaultSharedPreferences(MyApplication.getInstance());
    }

    public static LoginFragment newInstance() {
        Bundle args = new Bundle();
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.login_frag, container, false);
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

    //切换密码明文密文显示
    @OnClick(R.id.iv_login_password_hide)
    public void loginPasswordHide(View view) {
        if (!mbDisplayFlg) {
            mEtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            //解决切换密文之后，输入框光标定位问题
            mEtPassword.setSelection(mEtPassword.getText().length());
            //切换明文密文图片
            mIvLoginPasswordHide.setImageResource(R.mipmap.ic_password_show);
        } else {
            mEtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            mEtPassword.setSelection(mEtPassword.getText().length());
            mIvLoginPasswordHide.setImageResource(R.mipmap.ic_password_hide);
        }
        mbDisplayFlg = !mbDisplayFlg;
        mEtPassword.postInvalidate();
    }

    //登录提交
    @OnClick(R.id.btn_login_submit)
    public void loginSubmit(View view) {

        //获取用户名密码
        username = mEtUsername.getText().toString();
        password = mEtPassword.getText().toString();

        //校验输入框
        if (StringUtils.isBlank(username)) {
            //获取焦点
            mEtUsername.requestFocus();
            //提示框
            alertDialogUtils.showBasicDialogNoTitle(_mActivity, R.string.usernameIsnull);
            return;
        } else if (StringUtils.isBlank(password)) {
            mEtPassword.requestFocus();
            alertDialogUtils.showBasicDialogNoTitle(_mActivity, R.string.passwordIsnull);
            return;
        }

        //请求登录
        Network.getUserApi().doLogin(username, password)
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
