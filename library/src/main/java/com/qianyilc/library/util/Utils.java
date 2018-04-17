package com.qianyilc.library.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;

/**
 * @ClassName: Utils
 * @Description: 工具类
 * @author yingjun fan
 * @date 2012-10-29 上午10:24:48
 * 
 * 
 */
@SuppressLint("SimpleDateFormat")
public class Utils {

	
	

	public static String getMapImageUrl(int width, int height, double x,
			double y) {

		StringBuffer sb = new StringBuffer(
				"http://api.map.baidu.com/staticimage?center=");
		sb.append(x).append(",").append(y).append("&width=").append(width/2)
				.append("&height=").append(height/2).append("&zoom=11")
				.append("&markers=").append(x).append(",").append(y)
				.append("&markerStyles=m");
	     return sb.toString();

	}

	
	/**
	 * 获取当前时间
	 * 
	 * @return 当前时间字符串 格式(dd/MM/yyyy HH:mm:ss)
	 */
	public static String getCurFormatDate() {
		SimpleDateFormat mFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return mFormat.format(new Date());
	}
	
	/**
	 * 获取当前时间
	 * 
	 * @return 当前时间字符串 格式(dd/MM/yyyy HH:mm:ss)
	 */
	public static String getFormatDate(long time) {
		SimpleDateFormat mFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return mFormat.format(new Date(time));
	}
	

}
