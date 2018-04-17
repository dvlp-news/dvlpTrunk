package com.dvlp.news.ui.platform.presenter;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.dvlp.news.empHttp.todayMessage;
import com.dvlp.news.ui.platform.entity.ReadBean;
import com.dvlp.news.ui.platform.request.HttpContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liubaba on 2018/4/8.
 */

public class ReadPresenter {

    private HttpContext mInstance;
    private Context mContext;
    public ReadPresenter( Context context,HttpContext instance){
        this.mInstance=instance;
        this.mContext=context;
    }
    public void requestHy(){
//        AVQuery<todayMessage> query = AVQuery.getQuery(todayMessage.class);

        AVQuery<AVObject> query = new AVQuery<>("QdHy");
        query.orderByDescending("createdAt")
                .limit(4);

        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {

                if (e != null) {
                    mInstance.onFailed(mContext,null,0);
                }else if (list.size()>0){
                    mInstance.onSuccess(mContext,list,0);
                }else {
                    mInstance.onEmptyMessage(mContext,null,0);
                }
            }
        });
    }
}
