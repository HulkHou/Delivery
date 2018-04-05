package com.hulk.delivery.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.hulk.delivery.retrofit.Network;

import static java.lang.Boolean.FALSE;

/**
 * Created by hulk-out on 2017/11/8.
 */

public class LoginUtil {


    private static final String IS_LOGIN = "IS_LOGIN";

    public static boolean checkLogin(Context context) {
        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(context);
        Boolean isLogin = mPref.getBoolean(IS_LOGIN, FALSE);

        return isLogin;
    }

    public static void loginOut(Context context) {
        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor mEditor = mPref.edit();

        mEditor.clear();
        mEditor.commit();//提交修改

        //清除Cookie
        Network.getClearableCookieJar().clear();
    }

    public static void clearLoginStatus(Context context) {
        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor mEditor = mPref.edit();

        mEditor.putBoolean(IS_LOGIN, false);
        mEditor.commit();//提交修改
    }
}
