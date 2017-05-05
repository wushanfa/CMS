package com.gentlehealthcare.mobilecare.view.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.net.bean.BloodProductBean2;
import com.gentlehealthcare.mobilecare.tool.StringTool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zyy on 2016/1/25.
 * 类说明：已完成输血血品数据适配器
 */
public class TransfusionedAdapter extends BaseAdapter {
    private Context context = null;
    private List<BloodProductBean2> data = new ArrayList<BloodProductBean2>();

    public TransfusionedAdapter(Context context) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_transfusioned, null);
            viewHolder.content = (TextView) convertView.findViewById(R.id.tv_content_trans);
            viewHolder.time = (TextView) convertView.findViewById(R.id.tv_tras_end_time);
            viewHolder.bloodcode= (TextView) convertView.findViewById(R.id.tv_bloodcode);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        BloodProductBean2 bloodProductBean2 = data.get(i);
        if(!TextUtils.isEmpty(bloodProductBean2.getTransEndDate())){
            viewHolder.time.setText(StringTool.dateToTime(bloodProductBean2.getTransEndDate()));
        }
        viewHolder.content.setText(bloodProductBean2.getBloodTypeName());
        viewHolder.bloodcode.setText(bloodProductBean2.getBloodProductCode());
        return convertView;
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<BloodProductBean2> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        private TextView content;
        private TextView time;
        private TextView bloodcode;
    }
}
