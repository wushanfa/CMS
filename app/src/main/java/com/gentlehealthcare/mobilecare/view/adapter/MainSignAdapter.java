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
 * Created by ouyang on 2015/5/26.
 */
public class MainSignAdapter extends BaseAdapter {
    private String[] stringList;
    private Context context;

    public MainSignAdapter(String[] stringList, Context context) {
        this.stringList = stringList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return stringList.length;
    }

    @Override
    public Object getItem(int position) {
        return stringList[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.fra_miansign_main,null);
            viewHolder=new ViewHolder();
            viewHolder.tv_content= (TextView) convertView.findViewById(R.id.tv_mainsign_item);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_content.setText(stringList[position]);
        return convertView;
    }
    private class ViewHolder{
        private TextView tv_content;
    }

}
