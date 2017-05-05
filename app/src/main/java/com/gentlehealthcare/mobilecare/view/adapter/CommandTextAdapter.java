package com.gentlehealthcare.mobilecare.view.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import com.gentlehealthcare.mobilecare.R;

import java.util.List;

/**
 * 通用单个文本适配器
 * Created by ouyang on 2015/3/25.
 */
public class CommandTextAdapter extends BaseAdapter {
    private Context context=null;
    private List<String> medicines=null;
    public CommandTextAdapter(Context context, List<String> medicines) {
        this.context=context;
        this.medicines=medicines;
    }

    @Override
    public int getCount() {
        return this.medicines.size();
    }

    @Override
    public Object getItem(int position) {
        return medicines.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewholder=null;
        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.activity_commond_text,null);
            viewholder=new ViewHolder();
            viewholder.tv_command_text= (TextView) convertView.findViewById(R.id.tv_command_text);
            convertView.setTag(viewholder);
        }else{
            viewholder= (ViewHolder) convertView.getTag();
        }
        viewholder.tv_command_text.setText(medicines.get(position));
        return convertView;
    }

    private class ViewHolder{
        private TextView tv_command_text;
    }
}
