package com.qianyilc.library.http.client;

import android.content.Context;

/**
 * @author
 *
 */
public interface HttpContext {
    /**
     * 获取上下文
     * @return
     */
    Context getContext();


    void onRequestStart(int requestId);

    /**
     * 结束请求
     * @param requestId
     */
    void onRequestFinish(int requestId);

    /**
     * 解析json数据时候出错 通常是由json数据格式有误引起  connectError
     * @param requestId
     * @param info
     */
    void onError(int requestId, String info);

    /**
     * 非期望的结果，因为某种原因导致本次操作失败。codeError
     * @param requestId
     * @param resultCode  错误码
     * @param info  错误的原因
     */
    void onUnExpectResult(int requestId, int resultCode, String info);



}
