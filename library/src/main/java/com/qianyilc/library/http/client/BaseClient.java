package com.qianyilc.library.http.client;

import android.content.Context;
import android.text.TextUtils;

import com.qianyilc.library.Bean.CacheBean;
import com.qianyilc.library.util.Logger;

import org.xutils.DbManager;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.http.body.FileBody;
import org.xutils.x;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;


/**
 * @author
 */
public abstract class BaseClient implements org.xutils.common.Callback.CommonCallback<String> {
    /**
     * 是否打印日志
     */
    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public boolean notifyUiEnable = true;
    //本次请求的唯一id
    public int requestId;
    //打印日志的tag
    String logTag;
    // http上下文
    protected HttpContext httpContext = null;
    //服务器主机地址
    protected String urlServer;
    // 请求的数据路径
    protected String urlPath;

    CommonCallback.Cancelable cancelable;

    protected boolean isCache = false;

    /**
     * 修改超时时间
     */
    protected boolean isDebug = false;
    protected DbManager db;
    //请求的参数对
//    public RequestParams params = null;
    protected HashMap<String, String> params = new HashMap<>();

    protected HashMap<String, File> fileParams = new HashMap<>();

    protected NetWorkErrorListener netWorkErrorListener = null;

    protected UnExpectCodeListener unExpectCodeListener = null;

    public void setNetWorkErrorListener(NetWorkErrorListener netWorkErrorListener) {
        this.netWorkErrorListener = netWorkErrorListener;
    }

    public void setUnExpectCodeListener(UnExpectCodeListener unExpectCodeListener) {
        this.unExpectCodeListener = unExpectCodeListener;
    }

    public void setLogTag(String tag) {
        this.logTag = tag;
    }

    /**
     * 发送post请求
     */

    public void sendPostRequest() {
        sendRequest();
    }

    /**
     * 发送get请求
     */
    public void sendGetRequest() {
        sendRequest();
    }

    /**
     * 是否缓存该接口返回的数据
     * 如果为true,每次请求成功,会将数据缓存.
     * 当触发onError(默认全部处理为网络异常，应区分Exception/status)事件时,会读取缓存数据.
     */
    public void setCache(boolean isCache) {
        this.isCache = isCache;
        db = x.getDb(new DbManager.DaoConfig());
    }

    //发送一次网络请求
    private void sendRequest() {
        if (httpContext != null)
            httpContext.onRequestStart(requestId);

        if (params != null && !params.containsKey("sign")) {
            sign();
        }

        RequestParams requestParams = new org.xutils.http.RequestParams(urlServer);

/*        if(isDebug){
            requestParams.setConnectTimeout(10*1000);
        }*/

        Iterator iterator = params.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            requestParams.addBodyParameter(key, value);
        }
        if (fileParams != null) {
            iterator = fileParams.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                String key = (String) entry.getKey();
                File value = (File) entry.getValue();
                requestParams.addBodyParameter(key, value, FileBody.getFileContentType(value), value.getName());
            }
        }

        cancelable = x.http().post(requestParams, this);
        printLog();
    }



    protected void sign() {
    }
    private  static  SSLSocketFactory sslSocketFactory=null;
    private  static SSLSocketFactory getSSLSocketFactory(){
        if (sslSocketFactory==null){
            SSLContext context= null;
            try {
                context = SSLContext.getInstance("TLS");
                context.init(null,null,null);
            } catch (
                    Exception e) {
                e.printStackTrace();
            }

           sslSocketFactory= context.getSocketFactory();
        }
       return  sslSocketFactory;

    }
    /**
     * 打印日志
     */
    private void printLog() {
        if (logTag != null && Logger.enabled) {

            String printUrl = urlServer + "?";

            Iterator iter = params.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                printUrl += entry.getKey() + "=" + entry.getValue() + "&";
            }
            Logger.e(logTag + " " + printUrl);
        }
    }

    /**
     * 添加请求参数
     *
     * @param key
     * @param value
     */
    public void addParam(String key, String value) {
        if (value != null)
            params.put(key, value.toString());

    }

    /**
     * 添加请求参数
     *
     * @param key
     * @param value
     */
    public void addParam(String key, int value) {


        params.put(key, String.valueOf(value));

    }

    public abstract void initParams(Context context);


    /**
     * 添加请求参数
     *
     * @param key
     * @param value
     */
    public void addFile(String key, File value) {

        try {
            if (fileParams != null)
                fileParams.put(key, value);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * 停止请求
     */
    public void stop() {
        if (cancelable != null && !cancelable.isCancelled())

            cancelable.cancel();
    }

    public abstract void success(String string);


    public void setUrlServer(String urlServer) {
        this.urlServer = urlServer;
    }

    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;

    }

    @Override
    public void onSuccess(String result) {
        Logger.i("onSuccess");
        String t = result;
        Logger.e(t);
        if (!TextUtils.isEmpty(logTag)) {
            Logger.e(logTag + " " + t);
        }
        if (TextUtils.isEmpty(t)) {
            if (httpContext != null) {
                if (notifyUiEnable)
                    httpContext.onError(requestId, "没有请求到数据");
            }
        } else {
            success(t);
        }
    }

    @Override
    public void onError(Throwable e, boolean isOnCallback) {
        if (loadCache()) {
            return;
        }



        if (netWorkErrorListener != null)

        {
            netWorkErrorListener.onNetworkError(requestId, 0, "网络不太给力，稍后刷新再试吧");

        } else if (httpContext != null && notifyUiEnable)

        {
            httpContext.onError(requestId, "网络不太给力，稍后刷新再试吧");
        }

    }

    @Override
    public void onCancelled(CancelledException cex) {
        onFinished();
    }

    @Override
    public void onFinished() {
        Logger.i("onFinished");
        if (notifyUiEnable)
            httpContext.onRequestFinish(requestId);
    }

    /**
     * 读取缓存操作.如果缓存不存在,读取失败等出现异常,则继续原先逻辑.
     * @return
     */
    public boolean loadCache() {
        if (isCache && db != null) {
            try {
                String method = params.get("method");
                String app_v = params.get("app_v");

                CacheBean mBean = db.selector(CacheBean.class).where(WhereBuilder.b("method", "=", method).and("version", "=", app_v)).findFirst();

                if (mBean != null) {
                    Logger.i("读取缓存数据.--onCache");
                    onLoadCache();
                    onSuccess(mBean.result);
                    return true;
                }
            } catch (DbException e1) {
                e1.printStackTrace();
            }
        }
        return false;
    }

    public void onLoadCache(){

    }


}
