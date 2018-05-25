package com.hulk.delivery.retrofit;

/**
 * Created by hulk-out on 2017/12/8.
 */


import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.hulk.delivery.MyApplication;
import com.hulk.delivery.retrofit.api.GoogleApi;
import com.hulk.delivery.retrofit.api.UserApi;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Network {
    private static UserApi userApi;
    private static GoogleApi googleApi;

    //设置数据解析器
    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJava2CallAdapterFactory.create();
    private static final String BASE_URL = "http://10.64.4.48:2022/";
    private static final String GOOGLE_URL = "https://maps.google.com/";

    private static OkHttpClient okHttpClient;
    private static SharedPrefsCookiePersistor sharedPrefsCookiePersistor;
    private static ClearableCookieJar cookieJar;
    private static final String AUTHORIZATION = "AUTHORIZATION";
    private static MyApplication app = MyApplication.getInstance();
    ;
    private static String authorization;

    public static UserApi getUserApi() {
        if (userApi == null) {
            sharedPrefsCookiePersistor = new SharedPrefsCookiePersistor(app);
            cookieJar = new PersistentCookieJar(new SetCookieCache(), sharedPrefsCookiePersistor);
            okHttpClient = new OkHttpClient.Builder()
                    .cookieJar(cookieJar)
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request = chain.request()
                                    .newBuilder()
                                    .build();
                            return chain.proceed(request);
                        }
                    })
                    .build();

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

    //通过Google 获取实时位置地址
    public static GoogleApi getGoogleApi() {
        if (googleApi == null) {
            okHttpClient = new OkHttpClient.Builder()
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(GOOGLE_URL)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            googleApi = retrofit.create(GoogleApi.class);
        }
        return googleApi;
    }

    public static SharedPrefsCookiePersistor getSharedPrefsCookiePersistor() {
        return sharedPrefsCookiePersistor;
    }

    public static ClearableCookieJar getClearableCookieJar() {
        return cookieJar;
    }

}
