package lczq.leancloud;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;

/**
 * Created by liubaba on 2018/4/5.
 * 暂时没用
 */

public class MeApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化参数依次为 this, AppId, AppKey
        AVObject.registerSubclass(todayMessage.class);
        AVOSCloud.initialize(this,"17AjuBYajAE1PqQ7pPKqpsUu-gzGzoHsz","krDYwEVOHlVLhTj3OiyERalo");

        AVOSCloud.setDebugLogEnabled(true);
    }
}
