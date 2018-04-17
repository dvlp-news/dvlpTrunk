package com.dvlp.news.ui;

import android.app.Application;
import android.content.Context;


import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.dvlp.news.empHttp.todayMessage;
import com.dvlp.news.ui.platform.entity.User;
import com.qianyilc.library.util.Logger;
import android.support.multidex.MultiDex;


import org.xutils.x;



/**
 * Created by liubaba on 2017/1/13.
 */

public class AppContext extends Application {
    private static AppContext instance;
    private static Context context;
    public static User user;
    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
       // Logger.enabled = true;//开启打印日志功能
        init();
        initModleLeanCloud();
    }
    private void init() {

        x.Ext.init(this);
        x.Ext.setDebug(Logger.enabled);
        context = getApplicationContext();
    }

    //初始化LeanCloud modle
    private void initModleLeanCloud(){
//        主项目 gradle 配置 maven
//                app 依赖
//                        清单文件  后期改为 HTTP 请求 需要清楚三处
        String appId="17AjuBYajAE1PqQ7pPKqpsUu-gzGzoHsz";
        String appKey="krDYwEVOHlVLhTj3OiyERalo";
        AVObject.registerSubclass(todayMessage.class);


        // 初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(this,"17AjuBYajAE1PqQ7pPKqpsUu-gzGzoHsz","krDYwEVOHlVLhTj3OiyERalo");

        AVOSCloud.setDebugLogEnabled(true);

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    public static Context getContext() {
        return context;
    }

    public static AppContext getInstance() {
        return instance;
    }

}
