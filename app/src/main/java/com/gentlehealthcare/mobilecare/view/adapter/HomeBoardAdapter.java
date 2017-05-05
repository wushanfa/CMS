package com.gentlehealthcare.mobilecare.view.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.bean.HomeBean;
import com.gentlehealthcare.mobilecare.view.SquareTextView;

/**
 * 我的白板界面适配器
 * Created by ouyang on 2015/5/26.
 */
public class HomeBoardAdapter extends BaseAdapter {
    private Context context;
    private List<HomeBean> homeBeanList;

    public HomeBoardAdapter(Context context, List<HomeBean> homeBeanList) {
        this.context = context;
        this.homeBeanList = homeBeanList;
    }

    @Override
    public int getCount() {
        return homeBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return homeBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.activity_home_item,parent,false);
            viewHolder=new ViewHolder();
            viewHolder.tv_content= (TextView) convertView.findViewById(R.id.tv_home_content);
            viewHolder.tv_sum= (SquareTextView) convertView.findViewById(R.id.tv_home_sum);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }

        HomeBean homeBean=homeBeanList.get(position);
        viewHolder.tv_sum.setVisibility(View.GONE);
        if (homeBean.isVisible()){

            viewHolder.tv_content.setText(homeBean.getContent());
        }else{
            convertView.setVisibility(View.GONE);
        }
        return convertView;
    }
    private class ViewHolder{
    private TextView tv_content;
        private SquareTextView tv_sum;
    }
}
