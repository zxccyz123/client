package com.dd.ui;

import com.dd.bean.Car;
import com.dd.config.DataConfig;
import com.dd.dao.CarDao;
import com.dd.utils.T;
import com.dd.widget.ImageIndicatorView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.app.Activity;
import android.content.SharedPreferences;

public class CarActivity extends Activity {

	private Button back_btn;

	private ImageIndicatorView imageShowView;

	private SharedPreferences sp;

	private Car car;

	private CarDao carDao;

	private Integer[] pics = new Integer[] { R.drawable.lyf, R.drawable.lyf,
			R.drawable.lyf, R.drawable.lyf, R.drawable.lyf };

	private Handler mHandler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case DataConfig.NETWORK_CODE:

				break;
			case DataConfig.NO_NETWORK_CODE:
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
		setContentView(R.layout.activity_car);

		initView();

		initData();
	}

	private void initView() {
		sp = getSharedPreferences(DataConfig.SHARE_DATA_NAME, MODE_PRIVATE);
		car = new Car();
		carDao = new CarDao(this);
		back_btn = (Button) findViewById(R.id.car_back);
		imageShowView = (ImageIndicatorView) findViewById(R.id.car_pics);
	}

	private void initData() {
		back_btn.setOnClickListener(onBackClickListener);
		imageShowView.setupLayoutByDrawable(pics);
		imageShowView
				.setIndicateStyle(ImageIndicatorView.INDICATE_USERGUIDE_STYLE);
		imageShowView.show();
	}

	OnClickListener onEditClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			T.showShort(CarActivity.this, "点击了编辑资料");
		}
	};

	OnClickListener onBackClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			onBackPressed();
		}
	};

	@Override
	public void onBackPressed() {
		finish();
	}

}
