package com.hulk.delivery.ui.fragment.profile.child;

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

import com.afollestad.materialdialogs.MaterialDialog;
import com.hulk.delivery.MyApplication;
import com.hulk.delivery.R;
import com.hulk.delivery.entity.ResponseResult;
import com.hulk.delivery.retrofit.Network;
import com.hulk.delivery.util.AlertDialogUtils;
import com.hulk.delivery.util.RxLifecycleUtils;
import com.hulk.delivery.util.StringUtilsCustomize;
import com.uber.autodispose.AutoDisposeConverter;

import org.apache.commons.lang3.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.yokeyword.fragmentation.SupportFragment;


/**
 * Created by hulk-out on 2017/9/8.
 */

public class PasswordFragment extends SupportFragment {

    private View view;
    private MaterialDialog.Builder builder;
    private MaterialDialog dialog;

    //SharedPreferences key
    private static final String AUTHORIZATION = "AUTHORIZATION";
    private static final String IS_LOGIN = "IS_LOGIN";
    private final SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;

    private AlertDialogUtils alertDialogUtils = AlertDialogUtils.getInstance();

    private String passwordOld;
    private String passwordNew;
    private String passwordConfirm;

    @BindView(R.id.et_profile_password_old)
    EditText mPasswordOld;

    @BindView(R.id.iv_profile_password_old_hide)
    ImageView passwordOldHide;

    @BindView(R.id.et_profile_password_new)
    EditText mPasswordNew;

    @BindView(R.id.iv_profile_password_new_hide)
    ImageView passwordNewHide;

    @BindView(R.id.et_profile_password_confirm)
    EditText mPasswordConfirm;

    @BindView(R.id.iv_profile_password_confirm_hide)
    ImageView passwordConfirmHide;

    public PasswordFragment() {
        // Required empty public constructor
        //初始化
        mPref = PreferenceManager.getDefaultSharedPreferences(MyApplication.getInstance());
    }

    public static PasswordFragment newInstance() {

        Bundle args = new Bundle();
        PasswordFragment fragment = new PasswordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.profile_frag_password, container, false);
        ButterKnife.bind(this, view);
        initToolbar();
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

    @OnClick(R.id.iv_profile_password_old_hide)
    public void passwordOldHide() {
        if (passwordOldHide.getTag().equals("密文")) {
            passwordOldHide.setTag("明文");
            passwordOldHide.setImageResource(R.mipmap.ic_password_show);
            mPasswordOld.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else if (passwordOldHide.getTag().equals("明文")) {
            passwordOldHide.setTag("密文");
            passwordOldHide.setImageResource(R.mipmap.ic_password_hide);
            mPasswordOld.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }

    @OnClick(R.id.iv_profile_password_new_hide)
    public void passwordNewHide() {
        if (passwordNewHide.getTag().equals("密文")) {
            passwordNewHide.setTag("明文");
            passwordNewHide.setImageResource(R.mipmap.ic_password_show);
            mPasswordNew.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else if (passwordNewHide.getTag().equals("明文")) {
            passwordNewHide.setTag("密文");
            passwordNewHide.setImageResource(R.mipmap.ic_password_hide);
            mPasswordNew.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }

    @OnClick(R.id.iv_profile_password_confirm_hide)
    public void passwordConfirmHide() {
        if (passwordConfirmHide.getTag().equals("密文")) {
            passwordConfirmHide.setTag("明文");
            passwordConfirmHide.setImageResource(R.mipmap.ic_password_show);
            mPasswordConfirm.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else if (passwordConfirmHide.getTag().equals("明文")) {
            passwordConfirmHide.setTag("密文");
            passwordConfirmHide.setImageResource(R.mipmap.ic_password_hide);
            mPasswordConfirm.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }

    @OnClick(R.id.btn_profile_password_submit)
    public void passwordSubmit() {
        passwordOld = mPasswordOld.getText().toString();
        passwordNew = mPasswordNew.getText().toString();
        passwordConfirm = mPasswordConfirm.getText().toString();
        //校验输入框
        if (StringUtils.isBlank(passwordOld)) {
            //获取焦点
            mPasswordOld.requestFocus();
            //提示框
            alertDialogUtils.showBasicDialogNoTitle(_mActivity, R.string.oldPasswordIsNull);
            return;
        }
        if (StringUtils.isBlank(passwordNew)) {
            //获取焦点
            mPasswordNew.requestFocus();
            //提示框
            alertDialogUtils.showBasicDialogNoTitle(_mActivity, R.string.newPasswordIsNull);
            return;
        }
        if (!StringUtilsCustomize.isAlphanumeric(passwordNew)) {
            //获取焦点
            mPasswordNew.requestFocus();
            //提示框
            alertDialogUtils.showBasicDialogNoTitle(_mActivity, R.string.passwordIsAlphanumeric);
            return;
        }
        if (StringUtils.isBlank(passwordConfirm)) {
            //获取焦点
            mPasswordConfirm.requestFocus();
            //提示框
            alertDialogUtils.showBasicDialogNoTitle(_mActivity, R.string.confirmPasswordIsNull);
            return;
        }
        if (!StringUtilsCustomize.isAlphanumeric(passwordConfirm)) {
            //获取焦点
            mPasswordConfirm.requestFocus();
            //提示框
            alertDialogUtils.showBasicDialogNoTitle(_mActivity, R.string.passwordIsAlphanumeric);
            return;
        }
        if (!passwordNew.equals(passwordConfirm)) {
            //获取焦点
            mPasswordConfirm.requestFocus();
            //提示框
            alertDialogUtils.showBasicDialogNoTitle(_mActivity, R.string.passwordIsDifferent);
            return;
        }

        //获取token
        String authorization = Network.getAuthorization();

        Network.getUserApi().doChangePassword(authorization, passwordOld, passwordNew, passwordConfirm)
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
                        alertDialogUtils.showBasicDialogNoTitle(_mActivity, R.string.networkError);
                    }
                });
    }

    @OnClick(R.id.btn_profile_password_reset)
    public void passwordReset() {
        start(PasswordResetFragment.newInstance());
    }

    protected <T> AutoDisposeConverter<T> bindLifecycle() {
        return RxLifecycleUtils.bindLifecycle(this);
    }
}
