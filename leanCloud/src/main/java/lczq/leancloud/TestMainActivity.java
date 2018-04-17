package lczq.leancloud;

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

import java.util.ArrayList;
import java.util.List;


public class TestMainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView textview1;
    private TextView textview2;
    private TextView textview3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modle_lean_activity_main);
        textview1= (TextView) findViewById(R.id.addNew);
        textview2= (TextView) findViewById(R.id.update);
        textview3= (TextView) findViewById(R.id.select);
        textview1.setOnClickListener(this);
        textview2.setOnClickListener(this);
        textview3.setOnClickListener(this);



//        // 测试 SDK 是否正常工作的代码
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
        int i1 = view.getId();
        if (i1 == R.id.addNew) {
            List<todayMessage> students = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                todayMessage student = new todayMessage();
                student.setName(i + "号开发者");
                student.setMessage(i + "浅析Android插件化");
                students.add(student);
            }
            try {
                AVObject.saveAllInBackground(new ArrayList<AVObject>(students), new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            Log.d("saved", "success!");
                        } else {
                            Log.d("saved", "erro" + e.getMessage());
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (i1 == R.id.update) {
        } else if (i1 == R.id.select) {
            AVQuery<todayMessage> query = AVQuery.getQuery(todayMessage.class);
            query.orderByDescending("createdAt")
                    .limit(5);
            try {
                List<todayMessage> list = query.find();
            } catch (AVException e) {
                e.printStackTrace();
            }
            query.findInBackground(new FindCallback<todayMessage>() {
                @Override
                public void done(List<todayMessage> list, AVException e) {
                    logObjects(list, todayMessage.MESSAGE);
                }
            });


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
}
