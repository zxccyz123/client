package com.dd.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.dd.bean.Brand;
import com.dd.bean.Client;
import com.dd.config.DataConfig;
import com.dd.dao.BrandDao;
import com.dd.dao.ClientDao;
import com.dd.utils.HttpUtils;
import com.dd.utils.JsonUtils;
import com.dd.utils.T;
import com.dd.utils.CommonUtils;
import com.dd.widget.ClearEditText;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class RegActivity extends Activity {

	private ClearEditText account_et;
	private ClearEditText password_et;
	private ClearEditText repsw_et;

	private Button reg_btn;

	private TextView log_tv;

	private String account;

	private String password;

	private String repsw;

	private String json;

	private int code;

	private List<NameValuePair> params;

	private Client client;

	private ClientDao clientDao;

	private SharedPreferences sp;

	private List<Brand> brands;

	private BrandDao brandDao;

	private Handler mHandler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case DataConfig.NETWORK_CODE:
				code = JsonUtils.getCode(json);
				if (code == DataConfig.SUCCESS_CODE) {
					brands = JsonUtils.getBrands(json);
					brandDao.addBrand(brands);
					Editor editor = sp.edit();
					editor.putString("account", account);
					editor.commit();
					clientDao.addClient(client);
					T.showShort(RegActivity.this, "注册成功");
					Intent intent = new Intent(RegActivity.this,
							CompletePersonActivity.class);
					startActivity(intent);
					finish();
				}
				if (code == DataConfig.ACCOUNT_EXIT_CODE) {
					T.showShort(RegActivity.this, "该帐号已存在");
					account_et.setError("该帐号已存在");
				}
				break;
			case DataConfig.NO_NETWORK_CODE:
				T.showShort(RegActivity.this, "当前无网络连接，请检查您的网络");
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
		setContentView(R.layout.activity_reg);

		initView();
		initData();
	}

	private void initView() {
		sp = getSharedPreferences(DataConfig.SHARE_DATA_NAME, MODE_PRIVATE);
		client = new Client();
		clientDao = new ClientDao(RegActivity.this);
		brands = new ArrayList<Brand>();
		brandDao = new BrandDao(this);
		account_et = (ClearEditText) findViewById(R.id.reg_account);
		password_et = (ClearEditText) findViewById(R.id.reg_password);
		repsw_et = (ClearEditText) findViewById(R.id.reg_copypsw);
		reg_btn = (Button) findViewById(R.id.reg_btn);
		log_tv = (TextView) findViewById(R.id.reg_login);

	}

	private void initData() {
		reg_btn.setOnClickListener(onRegClickListener);
		log_tv.setOnClickListener(onLogClickListener);
	}

	/**
	 * 注册
	 */
	OnClickListener onRegClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			account = account_et.getText().toString();
			password = password_et.getText().toString();
			repsw = repsw_et.getText().toString();
			if (CommonUtils.isEmpty(account)) {
				account_et.setError("帐号不能为空");
			} else if (CommonUtils.isEmpty(password)) {
				password_et.setError("密码不能为空");
			} else if (CommonUtils.isEmpty(repsw)) {
				repsw_et.setError("请再次输入密码");
			} else if (!password.equals(repsw)) {
				T.showShort(RegActivity.this, "两次输入密码不一致");
			} else {
				login();
			}
		}
	};

	/**
	 * 登录
	 */
	private void login() {
		client.setAccount(account);
		client.setPassword(password);
		if (!CommonUtils.isConnected(RegActivity.this)) {
			mHandler.sendEmptyMessage(DataConfig.NO_NETWORK_CODE);
		} else {
			params = getParams();
			new Thread(new Runnable() {
				@Override
				public void run() {
					json = HttpUtils.doPost(DataConfig.REGISTER_ACTION, params);
					System.out.println(json);
					mHandler.sendEmptyMessage(DataConfig.NETWORK_CODE);
				}
			}).start();
		}
	}

	private List<NameValuePair> getParams() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("account", account));
		params.add(new BasicNameValuePair("password", password));
		return params;
	}

	/**
	 * 跳转至登录页面
	 */
	OnClickListener onLogClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(RegActivity.this, LoginActivity.class);
			startActivity(intent);
			finish();
		}
	};

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(RegActivity.this, LoginActivity.class);
		startActivity(intent);
		finish();
	}

}
