package com.dd.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * 常用工具类
 * 
 * @author daidong
 * 
 */
public class CommonUtils {

	/**
	 * 验证身份证
	 * 
	 * @param idCard
	 * @return
	 */
	public static boolean checkIdCard(String idCard) {
		String regex = "[1-9]\\d{13,16}[a-zA-Z0-9]{1}";
		return Pattern.matches(regex, idCard);
	}

	/**
	 * 验证手机号码格式
	 * 
	 * @param num
	 * @return
	 */
	public static boolean checkMobileNum(String num) {
		String regex = "(\\+\\d+)?1[3458]\\d{9}$";
		return Pattern.matches(regex, num);

	}

	/**
	 * 获取当前时间
	 * 
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getTime() {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String time = "";
		time = format.format(new Date());
		return time;
	}

	public static String getGender(int i) {
		String gender = "";
		switch (i) {
		case 1:
			gender = "男";
			break;
		case 2:
			gender = "女";
			break;
		default:
			break;
		}
		return gender;
	}

	/**
	 * 验证字符串是否为空
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isEmpty(String s) {
		if (null == s)
			return true;
		if (s.length() == 0)
			return true;
		if (s.trim().length() == 0)
			return true;
		return false;
	}

	/**
	 * 校验Tag Alias 只能是数字,英文字母和中文
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isValidTagAndAlias(String s) {
		Pattern p = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_-]{0,}$");
		Matcher m = p.matcher(s);
		return m.matches();
	}

	/**
	 * 校验网络连接
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isConnected(Context context) {
		ConnectivityManager conn = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = conn.getActiveNetworkInfo();
		return (info != null && info.isConnected());
	}

	/**
	 * 获取手机IMEI
	 * 
	 * @param context
	 * @param imei
	 * @return
	 */
	public static String getImei(Context context, String imei) {
		try {
			TelephonyManager telephonyManager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			imei = telephonyManager.getDeviceId();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return imei;
	}
}
