package com.dd.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;

public class SetActivity extends Activity implements OnClickListener {
	private Button back_btn;

	private TextView msg_tv;

	private TextView exit_tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set);

		initView();

		initData();
	}

	private void initView() {
		back_btn = (Button) findViewById(R.id.set_back);
		msg_tv = (TextView) findViewById(R.id.set_msg_notice);
		exit_tv = (TextView) findViewById(R.id.set_exit);
	}

	private void initData() {
		back_btn.setOnClickListener(this);
		msg_tv.setOnClickListener(this);
		exit_tv.setOnClickListener(this);
	}

	@Override
	public void onBackPressed() {
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.set_back:
			onBackPressed();
			break;
		case R.id.set_msg_notice:
			Intent intent = new Intent(SetActivity.this, NoticeActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case R.id.set_exit:
			break;
		default:
			break;
		}
	}

}
