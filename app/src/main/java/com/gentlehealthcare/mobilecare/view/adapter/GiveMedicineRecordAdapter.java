package com.gentlehealthcare.mobilecare.view.adapter;

import java.util.List;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.db.table.TB_GiveMedicineRecord;
import com.gentlehealthcare.mobilecare.tool.DateTool;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 给药记录适配器
 */
public class GiveMedicineRecordAdapter extends BaseAdapter {
	private List<TB_GiveMedicineRecord> list;
	private Context context;
	
	public GiveMedicineRecordAdapter(List<TB_GiveMedicineRecord> list,
			Context context) {
		super();
		this.list = list;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder=null;
		if(convertView==null){
			viewHolder=new ViewHolder();
			convertView=LayoutInflater.from(context).inflate(R.layout.activity_givemedicinerecord_item, null);
			viewHolder.tv_medicinename=(TextView) convertView.findViewById(R.id.tv_medicinename);
			viewHolder.tv_bedno=(TextView) convertView.findViewById(R.id.tv_bedno);
			viewHolder.tv_patientname=(TextView) convertView.findViewById(R.id.tv_patientname);
			viewHolder.tv_starttime=(TextView) convertView.findViewById(R.id.tv_starttime);
			viewHolder.tv_visitstime=(TextView) convertView.findViewById(R.id.tv_visitstime);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder) convertView.getTag();
		}
		viewHolder.tv_bedno.setText(list.get(position).getBedNo());
		viewHolder.tv_medicinename.setText(list.get(position).getMedicineName());
		viewHolder.tv_patientname.setText(list.get(position).getName());
		viewHolder.tv_starttime.setText("开始："+DateTool.parseDate(DateTool.YYYY_MM_DD_HH_MM, list.get(position).getStartTime()));
		viewHolder.tv_visitstime.setText("巡视："+DateTool.parseDate(DateTool.YYYY_MM_DD_HH_MM, list.get(position).getVisitsTime()));
		return convertView;
	}
	
	private class ViewHolder{
		private TextView tv_patientname;
		private TextView tv_bedno;
		private TextView tv_medicinename;
		private TextView tv_starttime;
		private TextView tv_visitstime;
	}

}
