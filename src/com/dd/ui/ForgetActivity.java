package com.dd.ui;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;

public class ForgetActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget);
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(ForgetActivity.this, LoginActivity.class);
		startActivity(intent);
		finish();
		super.onBackPressed();
	}

}
