package com.dvlp.news.ui.platform.http;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.dvlp.news.R;
import com.dvlp.news.ui.AppContext;
import com.dvlp.news.ui.utils.Constant;
import com.dvlp.news.ui.views.NoFrameDialog;
import com.qianyilc.library.http.client.HttpContext;
import com.qianyilc.library.http.client.RequestCallBack;
import com.qianyilc.library.http.client.RequestClient;
import com.qianyilc.library.util.ApkInfoUtils;
import com.qianyilc.library.util.Logger;


import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by liuwei on 15/5/11.
 */
public class HttpRequestClient extends RequestClient {

    private NoFrameDialog updateDialog = null;

    public HttpRequestClient(HttpContext mContext, RequestCallBack callBack, Class<?> genericClass, String urlPath) {
        super(mContext, callBack, genericClass, urlPath);
        setUrlServer(Constant.HTTPS_SERVER);
    }

    public HttpRequestClient(HttpContext mContext, RequestCallBack callBack, String urlPath) {
        super(mContext, callBack, urlPath);
        setUrlServer(Constant.HTTPS_SERVER);
    }

    @Override
    protected void handleExpect(String str, Exception e) {

//        if(e != null){
//            Intent mIntent = new Intent(httpContext.getContext() , ReportExceptionActivity.class);
//            mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            mIntent.putExtra("error" , "message:" + str + ";error:" + e.getMessage());
//            httpContext.getContext().startActivity(mIntent);
//        }
    }

    @Override
    protected void sign() {
        if (params != null) {
            if (!params.containsKey("v")) {
                addParam("v", Constant.INTERFACE_VERSION);
            }
            String[] keys = new String[params.size()];
            int index = 0;
            Iterator iterator = params.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                String key = (String) entry.getKey();
                keys[index] = key;

                index++;
            }

            Arrays.sort(keys);
            StringBuffer sb = new StringBuffer();
            for (String key : keys) {
                String value = params.get(key);
                if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
                    sb.append(key).append(params.get(key));
                }else{
                    params.remove(key);
                }
            }

            sb.append("eaf28de3f170a842485c4a6bfc92e348");
//            Logger.e("加密前"+sb.toString());
//            Logger.e("加密后"+com.qianyilc.library.util.StringUtil.MD5(sb.toString()));
            addParam("sign", com.qianyilc.library.util.StringUtil.MD5(sb.toString()));

        }
    }

    protected boolean catchExpectCode(int code) {
        boolean catched = false;
        switch (code) {
            case -99:
                Logger.e("catchExpectCode=" + code);
                forceUpdate();
                catched = true;
                break;
            case -100:

                catched = true;
                break;
            default:
                catched = false;
                break;
        }
        return catched;
    }

    /**
     * 检测是否有新版本,并强制更新.
     */
    public void forceUpdate(){

    }


    @Override
    public void initParams(Context context) {

        //isDebug = !BuildConfig.release ;

        if (AppContext.user != null)
            addParam("token", AppContext.user.token);
        String appV= ApkInfoUtils.getVersion(context);
        if (appV.contains("-")){
            appV=appV.substring(0,appV.indexOf("-"));
        }
        addParam("app_v",
                appV);
//        addParam("app_key", Constant.APP_KEY);
        addParam("plat", "2");
        addParam("channel", ApkInfoUtils.getChannel(context, "UMENG_CHANNEL"));
    }

    @Override
    public void onLoadCache() {
        super.onLoadCache();
        Toast.makeText(httpContext.getContext() , httpContext.getContext().getString(R.string.net_error1) , Toast.LENGTH_SHORT).show();
    }
}
