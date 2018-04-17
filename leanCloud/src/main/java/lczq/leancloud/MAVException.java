package lczq.leancloud;


import com.avos.avoscloud.AVException;

/**
 * Created by liubaba on 2018/4/8.
 */

public class MAVException extends AVException {
    public MAVException(int theCode, String theMessage) {
        super(theCode, theMessage);
    }

    public MAVException(String message, Throwable cause) {
        super(message, cause);
    }

    public MAVException(Throwable cause) {
        super(cause);
    }
}
