package com.gentlehealthcare.mobilecare.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.gentlehealthcare.mobilecare.R;

import java.util.List;
import java.util.Map;

/**
 * Created by ouyang on 2015-04-14.
 */
public class NursingEventDetailAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, String>> children;

    public NursingEventDetailAdapter(Context context, List<Map<String, String>> children) {
        super();
        this.context = context;
        this.children = children;
    }
    @Override
    public int getCount() {
        return children.size();
    }

    @Override
    public Object getItem(int position) {
        return children.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if (convertView==null){
            viewHolder=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.act_medicineli_nursingeventdetail_item,null);
            viewHolder.tv_content= (TextView) convertView.findViewById(R.id.tv_eventdetailcontent);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_content.setText(children.get(position).get("message"));
        return convertView;
    }
    private class ViewHolder{
        private TextView tv_content;
    }
}
