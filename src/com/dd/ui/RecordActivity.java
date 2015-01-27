package com.dd.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dd.adapter.RecordAdapter;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.app.Activity;

public class RecordActivity extends Activity {

	private Button back_btn;

	private ListView record_lv;

	private RecordAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record);

		initView();
	}

	private void initView() {
		back_btn = (Button) findViewById(R.id.record_back);
		record_lv = (ListView) findViewById(R.id.record_list);
		adapter = new RecordAdapter(RecordActivity.this, getItems());
		record_lv.setAdapter(adapter);

		back_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				onBackPressed();
			}
		});

	}

	private List<Map<String, Object>> getItems() {
		List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < 5; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("address", "重庆市" + i + "xx");
			map.put("staff", "员工" + i);
			map.put("time", "2014.12.15");
			items.add(map);
		}
		return items;
	}

	@Override
	public void onBackPressed() {
		finish();
	}

}
