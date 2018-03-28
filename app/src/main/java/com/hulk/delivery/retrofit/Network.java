package com.hulk.delivery.retrofit;

/**
 * Created by hulk-out on 2017/12/8.
 */


import com.hulk.delivery.MyApplication;
import com.hulk.delivery.retrofit.api.UserApi;

import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Network {
    private static UserApi userApi;
    private static OkHttpClient okHttpClient = MyApplication.getClient();
    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJava2CallAdapterFactory.create();
    private static final String BASE_URL = "http://10.64.4.48:2022/";

    public static UserApi getUserApi() {
        if (userApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(BASE_URL)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            userApi = retrofit.create(UserApi.class);
        }
        return userApi;
    }

}
