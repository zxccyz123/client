package com.dd.adapter;

import java.util.List;
import java.util.Map;

import com.dd.ui.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * ≤Àµ•  ≈‰∆˜
 * 
 * @author Administrator
 * 
 */
public class HomeItemAdapter extends BaseAdapter {

	private Context mContext;
	private List<Map<String, Object>> listItems;
	private LayoutInflater inflater;

	public final class ListItemView {
		public ImageView iv;
		public TextView tv;
	}

	public HomeItemAdapter(Context context, List<Map<String, Object>> listItems) {
		this.mContext = context;
		inflater = LayoutInflater.from(mContext);
		this.listItems = listItems;
	}

	@Override
	public int getCount() {
		return listItems.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ListItemView lv = null;
		if (convertView == null) {
			lv = new ListItemView();
			convertView = inflater.inflate(R.layout.list_item, null);
			lv.iv = (ImageView) convertView.findViewById(R.id.item_icon);
			lv.tv = (TextView) convertView.findViewById(R.id.item_text);
			convertView.setTag(lv);
		} else
			lv = (ListItemView) convertView.getTag();

		lv.iv.setImageResource((Integer) listItems.get(position).get("icon"));
		lv.tv.setText((String) listItems.get(position).get("name"));
		return convertView;
	}

}
