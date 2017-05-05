package com.gentlehealthcare.mobilecare.view.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.net.bean.LoadAppraislRecordBean;

public class DocAppraislAdapter extends BaseAdapter {

	private Context context;
	private List<LoadAppraislRecordBean> larList = new ArrayList<LoadAppraislRecordBean>();

	public DocAppraislAdapter() {
		super();
	}

	public DocAppraislAdapter(Context context,
			List<LoadAppraislRecordBean> larList) {
		super();
		this.context = context;
		this.larList = larList;
	}

	@Override
	public int getCount() {
		return larList.size();
	}

	@Override
	public Object getItem(int position) {
		return larList.get(position);
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
					R.layout.act_load_order_evaluation_content, null);
			viewHolder.tvContent = (TextView) convertView
					.findViewById(R.id.tv_content);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.tvContent.setText(larList.get(position).getItemName());

		return convertView;
	}

	private class ViewHolder {
		private TextView tvContent;
	}

}
