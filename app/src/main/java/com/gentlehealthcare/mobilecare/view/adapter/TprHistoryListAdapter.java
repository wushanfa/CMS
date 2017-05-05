package com.gentlehealthcare.mobilecare.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.net.bean.PioItemInfo;
import com.gentlehealthcare.mobilecare.net.bean.TprSpeedHistoryCommonBean;

import java.util.List;

/**
 * tpr history list adapter
 *
 * @author diaozhiwei
 * @date 2016/02/29
 */
public class TprHistoryListAdapter extends BaseAdapter {

    private Context context;
    private List<TprSpeedHistoryCommonBean> list;

    public TprHistoryListAdapter(Context context, List<TprSpeedHistoryCommonBean> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.act_tpr_history, null);
            viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            viewHolder.tv_t = (TextView) convertView.findViewById(R.id.tv_t);
            viewHolder.tv_p = (TextView) convertView.findViewById(R.id.tv_p);
            viewHolder.tv_r = (TextView) convertView.findViewById(R.id.tv_r);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_time.setText(list.get(position).getTime());
        viewHolder.tv_t.setText(list.get(position).getT());
        viewHolder.tv_p.setText(list.get(position).getP());
        viewHolder.tv_r.setText(list.get(position).getR());

        return convertView;
    }

    private class ViewHolder {
        private TextView tv_time;
        private TextView tv_t;
        private TextView tv_p;
        private TextView tv_r;
    }

}
