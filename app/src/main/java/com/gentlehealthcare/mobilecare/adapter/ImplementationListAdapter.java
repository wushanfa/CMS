package com.gentlehealthcare.mobilecare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.bean.ImplementationRecordBean;

import java.util.List;

/**
 * tpr history list adapter
 *
 * @author Yy
 * @date 2016/02/29
 */
public class ImplementationListAdapter extends BaseAdapter {

    private Context context;
    private List<ImplementationRecordBean> list;

    public ImplementationListAdapter(Context context, List<ImplementationRecordBean> list) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_implementation_record, null);
            viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_start_time1);
            viewHolder.tv_pj = (TextView) convertView.findViewById(R.id.tv_pj);
            viewHolder.tv_nurser = (TextView) convertView.findViewById(R.id.tv_nurser_name1);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_time.setText(list.get(position).getPerformType()+list.get(position).getEventStartTime());
        viewHolder.tv_nurser.setText(list.get(position).getName());
        viewHolder.tv_pj.setText(list.get(position).getPerformDesc());

        return convertView;
    }

    private class ViewHolder {
        private TextView tv_time;
        private TextView tv_pj;
        private TextView tv_nurser;
    }

}
