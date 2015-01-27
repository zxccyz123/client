package com.dd.adapter;

import java.util.List;
import java.util.Map;

import com.dd.ui.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RecordAdapter extends BaseAdapter {

	private Context mContext;
	private List<Map<String, Object>> items;
	private LayoutInflater inflater;

	public final class Holder {
		TextView time_tv;
		TextView staff_tv;
		TextView address_tv;
	}

	public RecordAdapter(Context context, List<Map<String, Object>> items) {
		this.mContext = context;
		this.items = items;
		inflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		if (items == null)
			return 0;
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		if (items == null)
			return null;
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			convertView = inflater.inflate(R.layout.item_time_line, null);
			holder.address_tv = (TextView) convertView
					.findViewById(R.id.time_line_address);
			holder.staff_tv = (TextView) convertView
					.findViewById(R.id.time_line_staff);
			holder.time_tv = (TextView) convertView
					.findViewById(R.id.time_line_time);
			convertView.setTag(holder);
		} else
			holder = (Holder) convertView.getTag();

		holder.address_tv.setText((String) items.get(position).get("address"));
		holder.staff_tv.setText((String) items.get(position).get("staff"));
		holder.time_tv.setText((String) items.get(position).get("time"));

		return convertView;
	}

}
