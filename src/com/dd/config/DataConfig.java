package com.dd.config;

import com.dd.ui.R;

/**
 * 
 * 数据
 * 
 * @author Administrator
 * 
 */
public class DataConfig {

	public static final String URL = "http://192.168.1.102:8080/Service/CAPP/";

	/** 菜单 **/
	public static final String[] MENU_NAME = new String[] { "个人中心", "汽车信息",
			"洗车记录", "设置", "关于" };

	/** 菜单对应图标 **/
	public static final int[] MENU_ICON = new int[] { R.drawable.menu_user,
			R.drawable.menu_car, R.drawable.menu_record, R.drawable.menu_set,
			R.drawable.menu_about };

	/** 成功返回码 **/
	public static final int SUCCESS_CODE = 0;

	/** 帐号错误 **/
	public static final int ACCOUNT_ERROR_CODE = -1;

	/** 密码错误 **/
	public static final int PASSWORD_ERROR_CODE = -2;

	/** 帐号已存在 **/
	public static final int ACCOUNT_EXIT_CODE = -3;

	/** 网络请求返回码 **/
	public static final int NETWORK_CODE = 9999;

	/** 无网络连接返回码 **/
	public static final int NO_NETWORK_CODE = 10000;

	/** 失败返回码 **/
	public static final int FAIL_CODE = 10001;

	/** 延迟时间 **/
	public static final int DELAY_TIME = 3 * 1000;

	/** 共享数据文件名 **/
	public static final String SHARE_DATA_NAME = "client";

	/** 判断是否是第一次运行程序 **/
	public static final String IS_FIRST_IN = "isFirstIn";

	/** 登录action **/
	public static final String LOGIN_ACTION = "clientlogin";

	/** 注册action **/
	public static final String REGISTER_ACTION = "clientregister";

	/** 完善个人信息action **/
	public static final String COMPLETE_ACTION = "clientcomplete";

	/** 发送洗车请求action **/
	public static final String SEND_APPLY_ACTION = "applysend";

	/** 更新个人信息action **/
	public static final String UPDATE_ACTION = "clientupdate";

	/** 汽车信息action **/
	public static final String SAVE_CAR_ACTION = "carsave";
}
