/**
 * 
 */
package com.qianyilc.library.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;

/*
 * @author 刘伟 E-mail: liuwei1@leju.com
 *
 * @version 创建时间：2013年9月26日 下午8:20:59
 *
 * @类说明：
 */

public class ApkInfoUtils {
	/**
	 * 获取应用的渠道号
	 * @param context
	 * @param channelKey 如果是null或者"" 则自动赋值为UMENG_CHANNEL
	 * @return
	 */
	public static String getChannel(Context context, String channelKey) {
		ApplicationInfo ai;
		try {
			ai = context.getPackageManager().getApplicationInfo(
					context.getPackageName(), PackageManager.GET_META_DATA);
			Bundle bundle = ai.metaData;
			channelKey= TextUtils.isEmpty(channelKey)?"UMENG_CHANNEL":channelKey;
			Object param = bundle.get(channelKey);
			if (param.getClass() == Integer.class) {
				return param.toString();
			} else {
				return param.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "UMENG_CHANNEL";
	}
	/**
	 * 获取版本name
	 * 
	 * @return 当前应用的版本号(versionName)
	 */
	public static String getVersion(Context context) {
		if (context != null) {
			try {
				PackageManager manager = context.getPackageManager();
				PackageInfo info = manager.getPackageInfo(
						context.getPackageName(), 0);

				return info.versionName;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	/**
	 * 获取版本Code
	 * 
	 * @param context
	 * @return
	 */
	public static int getVersionCode(Context context) {
		if (context != null) {
			try {
				PackageManager manager = context.getPackageManager();
				PackageInfo info = manager.getPackageInfo(
						context.getPackageName(), 0);
				return info.versionCode;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

}
