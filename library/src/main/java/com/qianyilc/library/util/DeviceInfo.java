package com.qianyilc.library.util;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * 设备的基础信息类
 * 
 * @author 刘伟 E-mail: liuwei1@leju.com
 * @version 创建时间： 2013-4-22 下午4:38:53
 * 
 */
public class DeviceInfo {
	public static final int WIFI_CONNECT = 0;
	public static final int NO_CONNECT = -1;
	public static final int CM_WAP = 11;
	public static final int CM_NET = 12;
	public static final int WAP_3G = 21;
	public static final int NET_3G = 22;
	public static final int UNI_WAP = 31;
	public static final int UNI_NET = 32;
	public static final int CT_WAP = 41;
	public static final int CT_NET = 42;

	private static final String WIFI = "wifi";
	public static final String CMWAP = "cmwap";
	private static final String CMNET = "cmnet";
	public static final String WAP3G = "3gwap";
	private static final String NET3G = "3gnet";
	public static final String UNIWAP = "uniwap";
	private static final String UNINET = "uninet";
	public static final String CTWAP = "ctwap";
	private static final String CTNET = "ctnet";

	public static final Uri PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn");

	// 获取手机的mac地址
	public static String getMac(Context context) {
		WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

		WifiInfo info = wifi.getConnectionInfo();

		return info.getMacAddress();

	}
   /**
    * 获取屏幕大小
    * @param context
    * @return
    */
	public static Point getScreeSize(Context context) {
		Point point = new Point();
		DisplayMetrics dm = new DisplayMetrics();
		dm = context.getResources().getDisplayMetrics();
		point.x = dm.widthPixels;
		point.y = dm.heightPixels;
		return point;

	}
	/**
	 * 获取屏幕密度
	 * @param context
	 * @return
	 */
	public static float getScreeDensity(Context context) {
		
		DisplayMetrics dm = new DisplayMetrics();
		dm = context.getResources().getDisplayMetrics();
		
		return dm.density;
		
	}
	
	/**
	 * 获取手机IMEI号
	 * 
	 * @param context
	 * 
	 *            如果获取手机的IMEI失败，则获取经过MD5验证的mac信息
	 */
	public static String getIMEI(Context context) {
		String imei = null;
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		imei = tm.getDeviceId();
		if (TextUtils.isEmpty(imei)) {
			imei = StringUtil.MD5(getMac(context));
		}
		return imei;
	}

	public static String getPhoneModel(Context context) {
		String phoneModel = Build.MODEL;
		return phoneModel;
	}

	// 获取屏幕尺寸大小，是程序能在不同大小的手机上有更好的兼容性
	public static Point getScreenSize(Context mContext) {
		Point point = new Point();
		WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);

		point.x = wm.getDefaultDisplay().getWidth();// 手机屏幕的宽度
		point.y = wm.getDefaultDisplay().getHeight();// 手机屏幕的高度
		return point;
	}

	/**
	 * 检测是否有可用的网络
	 * 
	 * @param context
	 * @return
	 */
	public static boolean checkNetwork(Context context) {
		boolean flag = false;
		ConnectivityManager cwjManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cwjManager.getActiveNetworkInfo() != null) {
			flag = cwjManager.getActiveNetworkInfo().isAvailable();
		}

		return flag;
	}

	/***
	 * 判断Network具体类型（联通移动wap，电信wap，其他net）
	 * 
	 * */
	public static int checkNetworkType(Context mContext) {
		try {
			final ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
			final NetworkInfo mobNetInfoActivity = connectivityManager.getActiveNetworkInfo();

			if (mobNetInfoActivity == null || !mobNetInfoActivity.isAvailable()) {
				return NO_CONNECT;
			} else {
				// NetworkInfo不为null开始判断是网络类型
				int netType = mobNetInfoActivity.getType();
				if (netType == ConnectivityManager.TYPE_WIFI) {
					return WIFI_CONNECT;
				} else if (netType == ConnectivityManager.TYPE_MOBILE) {
					Cursor c = null;
					try {
						c = mContext.getContentResolver().query(PREFERRED_APN_URI, null, null, null, null);
						if (c != null) {
							c.moveToFirst();
							final String user = c.getString(c.getColumnIndex("user"));
							if (!TextUtils.isEmpty(user)) {
								if (user.startsWith(CTWAP)) {
									c.close();
									return CT_WAP;
								} else if (user.startsWith(CTNET)) {
									c.close();
									return CT_NET;
								}
							}
						}
						c.close();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally {
						if (null != c && !c.isClosed()) {
							c.close();
						}
					}

					String netMode = mobNetInfoActivity.getExtraInfo();
					if (netMode != null) {
						// 通过apn名称判断是否是联通和移动wap
						netMode = netMode.toLowerCase();
						if (netMode.equals(CMWAP)) {
							return CM_WAP;
						} else if (netMode.equals(CMNET)) {
							return CM_NET;
						} else if (netMode.equals(UNIWAP)) {
							return UNI_WAP;
						} else if (netMode.equals(UNINET)) {
							return UNI_NET;
						} else if (netMode.equals(NET3G)) {
							return NET_3G;
						} else if (netMode.equals(WAP3G)) {
							return WAP_3G;
						} else if (netMode.equals(CTWAP)) {
							return CT_WAP;
						} else if (netMode.equals(CTNET)) {
							return CT_NET;
						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return NO_CONNECT;
		}
		return NO_CONNECT;
	}

}
