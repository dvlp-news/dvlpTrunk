package lczq.leancloud;

import android.content.Context;
import android.os.Bundle;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;

/**
 * Created by liubaba on 2018/4/7.
 */

public class ModleLean {

    public static ModleLean newInstance(Context context,String appId,String appKey,boolean bool) {
        ModleLean modleLean=new ModleLean();
        // 初始化参数依次为 this, AppId, AppKey

        AVOSCloud.initialize(context,appId,appKey);

        AVOSCloud.setDebugLogEnabled(bool);
        return modleLean;
    }
}
