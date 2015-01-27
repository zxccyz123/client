package com.dd.ui;

import java.io.File;
import java.io.FileNotFoundException;

import com.dd.bean.Client;
import com.dd.config.DataConfig;
import com.dd.dao.ClientDao;
import com.dd.utils.CommonUtils;
import com.dd.utils.JsonUtils;
import com.dd.widget.CircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ta.util.http.AsyncHttpClient;
import com.ta.util.http.AsyncHttpResponseHandler;
import com.ta.util.http.RequestParams;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PersonActivity extends Activity implements OnClickListener {

	private Button back_btn;

	private TextView name_tv;

	private TextView gender_tv;

	private TextView phone_tv;

	private TextView address_tv;

	private CircleImageView head_iv;

	private RelativeLayout layout;

	private Client client;

	private ClientDao clientDao;

	private SharedPreferences sp;

	private static final int TAKE_PICTURE = 0x000000;

	private String path = "";

	private AsyncHttpClient asyncHttpClient;

	private DisplayImageOptions options;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_person);

		initView();

		initData();
	}

	@SuppressWarnings("deprecation")
	private void initView() {
		client = new Client();
		clientDao = new ClientDao(this);
		sp = getSharedPreferences(DataConfig.SHARE_DATA_NAME, MODE_PRIVATE);
		back_btn = (Button) findViewById(R.id.person_back);
		name_tv = (TextView) findViewById(R.id.person_name);
		gender_tv = (TextView) findViewById(R.id.person_sex);
		phone_tv = (TextView) findViewById(R.id.person_phone);
		head_iv = (CircleImageView) findViewById(R.id.person_head);
		address_tv = (TextView) findViewById(R.id.person_address);
		layout = (RelativeLayout) findViewById(R.id.person_content);
		asyncHttpClient = new AsyncHttpClient();
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.head)
				.showImageOnFail(R.drawable.head).cacheInMemory(true)
				.cacheOnDisc(true).build();
	}

	private void initData() {
		client = clientDao.find(sp.getString("account", ""));
		back_btn.setOnClickListener(PersonActivity.this);
		name_tv.setText(client.getName());
		gender_tv.setText(CommonUtils.getGender(client.getGender()));
		phone_tv.setText(client.getAccount());
		address_tv.setText(client.getAddress());
		head_iv.setOnClickListener(this);
		ImageLoader.getInstance().displayImage(client.getLogo(), head_iv,
				options);
	}

	public class PopupWindows extends PopupWindow {

		@SuppressWarnings("deprecation")
		public PopupWindows(Context mContext, View parent) {

			View view = View
					.inflate(mContext, R.layout.item_popupwindows, null);
			view.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.fade_ins));
			LinearLayout ll_popup = (LinearLayout) view
					.findViewById(R.id.ll_popup);
			ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.push_bottom_in_2));

			setWidth(LayoutParams.MATCH_PARENT);
			setHeight(LayoutParams.MATCH_PARENT);
			setBackgroundDrawable(new BitmapDrawable());
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			showAtLocation(parent, Gravity.BOTTOM, 0, 0);
			update();

			Button bt1 = (Button) view
					.findViewById(R.id.item_popupwindows_camera);
			Button bt2 = (Button) view
					.findViewById(R.id.item_popupwindows_Photo);
			Button bt3 = (Button) view
					.findViewById(R.id.item_popupwindows_cancel);
			bt1.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					photo();
					dismiss();
				}
			});
			bt2.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					// Intent intent = new Intent(PersonActivity.this,
					// LoginActivity.class);
					// startActivity(intent);
					dismiss();
				}
			});
			bt3.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dismiss();
				}
			});

		}
	}

	public void photo() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_MEDIA_TITLE, "TakePhoto");

		String sdDir = null;
		boolean isSDcardExist = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
		if (isSDcardExist)
			sdDir = Environment.getExternalStorageDirectory().getAbsolutePath();
		else
			sdDir = Environment.getRootDirectory().getAbsolutePath();

		String targetDir = sdDir + "/" + "washcar/image";
		File file = new File(targetDir);
		if (!file.exists())
			file.mkdirs();
		path = targetDir + "/" + String.valueOf(System.currentTimeMillis())
				+ ".jpg";
		intent.putExtra(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(path)));
		startActivityForResult(intent, TAKE_PICTURE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PICTURE:
			if (resultCode == RESULT_OK) {
				client.setLogo(path);
				update();
			}
			break;

		default:
			break;
		}
	}

	private String json;

	private int code;

	private Handler mHandler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case DataConfig.NETWORK_CODE:
				code = JsonUtils.getCode(json);
				if (code == DataConfig.SUCCESS_CODE) {
					client = JsonUtils.getClient(json);
					clientDao.update(client);
					ImageLoader.getInstance().displayImage(client.getLogo(),
							head_iv, options);
				}
				System.out.println(json);
				break;

			default:
				break;
			}
			return false;
		}
	});

	private void update() {
		RequestParams params = new RequestParams();
		try {
			params.put("account", client.getAccount());
			params.put("password", client.getPassword());
			params.put("address", client.getAddress());
			params.put("creditID", client.getCreditID());
			params.put("name", client.getName());
			params.put("gender", String.valueOf(client.getGender()));
			params.put("logo", new File(client.getLogo()));
			asyncHttpClient.post(DataConfig.URL + DataConfig.UPDATE_ACTION,
					params, new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(String content) {
							json = content;
							mHandler.sendEmptyMessage(DataConfig.NETWORK_CODE);
						}

						@Override
						public void onProgress(long totalSize,
								long currentSize, long speed) {
							super.onProgress(totalSize, currentSize, speed);
						}
					});
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onBackPressed() {
		this.setResult(RESULT_OK);
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.person_back:
			onBackPressed();
			break;
		case R.id.person_head:
			new PopupWindows(PersonActivity.this, layout);
			break;
		default:
			break;
		}
	}

}
