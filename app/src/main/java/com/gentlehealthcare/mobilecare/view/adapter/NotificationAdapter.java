package com.gentlehealthcare.mobilecare.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.net.bean.TipBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zyy on 2016/1/25.
 * 类说明：通知数据适配器
 */
public class NotificationAdapter extends BaseAdapter {
    private Context context = null;
    private List<TipBean> data = new ArrayList<TipBean>();

    public NotificationAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_notification2, null);
            viewHolder.content = (TextView) convertView.findViewById(R.id.tv_content_notice);
            viewHolder.title = (TextView) convertView.findViewById(R.id.tv_patient_name);
            viewHolder.bedname = (TextView) convertView.findViewById(R.id.tv_bednumber);
            viewHolder.time = (TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        TipBean tipBean = data.get(i);
        viewHolder.content.setText(tipBean.getMessageContent());
        viewHolder.title.setText(tipBean.getPatName());
        viewHolder.bedname.setText(tipBean.getBedLabel());

        if (tipBean.getNoticeClass().equals("visit")) {
            viewHolder.time.setText("去巡视");
        } else if (tipBean.getNoticeClass().equals("complete")) {
            if(tipBean.getTemplateId().equals("AA-1")){
                viewHolder.time.setText("去拔针");
            }else{
                viewHolder.time.setText("去封管");
            }
        } else if (tipBean.getNoticeClass().equals("P")) {
            viewHolder.time.setText("去巡视");
        }
        viewHolder.time.setTextColor(context.getResources().getColor(R.color.blue_normal));
        return convertView;
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<TipBean> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        private TextView content;
        private TextView title;
        private TextView time;
        private TextView bedname;
    }
}
