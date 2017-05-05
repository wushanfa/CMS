package com.gentlehealthcare.mobilecare.view.adapter;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;

public class SlidingListAdapter extends BaseAdapter {

	private Context context;

	private List<String> list;

	private Resources resources;

	private int position = 0;

	public SlidingListAdapter(Context context, List<String> list) {

		super();

		this.context = context;

		this.list = list;

		resources = context.getResources();
	}

	public void notifyChanged(int position) {

		this.position = position;

		notifyDataSetChanged();
	}

	@Override
	public int getCount() {

		return list.size();
	}

	@Override
	public Object getItem(int position) {

		return list.get(position);
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

			convertView = LayoutInflater.from(context).inflate(R.layout.item_sliding, null);

			textView = (TextView) convertView.findViewById(R.id.tv_flow);

			convertView.setTag(textView);

		} else {

			textView = (TextView) convertView.getTag();
		}

		textView.setText(list.get(position));

		if (position == this.position) {

			textView.setTextColor(resources.getColor(R.color.white));

			convertView.setBackgroundResource(R.color.blue_normal);

		} else {

			textView.setTextColor(resources.getColor(R.color.blue_grey_500));//2015.8.31 11.49

			convertView.setBackgroundResource(R.color.white);
		}

		return convertView;
	}

}
