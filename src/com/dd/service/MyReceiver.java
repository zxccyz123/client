package com.dd.service;

import cn.jpush.android.api.JPushInterface;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {

	private static final String TAG = "JPush";

	@Override
	public void onReceive(Context context, Intent intent) {

		Bundle bundle = intent.getExtras();
		Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction()
				+ ", extras: " + printBundle(bundle));
		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			String regId = bundle
					.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			Log.d(TAG, "[MyReceiver]  Registration Id : " + regId);

		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
				.getAction())) {
			// 接收自定义消息
			Log.d(TAG,
					"[MyReceiver] message: "
							+ bundle.getString(JPushInterface.EXTRA_MESSAGE));
			// processCustomMessage(context, bundle);
			showNotification(context, bundle);
		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
				.getAction())) {
			Log.d(TAG, "[MyReceiver]  notification:");
			int notifactionId = bundle
					.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
			Log.d(TAG, "[MyReceiver] notificationID:  " + notifactionId);

		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
				.getAction())) {
			Log.d(TAG, "[MyReceiver]  open");
			// 打开自定义的Activity
			// Intent i = new Intent(context, FriendActivity.class);
			// i.putExtras(bundle);
			// // i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
			// | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// context.startActivity(i);

		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent
				.getAction())) {
			Log.d(TAG,
					"[MyReceiver] RICH PUSH CALLBACK: "
							+ bundle.getString(JPushInterface.EXTRA_EXTRA));
			// 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity，
			// 打开一个网页等..
		} else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent
				.getAction())) {
			boolean connected = intent.getBooleanExtra(
					JPushInterface.EXTRA_CONNECTION_CHANGE, false);
			Log.w(TAG, "[MyReceiver]" + intent.getAction()
					+ " connected state change to " + connected);
		} else {
			Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
		}

	}

	/**
	 * 在状态栏上显示接收到的自定义消息
	 */
	// @SuppressWarnings({ "static-access", "deprecation" })
	private void showNotification(Context context, Bundle bundle) {
		// NotificationManager manager = (NotificationManager) context
		// .getSystemService(context.NOTIFICATION_SERVICE);
		// 定义notfication属性
		// Notification notification = new Notification(R.drawable.icon,
		// "message : " + bundle.getString(JPushInterface.EXTRA_MESSAGE)
		// + "\n extras: "
		// + bundle.getString(JPushInterface.EXTRA_EXTRA),
		// System.currentTimeMillis());
		// // 将此消息放到通知栏“正在运行”组中
		// notification.flags |= Notification.FLAG_ONGOING_EVENT;
		// notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		// notification.defaults = Notification.DEFAULT_LIGHTS;
		// notification.ledARGB = color.background_light;
		// notification.ledOnMS = 5000;
		//
		// CharSequence title = bundle.getString(JPushInterface.EXTRA_MESSAGE);
		// CharSequence content = bundle.getString(JPushInterface.EXTRA_EXTRA);
		// Intent intent = new Intent(context, FriendActivity.class);
		// PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
		// intent, 0);
		// notification.setLatestEventInfo(context, title, content,
		// contentIntent);
		//
		// manager.notify(0, notification);

	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			} else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}

}
