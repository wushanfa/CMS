package com.gentlehealthcare.mobilecare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/23.
 */

public class RecordTimeAdapter extends BaseAdapter {

    private Context context;
    private ArrayList list;
    private String tag;
    public RecordTimeAdapter(Context context,ArrayList list,String tag){
        this.context=context;
        this.list=list;
        this.tag = tag;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.act_record_time_item, null);
            vh.textview = (TextView) convertView.findViewById(R.id.tv_record_time_item);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.textview.setText(list.get(position) + "");
        if (position == 0){
            vh.textview.setBackground(context.getResources().getDrawable(R.drawable.rectangular_border_top_bg));
        }
       else if (position == list.size()-1){
           vh.textview.setBackground(context.getResources().getDrawable(R.drawable.rectangular_border_bottom_bg));
       }
       else {
            vh.textview.setBackground(context.getResources().getDrawable(R.drawable.rectangular_border_rectangular_bg));
       }
        return convertView;
    }
    static class ViewHolder {
        TextView textview;
    }
}