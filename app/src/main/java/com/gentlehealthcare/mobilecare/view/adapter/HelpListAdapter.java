package com.gentlehealthcare.mobilecare.view.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;

public class HelpListAdapter extends BaseAdapter {

	private Context context;

	private List<Map<String, String>> maps;

	public HelpListAdapter(Context context, List<Map<String, String>> maps) {

		super();

		this.maps = maps;

		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return maps.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return maps.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Map<String, String> map = maps.get(position);

		ViewHolder viewHolder = null;

		if (convertView == null) {

			viewHolder = new ViewHolder();

			convertView = LayoutInflater.from(context).inflate(R.layout.item_tv_normal, null);

			viewHolder.textView = (TextView) convertView.findViewById(R.id.tv_lv_item);

			convertView.setTag(viewHolder);

		} else {

			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.textView.setText(map.get("title"));

		return convertView;
	}

	class ViewHolder {

		TextView textView;
	}

}
