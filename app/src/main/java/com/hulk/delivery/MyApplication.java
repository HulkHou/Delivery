package com.hulk.delivery;

import android.app.Application;

import com.boma.boma365.MyEventBusIndex;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import me.yokeyword.fragmentation.Fragmentation;
import me.yokeyword.fragmentation.helper.ExceptionHandler;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by hulk-out on 2017/10/24.
 */

public class MyApplication extends Application {

    private static MyApplication app;
    private static OkHttpClient okHttpClient;
    private static SharedPrefsCookiePersistor sharedPrefsCookiePersistor;
    private static ClearableCookieJar cookieJar;

    @Override
    public void onCreate() {
        super.onCreate();

        initializeClient();

        Fragmentation.builder()
                // 设置 栈视图 模式为 （默认）悬浮球模式   SHAKE: 摇一摇唤出  NONE：隐藏， 仅在Debug环境生效
                .stackViewMode(Fragmentation.BUBBLE)
                .debug(true) // 实际场景建议.debug(BuildConfig.DEBUG)
                /**
                 * 可以获取到{@link me.yokeyword.fragmentation.exception.AfterSaveStateTransactionWarning}
                 * 在遇到After onSaveInstanceState时，不会抛出异常，会回调到下面的ExceptionHandler
                 */
                .handleException(new ExceptionHandler() {
                    @Override
                    public void onException(Exception e) {
                        // 以Bugtags为例子: 把捕获到的 Exception 传到 Bugtags 后台。
                        // Bugtags.sendException(e);
                    }
                })
                .install();

        app = this;
        EventBus.builder().addIndex(new MyEventBusIndex()).installDefaultEventBus();
    }

    public static MyApplication getInstance() {
        return app;
    }

    private void initializeClient() {
        sharedPrefsCookiePersistor = new SharedPrefsCookiePersistor(this);
        cookieJar = new PersistentCookieJar(new SetCookieCache(), sharedPrefsCookiePersistor);

        okHttpClient = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
                                .addHeader("Authorization", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJwaG9uZSI6IjEzMDExMTEyMjIwIiwiZXhwIjoxNTIyMjMxOTc3fQ.2OukmW3fo3cLMQfPHt8QSTqCcOJUNHZP9xlPcX6Bfaw")
                                .build();

                        return chain.proceed(request);
                    }
                })
                .build();
    }

    public static OkHttpClient getClient() {
        return okHttpClient;
    }

    public static SharedPrefsCookiePersistor getSharedPrefsCookiePersistor() {
        return sharedPrefsCookiePersistor;
    }

    public static ClearableCookieJar getClearableCookieJar() {
        return cookieJar;
    }
}
