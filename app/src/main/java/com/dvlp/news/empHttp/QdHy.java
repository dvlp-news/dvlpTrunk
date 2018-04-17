package com.dvlp.news.empHttp;

import android.os.Parcelable;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

/**
 * Created by liubaba on 2018/4/5.
 */

@AVClassName("QdHy")
public class QdHy extends AVObject implements Parcelable {
    public static final String NAME = "name";
    public static final String MESSAGE = "message";

    private String name;
    private String message;

    public QdHy(){

    }
    public String getName() {
        return getString(NAME);
    }

    public void setName(String name) {
        put(NAME,name);
    }

    public String getMessage() {
        return getString(MESSAGE);
    }

    public void setMessage(String message) {
        put(MESSAGE,message);
    }


}
