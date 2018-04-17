/**
 * 
 */
package com.qianyilc.library.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
/*
 * @author 刘伟 E-mail: liuwei1@leju.com      
 *               
 * @version 创建时间：2013年9月26日 下午8:36:48 
 *
 * @类说明：
 */
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

public class StringUtil {
	/**
	 * 对给定的字符串进行MD5加密
	 * 
	 * @param str
	 * @return
	 */
	public static String MD5(String str) {
		if (!TextUtils.isEmpty(str)) {
			MessageDigest messageDigest = null;
			try {
				messageDigest = MessageDigest.getInstance("MD5");
				messageDigest.reset();
				messageDigest.update(str.getBytes("UTF-8"));
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			byte[] byteArray = messageDigest.digest();
			StringBuffer md5StrBuff = new StringBuffer();
			for (int i = 0; i < byteArray.length; i++) {
				if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
					md5StrBuff.append("0").append(
							Integer.toHexString(0xFF & byteArray[i]));
				} else {
					md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
				}
			}
			return md5StrBuff.toString();
		} else {
			return null;
		}
	}

	/**
	 * 验证字符串是否为手机号
	 * 
	 * @param mobile
	 * @return
	 */
	public static boolean isMobile(String mobile) {
		if (!TextUtils.isEmpty(mobile)) {
			mobile = mobile.trim();
			String pattern = "^[1]\\d{10}$";
			return mobile.matches(pattern);
		}
		return false;
	}

	/**
	 * 设置textview某一区间显示不同颜色的文字
	 * 
	 * @param startIndex
	 * @param endIndex
	 * @param color
	 * @param textView
	 */
	public static void setTextIntervalColor(int startIndex, int endIndex,
			int color, TextView textView) {
		SpannableString sp = new SpannableString(textView.getText().toString());
		sp.setSpan(new ForegroundColorSpan(color), startIndex, endIndex,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		textView.setText(sp);

	}

}
