package com.qianyilc.library.http.client;



/**
 * Created by leju on 2015/2/3.
 */
public interface RequestCallBack <M>{

    void onResult(M m, Object... objects) ;

}
