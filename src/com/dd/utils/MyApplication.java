package com.dd.utils;

import java.io.File;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;

import cn.jpush.android.api.JPushInterface;
import android.app.Application;
import android.app.Service;
import android.os.Vibrator;
import android.util.Log;
import android.widget.TextView;

public class MyApplication extends Application {

	private static final String TAG = "JPush";

	

	public TextView mLocationResult, logMsg;
	public TextView trigger, exit;
	public Vibrator mVibrator;

	@Override
	public void onCreate() {
		Log.d(TAG, "[ExampleApplication] onCreate");
		super.onCreate();

		JPushInterface.setDebugMode(true); // 开启debug模式
		JPushInterface.init(this); // 初始化JPush

		File cacheDir = StorageUtils.getOwnCacheDirectory(this,
				"YST/image/Cache");

		@SuppressWarnings("deprecation")
		ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(
				this).threadPoolSize(3)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCache(new UnlimitedDiscCache(cacheDir)).writeDebugLogs()
				.build();

		ImageLoader.getInstance().init(configuration);

	

		mVibrator = (Vibrator) getApplicationContext().getSystemService(
				Service.VIBRATOR_SERVICE);
	}

	

}
