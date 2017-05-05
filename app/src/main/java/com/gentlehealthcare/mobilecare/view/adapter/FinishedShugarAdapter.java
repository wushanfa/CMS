package com.gentlehealthcare.mobilecare.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.net.bean.FinishedShugar;

import java.util.List;

/**
 * @author Zyy
 * @date 2015-10-9下午1:26:37
 * @category 已完成血糖记录
 */
public class FinishedShugarAdapter extends BaseAdapter {
	private List<FinishedShugar> mFinishedShugars;
	private Context context;
	private int position = 0;

	public FinishedShugarAdapter(List<FinishedShugar> mFinishedShugars, Context context) {
		this.mFinishedShugars = mFinishedShugars;
		this.context = context;
	}

	public void notifyChanged(int position) {

		this.position = position;
		notifyDataSetInvalidated();
		// notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mFinishedShugars.size();
	}

	@Override
	public Object getItem(int position) {
		return mFinishedShugars.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_finished_shugar, null);
			viewHolder = new ViewHolder();
			viewHolder.tv_xtz = (TextView) convertView
					.findViewById(R.id.tv_xtz);
			viewHolder.tv_bcxt = (TextView) convertView
					.findViewById(R.id.tv_bcxt);
			viewHolder.tv_xtbz = (TextView) convertView
					.findViewById(R.id.tv_xtbz);
			viewHolder.tv_hsqm = (TextView) convertView
					.findViewById(R.id.tv_hsqm);

			viewHolder.tv_xtcssj = (TextView) convertView
					.findViewById(R.id.tv_xtcssj);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		FinishedShugar mFinishedShugar= mFinishedShugars.get(position);
		viewHolder.tv_xtcssj.setText(mFinishedShugar.getRecordingTime());
		viewHolder.tv_xtz.setText(mFinishedShugar.getItemName());
		viewHolder.tv_bcxt.setText(mFinishedShugar.getVal()+"  "+mFinishedShugar.getRes());
		viewHolder.tv_xtbz.setText(mFinishedShugar.getReference());
		viewHolder.tv_hsqm.setText(mFinishedShugar.getLogBy());

//		if (washPip.getInjectionTool() != null) {
//			viewHolder.tv_injectionTool.setText(washPip.getInjectionTool());
//		} else {
//			viewHolder.rl_injectionTool.setVisibility(View.GONE);
//		}
//		viewHolder.tv_planStartTime.setText(washPip.getPlanStartTime());
//		if (washPip.getSpeedUnits() != null) {
//			viewHolder.tv_speed.setText(washPip.getSpeed()
//					+ washPip.getSpeedUnits());
//		} else {
//			viewHolder.tv_speed.setText(washPip.getSpeed());
//		}
//
//		convertView.setPadding(0, 10, 0, 0);
//
//		if (position == this.position) {
//			convertView.setBackgroundResource(R.drawable.skin_btnblue_normal);
//		} else {
//			convertView.setBackgroundResource(R.drawable.skin_btngray_overdue);
//
//		}
		return convertView;
	}

	public class ViewHolder {
		TextView tv_xtz;
		TextView tv_bcxt;
		TextView tv_xtbz;
		TextView tv_hsqm;
		TextView tv_xtcssj;
	}

}
