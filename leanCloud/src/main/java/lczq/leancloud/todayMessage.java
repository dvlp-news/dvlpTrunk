package lczq.leancloud;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

/**
 * Created by liubaba on 2018/4/5.
 */

@AVClassName("todayMessage")
public class todayMessage extends AVObject {
    public static final Creator CREATOR = AVObjectCreator.instance;
    public static final String NAME = "name";
    public static final String MESSAGE = "message";

//    private String name;
//    private String message;

    public todayMessage(){

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
