package com.dd.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.dd.bean.Client;
import com.dd.config.DataConfig;
import com.dd.dao.ClientDao;
import com.dd.utils.HttpUtils;
import com.dd.utils.JsonUtils;
import com.dd.utils.T;
import com.dd.utils.CommonUtils;
import com.dd.widget.ClearEditText;

public class LoginActivity extends Activity implements OnClickListener {

	private ClearEditText account_et;

	private ClearEditText password_et;

	private Button login_btn;

	private TextView reg_tv;

	private TextView forget_tv;

	private String json;

	private int code;

	private List<NameValuePair> params;

	private ProgressDialog dialog;

	private SharedPreferences sp;

	private Client client;

	private ClientDao clientDao;

	@SuppressLint("CommitPrefEdits")
	private Handler mHandler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			dialog.dismiss();
			Intent intent = new Intent();
			switch (msg.what) {
			case DataConfig.NETWORK_CODE:
				code = JsonUtils.getCode(json);
				if (code == DataConfig.SUCCESS_CODE) {
					T.showShort(LoginActivity.this, "登录成功");
					client = JsonUtils.getClient(json);
					clientDao.addClient(client);
					Editor editor = sp.edit();
					editor.putString("account", account_et.getText().toString());
					editor.commit();
					intent.setClass(LoginActivity.this, HomeActivity.class);
					startActivity(intent);
					finish();
				}
				if (code == DataConfig.ACCOUNT_ERROR_CODE) {
					T.showShort(LoginActivity.this, "帐号错误");
					account_et.setError("帐号错误");
				}
				if (code == DataConfig.PASSWORD_ERROR_CODE) {
					T.showShort(LoginActivity.this, "密码错误");
					password_et.setError("密码错误");
				}
				break;
			case DataConfig.NO_NETWORK_CODE:
				T.showShort(LoginActivity.this, "当前无网络连接，请检查您的网络");
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
		setContentView(R.layout.activity_login);

		initView();

		initData();
	}

	private void initView() {
		client = new Client();
		clientDao = new ClientDao(this);
		sp = getSharedPreferences(DataConfig.SHARE_DATA_NAME, MODE_PRIVATE);
		account_et = (ClearEditText) findViewById(R.id.login_account);
		password_et = (ClearEditText) findViewById(R.id.login_password);
		login_btn = (Button) findViewById(R.id.login_btn);
		reg_tv = (TextView) findViewById(R.id.login_reg);
		forget_tv = (TextView) findViewById(R.id.login_forget);
	}

	private void initData() {
		login_btn.setOnClickListener(this);
		reg_tv.setOnClickListener(this);
		forget_tv.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.login_btn:
			if (CommonUtils.isEmpty(account_et.getText().toString())) {
				account_et.setShakeAnimation();
				account_et.setError("帐号不能为空");
			} else if (CommonUtils.isEmpty(password_et.getText().toString())) {
				password_et.setError("密码不能为空");
				password_et.setShakeAnimation();
			} else {
				login();
			}
			break;
		case R.id.login_forget:
			forget_tv
					.setTextColor(getResources().getColor(R.color.btn_pressed));
			intent.setClass(LoginActivity.this, ForgetActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.login_reg:
			reg_tv.setTextColor(getResources().getColor(R.color.btn_pressed));
			intent.setClass(LoginActivity.this, RegActivity.class);
			startActivity(intent);
			finish();
			break;
		default:
			break;
		}
	}

	private List<NameValuePair> getParams() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("account", account_et.getText()
				.toString()));
		params.add(new BasicNameValuePair("password", password_et.getText()
				.toString()));
		return params;
	}

	private void login() {
		if (CommonUtils.isConnected(LoginActivity.this)) {
			dialog = ProgressDialog.show(LoginActivity.this, "", "正在登录，请稍后");
			params = getParams();
			new Thread(new Runnable() {
				@Override
				public void run() {
					json = HttpUtils.doPost(DataConfig.LOGIN_ACTION, params);
					Log.i("login", json);
					mHandler.sendEmptyMessage(DataConfig.NETWORK_CODE);
				}
			}).start();
		} else {
			mHandler.sendEmptyMessage(DataConfig.NO_NETWORK_CODE);
		}
	}
}
