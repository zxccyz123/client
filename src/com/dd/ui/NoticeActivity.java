package com.dd.ui;

import com.dd.utils.T;
import com.dd.widget.CheckSwitchButton;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.app.Activity;

public class NoticeActivity extends Activity implements OnClickListener {

	private Button back_btn;

	private CheckSwitchButton receiver_cb;

	private CheckSwitchButton voice_cb;

	private CheckSwitchButton shake_cb;

	private RelativeLayout notice_2;

	private RelativeLayout notice_3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notice);

		initView();

		initData();
	}

	private void initView() {
		back_btn = (Button) findViewById(R.id.notice_back);
		receiver_cb = (CheckSwitchButton) findViewById(R.id.notice_recevier);
		voice_cb = (CheckSwitchButton) findViewById(R.id.notice_voice);
		shake_cb = (CheckSwitchButton) findViewById(R.id.notice_shake);
		notice_2 = (RelativeLayout) findViewById(R.id.notice_2);
		notice_3 = (RelativeLayout) findViewById(R.id.notice_3);
	}

	private void initData() {
		receiver_cb.setChecked(true);
		shake_cb.setChecked(true);
		voice_cb.setChecked(true);
		back_btn.setOnClickListener(this);
		receiver_cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				if (isChecked) {
					notice_2.setVisibility(View.VISIBLE);
					notice_3.setVisibility(View.VISIBLE);
				} else {
					notice_2.setVisibility(View.INVISIBLE);
					notice_3.setVisibility(View.INVISIBLE);
				}
			}
		});

		voice_cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				if (isChecked) {
					T.showShort(NoticeActivity.this, "打开声音提醒");
				} else {
					T.showShort(NoticeActivity.this, "关闭声音提醒");
				}
			}
		});

		shake_cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				if (isChecked) {
					T.showShort(NoticeActivity.this, "打开震动");
				} else {
					T.showShort(NoticeActivity.this, "关闭震动");
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.notice_back:
			finish();
			break;

		default:
			break;
		}
	}
}
