package lczq.leancloud;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SaveCallback;

/**
 * Created by liubaba on 2018/4/8.
 */

public  class MSaveCallback extends SaveCallback {

    @Override
    public void done(AVException e) {
        if (e == null) {
            Log.d("saved", "success!");
        } else {
            Log.d("saved", "erro" + e.getMessage());
        }
    }


}
