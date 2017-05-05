package com.gentlehealthcare.mobilecare.view.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;

/**
 * Created by ouyang on 2015-05-07.
 */
public class ProgressWorkAdapter extends BaseAdapter{
    private List<String> list;
    private Context context;

	public ProgressWorkAdapter(List<String> list, Context context) {
            this.list = list;
            this.context = context;
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
        ViewHolder viewHolder=null;
        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.item_progress,null);
            viewHolder=new ViewHolder();
            viewHolder.tv_content= (TextView) convertView.findViewById(R.id.tv_content);
            viewHolder.tv_content.setTextSize(30);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        ListView.LayoutParams layoutParams=new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT,130);
        convertView.setLayoutParams(layoutParams);
        String content=list.get(position);
        int start=0;
        SpannableStringBuilder style=new SpannableStringBuilder(content);
        while (start>=0) {
            start = content.indexOf("0",start);
            int index=content.indexOf("0",start+1);
            if (start>=0) {
                if (index==(start+1)) {
                    style.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), start, start + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                }else {
                    style.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), start, start + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

                start += 2;
            }
        }
        viewHolder.tv_content.setText(style);
        return convertView;
    }

    private class ViewHolder {
        private TextView tv_content;
    }

}
