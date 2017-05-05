package com.gentlehealthcare.mobilecare.view.adapter;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.constant.MedicineConstant;

public class GridReasonAdapter extends BaseAdapter {

	private Context context;

	private String[] strings;

	private Resources resources;

	private int position = 0;

	public GridReasonAdapter(Context context, String[] strings) {

		super();

		this.context = context;

		this.strings = strings;

		resources = context.getResources();
	}

	public void notifyChanged(int position) {

		this.position = position;

		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return (strings.length == 0) ? 0 : strings.length;
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

		ViewHolder viewHolder;

		if (convertView == null) {

			viewHolder = new ViewHolder();

			convertView = LayoutInflater.from(context).inflate(R.layout.item_reason, null);

			viewHolder.textView = (TextView) convertView.findViewById(R.id.tv_reason);

			convertView.setTag(viewHolder);

		} else {

			viewHolder = (ViewHolder) convertView.getTag();
		}

		String string = strings[position];

		viewHolder.textView.setText(string);

		if (position == this.position) {

			viewHolder.textView.setTextColor(resources.getColor(R.color.white));
            if (position+2<strings.length) {
                viewHolder.textView.setPadding(0, 0, 0, 0);
                viewHolder.textView.setBackgroundResource(R.drawable.skin_btn_normal);
            }else {
                viewHolder.textView.setPadding(0, 16, 0, 16);
                viewHolder.textView.setBackgroundResource(R.drawable.frame_red_select);
            }
		} else {


            if (position+2>=strings.length) {
                viewHolder.textView.setPadding(0, 16, 0, 16);
                viewHolder.textView.setTextColor(Color.RED);
                viewHolder.textView.setBackgroundResource(R.drawable.frame_red);
            }else {
                viewHolder.textView.setPadding(0,0,0,0);
                viewHolder.textView.setTextColor(resources.getColor(R.color.blue_normal));
                viewHolder.textView.setBackgroundResource(R.drawable.skin_btn_frame);
            }
            }

		return convertView;
	}

	class ViewHolder {

		TextView textView;
	}

}
