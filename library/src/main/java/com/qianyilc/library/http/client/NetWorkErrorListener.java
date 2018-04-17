package com.qianyilc.library.http.client;

/**
 * Created by liuwei on 15/6/5.
 */
public interface NetWorkErrorListener {
    /**
     * 网络链接出错的回掉
     * @param requestId  请求id
     * @param errorId  错误id  0 时为链接失败，链接超时等。 其他是服务器异常错误
     * @param errorInfo   错误的具体原因
     */
    void onNetworkError(int requestId, int errorId, String errorInfo);

}
