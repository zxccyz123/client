package com.dd.ui;

import com.dd.bean.Brand;
import com.dd.bean.Car;
import com.dd.config.DataConfig;
import com.dd.dao.BrandDao;
import com.dd.dao.CarDao;
import com.dd.utils.CommonUtils;
import com.dd.utils.JsonUtils;
import com.dd.utils.T;
import com.ta.util.http.AsyncHttpClient;
import com.ta.util.http.AsyncHttpResponseHandler;
import com.ta.util.http.RequestParams;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

public class AddCarActivity extends Activity implements OnClickListener {

	private AsyncHttpClient asyncHttpClient;

	private RelativeLayout brand_layout;

	private EditText type_et;

	private EditText number_et;

	private TextView finish_tv;

	private TextView brand_tv;

	private Car car;

	private CarDao carDao;

	private Brand brand;

	private BrandDao brandDao;

	private SharedPreferences sp;

	private String json;

	private int code;

	private String account;

	private int brandID = 1;

	private String type;

	private String number;

	private Handler mHandler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case DataConfig.NETWORK_CODE:
				code = JsonUtils.getCode(json);
				if (code == DataConfig.SUCCESS_CODE) {
					car = JsonUtils.getCar(json);
					carDao.add(car);
					Intent intent = new Intent(AddCarActivity.this,
							HomeActivity.class);
					startActivity(intent);
					finish();
				}
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
		setContentView(R.layout.activity_add_car);

		initView();

		initData();
	}

	private void initView() {
		sp = getSharedPreferences(DataConfig.SHARE_DATA_NAME, MODE_PRIVATE);
		asyncHttpClient = new AsyncHttpClient();
		car = new Car();
		carDao = new CarDao(this);
		brand = new Brand();
		brandDao = new BrandDao(this);
		brand_layout = (RelativeLayout) findViewById(R.id.add_car_brand);
		finish_tv = (TextView) findViewById(R.id.add_car_finish);
		type_et = (EditText) findViewById(R.id.add_type);
		number_et = (EditText) findViewById(R.id.add_num);
		brand_tv = (TextView) findViewById(R.id.add_brand);
	}

	private void initData() {
		account = sp.getString("account", "");
		brand = brandDao.findBrand(brandID);
		brand_tv.setText(brand.getName());
		brand_layout.setOnClickListener(this);
		finish_tv.setOnClickListener(this);
	}

	private void saveCar() {
		number = number_et.getText().toString();
		type = type_et.getText().toString();

		if (!CommonUtils.isEmpty(number) && !CommonUtils.isEmpty(type)) {
			if (CommonUtils.isConnected(this)) {
				RequestParams params = new RequestParams();
				params.put("brandID", String.valueOf(brandID));
				params.put("number", number);
				params.put("type", type);
				params.put("clientID", account);
				asyncHttpClient.post(DataConfig.URL
						+ DataConfig.SAVE_CAR_ACTION, params,
						new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(String content) {
								json = content;
								System.out.println(json);
								mHandler.sendEmptyMessage(DataConfig.NETWORK_CODE);
							}

						});

			} else
				mHandler.sendEmptyMessage(DataConfig.NO_NETWORK_CODE);
		} else {
			T.showShort(this, "请填写完成信息");
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_car_brand:

			break;
		case R.id.add_car_finish:
			saveCar();
			break;
		default:
			break;
		}

	}
}
