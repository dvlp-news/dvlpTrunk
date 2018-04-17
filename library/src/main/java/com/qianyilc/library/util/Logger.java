package com.qianyilc.library.util;

import android.util.Log;

import java.io.Serializable;

/**
 * 日志打印工具类，封装到一起，是为了调用时方便
 * 
 * @author Administrator
 * 
 */
public class Logger {
	private static final String TAG = "leju";

	public static  boolean enabled = true;
   

	public static void v(Serializable message) {

		if (enabled)
			Log.v(TAG, message.toString());
	}

	public static void d(Serializable message) {
		if (enabled)
			Log.d(TAG, message.toString());
	}

	public static void i(Serializable message) {
		if (enabled){
			if(message == null){
				Log.i(TAG, "message is null");
			}else{
				Log.i(TAG, message.toString());
			}
		}
	}

	public static void w(Serializable message) {
		if (enabled)
			Log.w(TAG, message.toString());
	}

	public static void e(Serializable message) {
		if (enabled)
			Log.e(TAG, message == null ? (message + "") : message.toString());
	}

    public static void e(String tag, Serializable message) {
        if (enabled)
            Log.e(TAG, tag + " " + message.toString());
    }

    public static void v(String tag, String msg) {
        if (enabled)
            Log.v(TAG, tag + " " + msg);
    }

    public static void i(String tag, String msg) {
        if (enabled)
            Log.i(TAG, tag + " " + msg);
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (enabled)
            Log.e(TAG, tag + " " + msg + tr.toString());
    }

	

}
