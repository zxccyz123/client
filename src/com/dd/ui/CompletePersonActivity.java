package com.dd.ui;

import com.dd.bean.Client;
import com.dd.config.DataConfig;
import com.dd.dao.ClientDao;
import com.dd.utils.JsonUtils;
import com.dd.utils.T;
import com.dd.utils.CommonUtils;
import com.ta.util.http.AsyncHttpClient;
import com.ta.util.http.AsyncHttpResponseHandler;
import com.ta.util.http.RequestParams;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;

public class CompletePersonActivity extends Activity implements OnClickListener {

	private TextView finish_tv;

	private EditText name_et;

	private EditText address_et;

	private EditText credit_et;

	private RelativeLayout head_layout;

	private RelativeLayout gender_layout;

	private TextView gender_tv;

	private AlertDialog dialog;

	private String name;

	private String address;

	private String credit;

	private int gender = 1;

	private String json;

	private int code;

	private AsyncHttpClient asyncHttpClient;

	private Client client;

	private ClientDao clientDao;

	private SharedPreferences sp;

	private Handler mHandler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case DataConfig.NO_NETWORK_CODE:
				T.showShort(CompletePersonActivity.this, "当前无网络连接，请检查网络");
				break;
			case DataConfig.NETWORK_CODE:
				code = JsonUtils.getCode(json);
				if (code == DataConfig.SUCCESS_CODE) {
					T.showShort(CompletePersonActivity.this, "成功");
					clientDao.update(client);
					Intent intent = new Intent(CompletePersonActivity.this,
							AddCarActivity.class);
					startActivity(intent);
					finish();
				}
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
		setContentView(R.layout.activity_complete_person);

		initView();

		initData();
	}

	private void initView() {
		client = new Client();
		clientDao = new ClientDao(CompletePersonActivity.this);
		asyncHttpClient = new AsyncHttpClient();
		sp = getSharedPreferences(DataConfig.SHARE_DATA_NAME, MODE_PRIVATE);
		finish_tv = (TextView) findViewById(R.id.cp_finish);
		name_et = (EditText) findViewById(R.id.cp_name_et);
		address_et = (EditText) findViewById(R.id.cp_address_et);
		credit_et = (EditText) findViewById(R.id.cp_credit_et);
		head_layout = (RelativeLayout) findViewById(R.id.cp_head_layout);
		gender_layout = (RelativeLayout) findViewById(R.id.cp_gender_layout);
		gender_tv = (TextView) findViewById(R.id.cp_gender);
	}

	private void initData() {
		client = clientDao.find(sp.getString("account", ""));
		finish_tv.setOnClickListener(this);
		head_layout.setOnClickListener(this);
		gender_layout.setOnClickListener(this);
	}

	private void complete() {
		name = name_et.getText().toString();
		address = address_et.getText().toString();
		credit = credit_et.getText().toString();
		if (CommonUtils.isEmpty(name) || CommonUtils.isEmpty(address)
				|| CommonUtils.isEmpty(credit)) {
			T.showShort(CompletePersonActivity.this, "请完成填写信息");
		} else {
			if (CommonUtils.isConnected(CompletePersonActivity.this)) {
				client.setName(name);
				client.setAddress(address);
				client.setCreditID(credit);
				client.setGender(gender);
				RequestParams params = new RequestParams();
				params.put("account", client.getAccount());
				params.put("password", client.getPassword());
				params.put("name", client.getName());
				params.put("address", client.getAddress());
				params.put("gender", String.valueOf(client.getGender()));
				params.put("creditID", client.getCreditID());
				asyncHttpClient.post(DataConfig.URL + DataConfig.UPDATE_ACTION,
						params, new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(String content) {
								json = content;
								mHandler.sendEmptyMessage(DataConfig.NETWORK_CODE);
							}
						});
			} else {
				mHandler.sendEmptyMessage(DataConfig.NO_NETWORK_CODE);
			}
		}
	}

	/**
	 * 显示性别选择
	 */
	private void showGenderSelected() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				CompletePersonActivity.this);
		View view = LayoutInflater.from(CompletePersonActivity.this).inflate(
				R.layout.gender, null);
		Button male = (Button) view.findViewById(R.id.gender_male);
		Button female = (Button) view.findViewById(R.id.gender_female);
		switch (1) {
		case 1: // 男
			female.setCompoundDrawables(male.getCompoundDrawables()[0],
					male.getCompoundDrawables()[1], null,
					male.getCompoundDrawables()[3]);
			break;
		case 2: // 女
			male.setCompoundDrawables(male.getCompoundDrawables()[0],
					male.getCompoundDrawables()[1], null,
					male.getCompoundDrawables()[3]);
			break;
		default:
			break;
		}

		builder.setView(view);
		dialog = builder.show();
		male.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (gender == 1) {
					dialog.dismiss();
				} else {
					gender = 1;
					gender_tv.setText("男");
				}
			}
		});
		female.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (gender == 2) {
					dialog.dismiss();
				} else {
					gender = 2;
					gender_tv.setText("女");
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cp_finish:
			complete();
			break;
		case R.id.cp_head_layout:
			break;
		case R.id.cp_gender_layout:
			showGenderSelected();
			break;
		default:
			break;
		}
	}

}
