package com.gentlehealthcare.mobilecare.view.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.net.bean.PioItemInfo;

/**
 * 增加Pio listview的适配器
 */
public class AddPioAdapter extends BaseAdapter {

	private Context context;
	private List<PioItemInfo> list;

	public AddPioAdapter(Context context, List<PioItemInfo> list) {
		this.context = context;
		this.list = list;

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
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.act_add_pio_content, null);
			viewHolder.tvContent = (TextView) convertView
					.findViewById(R.id.tv_content);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.tvContent.setText(list.get(position).getItemName());

		return convertView;
	}

	private class ViewHolder {
		private TextView tvContent;
	}

}
