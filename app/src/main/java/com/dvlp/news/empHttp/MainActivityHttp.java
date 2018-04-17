package com.dvlp.news.empHttp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.dvlp.news.R;
import com.dvlp.news.ui.platform.presenter.ReadPresenter;
import com.dvlp.news.ui.platform.request.HttpContext;

import java.util.ArrayList;
import java.util.List;


public class MainActivityHttp extends AppCompatActivity implements View.OnClickListener ,HttpContext {
    private TextView textview1;

    @Override
    public void onSuccess(Context context, List<AVObject> list, int type) {
        Log.e("tad","成功"+list.get(0).getString("ctitle"));

        // object 就是 id 为 558e20cbe4b060308e3eb36c 的 Todo 对象实例
//
//        int priority = avObject.getInt("priority");
//        String location = avObject.getString("location");
//        String title = avObject.getString("title");
//        String content = avObject.getString("content");
//
//        // 获取三个特殊属性
//        String objectId = avObject.getObjectId();
//        Date updatedAt = avObject.getUpdatedAt();
//        Date createdAt = avObject.getCreatedAt();
    }

    private TextView textview2;
    private TextView textview3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_http);

        textview1= (TextView) findViewById(R.id.addNew);
        textview2= (TextView) findViewById(R.id.update);
        textview3= (TextView) findViewById(R.id.select);
        textview1.setOnClickListener(this);
        textview2.setOnClickListener(this);
        textview3.setOnClickListener(this);



        // 测试 SDK 是否正常工作的代码
//        AVObject testObject = new AVObject("TestObject");
//        testObject.put("words","Hello World!");
//        testObject.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(AVException e) {
//                if(e == null){
//                    Log.d("saved","success!");
//                }
//            }
//        });
//
//        AVObject todayMes = new AVObject("todayMessage");
//        todayMes.put("name","Hello World!");
//        todayMes.put("message","Hello World!");
//        todayMes.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(AVException e) {
//                if(e == null){
//                    Log.d("saved","success!");
//                }
//            }
//        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.addNew:
                List<todayMessage> students = new ArrayList<>();
                for (int i = 0; i < 5; i++) {
                    todayMessage student = new todayMessage();
                    student.setName(i + "name");
                    student.setMessage(i + "浅析Android插件化");
                    students.add(student);
                }
                try {
                    AVObject.saveAllInBackground(new ArrayList<AVObject>(students), new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if(e == null){
                                Log.d("saved","success!");
                            }else {
                                Log.d("saved","erro"+e.getMessage());
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.update:
                ReadPresenter presenter=new ReadPresenter(this,this);
                presenter.requestHy();
                break;

            case R.id.select:
                AVQuery<todayMessage> query = AVQuery.getQuery(todayMessage.class);
                query.orderByDescending("createdAt")
                        .limit(5);

                query.findInBackground(new FindCallback<todayMessage>() {
                        @Override
                        public void done(List<todayMessage> list, AVException e) {
                            logObjects(list, todayMessage.MESSAGE);
                        }
                    });





                break;
        }
    }

    protected <T extends AVObject> void logObjects(List<T> objects, String key) {
        StringBuilder sb = new StringBuilder();
        sb.append("一组对象 ");
        sb.append(key);
        sb.append(" 字段的值：\n");
        for (AVObject obj : objects) {
            sb.append(obj.get(key));
            sb.append("\n");
        }
        Log.d("log",sb.toString());
    }

    @Override
    public void onSuccess(Context context, Bundle bundle, int type) {

    }


    @Override
    public void onFailed(Context context, Bundle bundle, int type) {

    }

    @Override
    public void onEmptyMessage(Context context, Bundle bundle, int type) {

    }
}
