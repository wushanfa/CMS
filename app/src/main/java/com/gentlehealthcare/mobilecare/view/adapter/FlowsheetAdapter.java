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

public class FlowsheetAdapter extends BaseAdapter {

	private Context context;

	private List<Map<String, String>> maps;

	public FlowsheetAdapter(Context context, List<Map<String, String>> maps) {

		super();

		this.context = context;

		this.maps = maps;
	}

	@Override
	public int getCount() {

		return maps.size();
	}

	@Override
	public Object getItem(int position) {

		return maps.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		TextView textView;

		if (convertView == null) {

			convertView = LayoutInflater.from(context).inflate(R.layout.item_flow, null);

			textView = (TextView) convertView.findViewById(R.id.tv_flow);

			convertView.setTag(textView);
		} else {

			textView = (TextView) convertView.getTag();
		}

		Map<String, String> map = maps.get(position);

		String name = map.get("name");

		String state = map.get("state");

		textView.setText(name);

		if (state.equals("normal")) {

			textView.setTextColor(context.getResources().getColor(R.color.blue_normal));

		} else if (state.equals("checked")) {

			textView.setTextColor(context.getResources().getColor(R.color.blue_pressed));

		} else if (state.equals("excuted")) {

			textView.setTextColor(context.getResources().getColor(R.color.blue_grey_400));
		}

		return convertView;
	}

}
