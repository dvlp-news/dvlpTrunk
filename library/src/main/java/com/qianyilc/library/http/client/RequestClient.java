package com.qianyilc.library.http.client;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.qianyilc.library.Bean.CacheBean;
import com.qianyilc.library.util.Logger;

import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by lw on 2015/2/3.
 */
public abstract class RequestClient extends BaseClient {
    public static final int MODE_NO_DATA = 0;

    public static final int MODE_CALLBACK_OBJECT = 2;
    public static final int MODE_CALLBACK_STRING = 5;
    public static final int MODE_CALLBACK_MESSAGE = 6;
    public static final int MODE_CALLBACK_WHOLE_STRING = 1;

    public void setMode(int mode) {
        this.mode = mode;
    }

    protected int mode = MODE_CALLBACK_OBJECT;
    LinkedHashMap<String, Class<?>> classMap = null;
    RequestCallBack callBack;

    Class<?> genericClass = null;

    /**
     * 构造方法
     *
     * @param mContext
     * @param callBack     回调
     * @param genericClass 泛型类
     * @param urlPath      url的Path部分
     */
    public RequestClient(HttpContext mContext, RequestCallBack callBack, Class<?> genericClass, String urlPath) {
        super();
        init(mContext, callBack, genericClass, urlPath);
    }


    /**
     * 构造方法
     *
     * @param mContext
     * @param callBack 回调
     * @param urlPath  url的Path部分
     */
    public RequestClient(HttpContext mContext, RequestCallBack callBack, String urlPath) {
        super();

        init(mContext, callBack, null, urlPath);
    }


    private void init(HttpContext context, RequestCallBack callBack, Class<?> genericClass, String urlPath) {
        httpContext = context;
        this.callBack = callBack;
        this.genericClass = genericClass;
        initParams(context.getContext());
        if (!TextUtils.isEmpty(urlPath))
            addParam("method", urlPath);
    }

    public void addNode(String key, Class<?> class1) {
        if (classMap == null) {
            classMap = new LinkedHashMap<>();
        }
        classMap.put(key, class1);
    }

    @Override
    public void success(String string) {
        if (mode == MODE_CALLBACK_WHOLE_STRING) {
            callBack.onResult(string);
        } else {
            com.alibaba.fastjson.JSONObject jsonObject = filerJsonObject(string);
            if (jsonObject != null) {
                int code = jsonObject.getIntValue("code");
                if (code == 0) {
                    if (isCache && db != null) {//缓存数据
                        try {
                            String method = params.get("method");
                            String app_v = params.get("app_v");

                            CacheBean mBean ;
                            try {
                                mBean = db.selector(CacheBean.class).where(WhereBuilder.b("method", "=", method).and("version", "=", app_v)).findFirst();
                            } catch (Exception e) {
                                e.printStackTrace();
                                mBean = null ;
                            }

                            if (mBean == null){
                                Logger.i("缓存_新建记录");
                                mBean = new CacheBean();
                                mBean.method = method;
                                mBean.version = app_v;
                            }

                            mBean.date = System.currentTimeMillis();
                            mBean.result = string;

                            db.saveOrUpdate(mBean);
                            Logger.i("缓存_存储成功.");
                        } catch (DbException e) {
                            Logger.i("缓存_存储失败." + e.toString());
                            e.printStackTrace();
                        }
                    }
                    switch (mode) {
                        case MODE_NO_DATA:
                            callBack.onResult(null);
                            break;

                        case MODE_CALLBACK_STRING:
                            callBack.onResult(jsonObject.getString("data"));
                            break;

                        case MODE_CALLBACK_OBJECT:
                            String dataString = jsonObject.getString("data");
                            try {
                                if (classMap == null) {
                                    parseJsonToObject(dataString);
                                } else {
                                    parseJsonToObjects(dataString);
                                }
                            } catch (Exception e) {
                                doCatcher(code, jsonObject.getString("message"), e);
                            }
                            break;
                        case MODE_CALLBACK_MESSAGE:
                            callBack.onResult(jsonObject.getString("message"));
                            break;
                    }
                } else if (!catchExpectCode(code)) {
                    doCatcher(code, jsonObject.getString("message"), null);
                }
            }
        }
    }

    protected void handleExpect(String str, Exception e) {
    }

    protected boolean catchExpectCode(int code) {
        return false;
    }

    protected void doCatcher(int code, String str, Exception e) {
        handleExpect(str, e);
        if (unExpectCodeListener != null) {
            unExpectCodeListener.onUnExpectResult(requestId, code, str);
        } else {
            httpContext.onUnExpectResult(requestId, code, str);
        }
    }

    protected void parseJsonToObject(String json) {
        if (TextUtils.isEmpty(json)) {
            callBack.onResult("");
        } else if (json.startsWith("{")) {
            callBack.onResult(JSON.parseObject(json, genericClass));
        } else {
            callBack.onResult(JSON.parseArray(json, genericClass));
        }
    }

    private void parseJsonToObjects(String json) {
        if (classMap.size() > 0) {
            ArrayList list = new ArrayList();
            Iterator<String> iterator = classMap.keySet().iterator();
            com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(json);
            for (Map.Entry<String, Class<?>> entry : classMap.entrySet()) {
                String key = entry.getKey();
                Class<?> class1 = entry.getValue();
                if (class1.isPrimitive() || class1 == String.class || class1 == Integer.class || class1 == Float.class || class1 == Long.class || class1 == Byte.class) {
                    list.add(jsonObject.get(key));
                    continue;
                }
                String nodeString = jsonObject.getString(key);
                if (TextUtils.isEmpty(nodeString)) {
                    list.add(null);
                } else {
                    if (nodeString.startsWith("{")) {

                        list.add(JSON.parseObject(nodeString, class1));
                    } else {

                        list.add(JSON.parseArray(nodeString, class1));
                    }
                }


            }

            if (list.size() == 0) {
                callBack.onResult(null);

            } else if (list.size() == 1) {

                callBack.onResult(list.get(0), null);
            } else {
                callBack.onResult(list.get(0), list.subList(1, list.size()).toArray());
            }

        }

    }

    private com.alibaba.fastjson.JSONObject filerJsonObject(String string) {

        try {
            com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(string);
            if (jsonObject.containsKey("result")) {
                jsonObject = jsonObject.getJSONObject("result");
            }

            return jsonObject;
        } catch (Exception e) {
            if (notifyUiEnable
                    )
                httpContext.onError(requestId, "数据不合法");
            return null;
        }
    }
}
