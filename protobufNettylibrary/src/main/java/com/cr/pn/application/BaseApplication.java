package com.cr.pn.application;

import android.app.Application;
import android.content.Context;

import com.cr.pn.R;
import com.cr.pn.Utils.ViewUtils.AdaptationUtil;
import com.cr.pn.Utils.log.MyLog;
import com.cr.pn.configure.DefaultConfig;
import com.cr.pn.interfaceClass.application.ApplicationInterface;

/**
 * Created by zy on 2018/7/3.
 */

public abstract class BaseApplication extends Application implements ApplicationInterface{

    public static Context sApplicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sApplicationContext = this;
        AdaptationUtil.init(sApplicationContext);
        initConfig();
        initConfigApp();
        MyLog.initLogger("http_log");
    }

    @Override
    public void initConfig() {
        DefaultConfig.activityConfigure .setNavigationBarAlpha(1f)
                .setNavigationColor(DefaultConfig.appDefaultColor)
                .setDefaultBar(true)
                .setBarColor(DefaultConfig.appDefaultColor)
//                .setBarHeight(AdaptationUtil.dip2px(60))
                .setBarHeight(60)
                .setResId(R.layout.actionbar_layout)
                .setTitleBarAlpha(1f)
                .setFullScreen(false)
                .setTitleViewColor(DefaultConfig.appDefaultColor);
    }


}
