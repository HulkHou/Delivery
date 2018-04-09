package com.hulk.delivery;

import android.app.Application;

import com.boma.boma365.MyEventBusIndex;
import com.mob.MobSDK;

import org.greenrobot.eventbus.EventBus;

import me.yokeyword.fragmentation.Fragmentation;
import me.yokeyword.fragmentation.helper.ExceptionHandler;


/**
 * Created by hulk-out on 2017/10/24.
 */

public class MyApplication extends Application {

    private static MyApplication app;

    @Override
    public void onCreate() {
        super.onCreate();
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
        MobSDK.init(this);
    }

    public static MyApplication getInstance() {
        return app;
    }
}
