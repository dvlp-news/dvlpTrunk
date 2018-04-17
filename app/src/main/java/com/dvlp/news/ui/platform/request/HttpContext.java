package com.dvlp.news.ui.platform.request;

import android.content.Context;
import android.os.Bundle;

import com.avos.avoscloud.AVObject;

import java.util.List;

/**
 * Created by liubaba on 2018/4/8.
 * 请求回调
 */

public interface HttpContext{
    void onSuccess(Context context, Bundle bundle, int type);

    void onSuccess(Context context, List<AVObject> list, int type);

    void onFailed(Context context, Bundle bundle, int type);

    void onEmptyMessage(Context context, Bundle bundle, int type);
}
