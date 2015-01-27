package com.dd.ui;

import com.dd.config.DataConfig;
import com.dd.service.MyReceiver;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;

/**
 * 
 * 欢迎界面
 * 
 * @author daidong
 * 
 */
public class WelcomeActivity extends Activity {

	public static boolean isForeground = false;

	private MyReceiver myReceiver;

	private SharedPreferences sp;

	private boolean isFirstIn;

	private Handler handler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			Intent intent = new Intent();
			switch (msg.what) {
			case DataConfig.FAIL_CODE:
				intent.setClass(WelcomeActivity.this, GuideActivity.class);
				startActivity(intent);
				finish();
				break;
			case DataConfig.SUCCESS_CODE:
				intent.setClass(WelcomeActivity.this, GuideActivity.class);
				startActivity(intent);
				finish();
				break;
			default:
				break;
			}

			return false;
		}
	});

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		// 注册JPUSH接收
		registerMessageReceiver();
		init();
	}

	private void registerMessageReceiver() {
		myReceiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		registerReceiver(myReceiver, filter);
	}

	private void init() {

		sp = getSharedPreferences(DataConfig.SHARE_DATA_NAME, MODE_PRIVATE);
		isFirstIn = sp.getBoolean(DataConfig.IS_FIRST_IN, true);
		if (isFirstIn) {
			handler.sendEmptyMessageDelayed(DataConfig.FAIL_CODE,
					DataConfig.DELAY_TIME);
		} else {
			handler.sendEmptyMessageDelayed(DataConfig.SUCCESS_CODE,
					DataConfig.DELAY_TIME);
		}
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(myReceiver);
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		isForeground = true;
		super.onResume();
	}

}
