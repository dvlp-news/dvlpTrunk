package com.qianyilc.library.http.client;

/**
 * code 不等于0时候的监听
 * Created by liuwei on 15/6/5.
 */
public interface UnExpectCodeListener {

    void onUnExpectResult(int requestId, int code, String info);
}
