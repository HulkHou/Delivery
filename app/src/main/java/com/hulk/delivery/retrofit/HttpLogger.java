package com.hulk.delivery.retrofit;

import android.util.Log;

import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by hulk-out on 2018/6/1.
 */

public class HttpLogger implements HttpLoggingInterceptor.Logger {
    @Override
    public void log(String message) {
        Log.d("HttpLogInfo", message);
    }
}
