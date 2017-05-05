package com.gentlehealthcare.mobilecare.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;

public class KeyBoardGridAdapter extends BaseAdapter {

	private Context context;
	private String[] strings;

	public KeyBoardGridAdapter(Context context, String[] strings) {
		super();
		this.context = context;
		this.strings = strings;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return strings.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return strings[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		String string = strings[position];

		if (convertView == null) {

			convertView = LayoutInflater.from(context).inflate(R.layout.item_keyboard, null);

			TextView textView = (TextView) convertView.findViewById(R.id.textview);

			textView.setText(string);
		}
		return convertView;
	}
}
