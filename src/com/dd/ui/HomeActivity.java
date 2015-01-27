package com.dd.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.dd.adapter.HomeItemAdapter;
import com.dd.bean.Apply;
import com.dd.bean.Client;
import com.dd.config.DataConfig;
import com.dd.dao.ApplyDao;
import com.dd.dao.ClientDao;
import com.dd.utils.CommonUtils;
import com.dd.utils.HttpUtils;
import com.dd.utils.JsonUtils;
import com.dd.utils.T;
import com.dd.widget.CircleImageView;
import com.dd.widget.DragLayout;
import com.dd.widget.DragLayout.DragListener;
import com.dd.widget.SweetAlertDialog;
import com.dd.widget.SweetAlertDialog.OnSweetClickListener;
import com.nineoldandroids.view.ViewHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class HomeActivity extends Activity {

	private long firstTime = 0;

	private DragLayout dragLayout;

	private ListView listView;

	private CircleImageView iv_icon, iv_bottom;

	private HomeItemAdapter adapter;

	private LocationClient mLocationClient;

	private LocationMode tempMode = LocationMode.Hight_Accuracy;

	private String tempcoor = "gcj02";

	public GeofenceClient mGeofenceClient;
	public MyLocationListener mMyLocationListener;

	private EditText loc_et;

	private EditText ill_et;

	private Button send_btn;

	private ImageButton loc_btn;

	private String json;

	private int code;

	private Client client;

	private ClientDao clientDao;

	private SharedPreferences sp;

	private TextView name_tv;

	private Apply apply;

	private ApplyDao applyDao;

	private String account;

	@SuppressWarnings("deprecation")
	private DisplayImageOptions options = new DisplayImageOptions.Builder()
			.cacheOnDisc(true).cacheInMemory(true)
			.showImageForEmptyUri(R.drawable.head)
			.showImageOnFail(R.drawable.head).build();

	private Handler mHandler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case DataConfig.NETWORK_CODE:
				code = JsonUtils.getCode(json);
				if (code == DataConfig.SUCCESS_CODE) {
					applyDao.save(apply);
					new SweetAlertDialog(HomeActivity.this,
							SweetAlertDialog.SUCCESS_TYPE)
							.setTitleText("提示")
							.setContentText("请求成功")
							.setConfirmClickListener(
									new OnSweetClickListener() {

										@Override
										public void onClick(
												SweetAlertDialog sweetAlertDialog) {
											sweetAlertDialog.cancel();
										}
									}).show();
				} else
					new SweetAlertDialog(HomeActivity.this,
							SweetAlertDialog.ERROR_TYPE).setTitleText("提示")
							.setContentText("请求失败").show();

				break;
			case DataConfig.NO_NETWORK_CODE:
				new SweetAlertDialog(HomeActivity.this,
						SweetAlertDialog.ERROR_TYPE).setTitleText("请求失败")
						.setContentText("当前无网络,请检查网络").show();
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
		setContentView(R.layout.activity_home);
		initDragLayout();
		initView();
	}

	private void initDragLayout() {
		dragLayout = (DragLayout) findViewById(R.id.draglayout);
		dragLayout.setDragListener(new DragListener() {
			@Override
			public void onOpen() {
				listView.smoothScrollToPosition(new Random().nextInt(30));
			}

			@Override
			public void onClose() {
				shake();
			}

			@Override
			public void onDrag(float percent) {
				ViewHelper.setAlpha(iv_icon, 1 - percent);
			}
		});
	}

	private void initView() {
		sp = getSharedPreferences(DataConfig.SHARE_DATA_NAME, MODE_PRIVATE);
		client = new Client();
		clientDao = new ClientDao(this);
		applyDao = new ApplyDao(this);
		iv_bottom = (CircleImageView) findViewById(R.id.iv_bottom);
		iv_icon = (CircleImageView) findViewById(R.id.iv_icon);
		listView = (ListView) findViewById(R.id.home_list);
		loc_et = (EditText) findViewById(R.id.home_loc);
		ill_et = (EditText) findViewById(R.id.home_explain);
		send_btn = (Button) findViewById(R.id.send_btn);
		loc_btn = (ImageButton) findViewById(R.id.loc_btn);
		name_tv = (TextView) findViewById(R.id.home_user_name);
		adapter = new HomeItemAdapter(this, getListItem());
		listView.setAdapter(adapter);
		mLocationClient = new LocationClient(this.getApplicationContext());
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);
		mGeofenceClient = new GeofenceClient(getApplicationContext());
		account = sp.getString("account", "");

		client = clientDao.find(account);
		InitLocation();
		mLocationClient.start();
		name_tv.setText(client.getName());
		ImageLoader.getInstance().displayImage(client.getLogo(), iv_icon,
				options);
		ImageLoader.getInstance().displayImage(client.getLogo(), iv_bottom,
				options);
		iv_icon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dragLayout.open();
			}
		});
		iv_bottom.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				T.showShort(HomeActivity.this, "点击了头像");
			}
		});

		loc_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (CommonUtils.isConnected(HomeActivity.this)) {
					mLocationClient.start();
				} else
					mHandler.sendEmptyMessage(DataConfig.NO_NETWORK_CODE);
			}
		});

		listView.setOnItemClickListener(onMenuItemClickListener);
		send_btn.setOnClickListener(onSendClickListener);
	}

	/**
	 * 发送
	 */
	OnClickListener onSendClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			apply = new Apply();
			apply.setLocation(loc_et.getText().toString());
			apply.setIllustrate(ill_et.getText().toString());
			apply.setClientID(account);
			apply.setTime(CommonUtils.getTime());
			send(apply);

		}
	};

	private void send(final Apply apply) {
		if (CommonUtils.isConnected(HomeActivity.this)) {

			new Thread(new Runnable() {
				@Override
				public void run() {
					json = HttpUtils.doPost(DataConfig.SEND_APPLY_ACTION,
							getParams(apply));
					mHandler.sendEmptyMessage(DataConfig.NETWORK_CODE);
				}
			}).start();

		} else
			mHandler.sendEmptyMessage(DataConfig.NO_NETWORK_CODE);
	}

	private List<NameValuePair> getParams(Apply apply) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("clientID", apply.getClientID()));
		params.add(new BasicNameValuePair("location", apply.getLocation()));
		params.add(new BasicNameValuePair("illustrate", apply.getIllustrate()));
		params.add(new BasicNameValuePair("time", apply.getTime()));
		params.add(new BasicNameValuePair("isAccept", String.valueOf(apply
				.getIsAccept())));
		return params;
	}

	private void InitLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(tempMode);//
		option.setCoorType(tempcoor);//
		int span = 1000;
		option.setScanSpan(span);//
		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);
	}

	private final static int PERSON_CODE = 10001;

	OnItemClickListener onMenuItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent = new Intent();
			switch (position) {
			case 0:
				intent.setClass(HomeActivity.this, PersonActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivityForResult(intent, PERSON_CODE);
				break;
			case 1:
				intent.setClass(HomeActivity.this, CarActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				break;
			case 2:
				intent.setClass(HomeActivity.this, RecordActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				break;
			case 3:
				intent.setClass(HomeActivity.this, SetActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				break;
			case 4:
				T.showShort(HomeActivity.this, "我的毕业设计");
				break;
			default:
				break;
			}

		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case PERSON_CODE:
			if (resultCode == RESULT_OK) {
				client = clientDao.find(account);
				ImageLoader.getInstance().displayImage(client.getLogo(),
						iv_icon, options);
				ImageLoader.getInstance().displayImage(client.getLogo(),
						iv_bottom, options);
			}
			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private List<Map<String, Object>> getListItem() {
		List<Map<String, Object>> listItem = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < DataConfig.MENU_NAME.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", DataConfig.MENU_NAME[i]);
			map.put("icon", DataConfig.MENU_ICON[i]);
			listItem.add(map);
		}
		return listItem;
	}

	@Override
	protected void onStop() {
		mLocationClient.stop();
		super.onStop();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			long secondTime = System.currentTimeMillis();
			if (secondTime - firstTime > 2000) {
				T.showShort(HomeActivity.this, "再按一次，退出程序");
				firstTime = secondTime;
				return true;
			} else
				System.exit(0);
		}
		return super.onKeyDown(keyCode, event);
	}

	private void shake() {
		iv_icon.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
	}

	/**
	 * 定位
	 */
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// Receive Location
			String s = "";
			if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
				s = location.getAddrStr();
				mLocationClient.stop();
			}
			loc_et.setText(s);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}

}
