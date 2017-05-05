package com.gentlehealthcare.mobilecare.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.gentlehealthcare.mobilecare.R;

/**
 * Created by ouyang on 2015/5/29.
 */
public class CopySealAdapter extends BaseAdapter {
    private Context context;
    private String[] strings;
    private int position = 0;
    public CopySealAdapter(Context context, String[] strings) {
        this.context = context;
        this.strings = strings;
    }

    @Override
    public int getCount() {
        return strings.length;
    }

    @Override
    public Object getItem(int position) {
        return strings[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public void notifyChanged(int position) {

        this.position = position;

        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.activity_seal_item,null);
            viewHolder=new ViewHolder();
            viewHolder.tv_seal= (TextView) convertView.findViewById(R.id.tv_seal);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_seal.setText(strings[position]);
        if (this.position >=position) {

            viewHolder.tv_seal.setTextColor(Color.WHITE);
            viewHolder.tv_seal.setBackgroundResource(R.drawable.frame_red_select);

        } else {

            viewHolder.tv_seal.setTextColor(Color.RED);
            viewHolder.tv_seal.setBackgroundResource(R.drawable.frame_red);
        }
        return convertView;
    }
    private class ViewHolder{
        private TextView tv_seal;
    }
}
