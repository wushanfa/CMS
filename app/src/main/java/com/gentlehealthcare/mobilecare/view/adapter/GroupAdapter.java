package com.gentlehealthcare.mobilecare.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;

import java.util.List;

/**
 * 护理单元适配器
 */
public class GroupAdapter extends BaseAdapter {
    private Context context;
    private List<String> groupNames;

    public GroupAdapter(Context context, List<String> groupNames) {
        super();
        this.context = context;
        this.groupNames = groupNames;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return groupNames.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return groupNames.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_choosegroup_item, parent,false);
            viewHolder = new ViewHolder();
            viewHolder.tv_groupName = (TextView) convertView.findViewById(R.id.tv_groupname);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (groupNames.get(position).contains("-")) {
            viewHolder.tv_groupName.setText(groupNames.get(position).split("-")[1]);
        } else if (groupNames.get(position).contains("（") && groupNames.get(position).contains("）")) {
            String groupName = groupNames.get(position).split("（")[1];
            groupName = groupName.split("）")[0];
            viewHolder.tv_groupName.setText(groupName);
        }else if (groupNames.get(position).contains("(") && groupNames.get(position).contains(")")) {
            String groupName = groupNames.get(position).split("\\(")[1];
            groupName = groupName.split("\\)")[0];
            viewHolder.tv_groupName.setText(groupName);
        }else{
            viewHolder.tv_groupName.setText(groupNames.get(position));
        }

//		viewHolder.tv_groupName.setText(groupNames.get(position).split("-")[1]);
        return convertView;
    }

    private class ViewHolder {
        private TextView tv_groupName;
    }
}
