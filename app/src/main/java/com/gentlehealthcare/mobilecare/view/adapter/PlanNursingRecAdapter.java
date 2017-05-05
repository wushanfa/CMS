package com.gentlehealthcare.mobilecare.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.net.bean.PlanNursingRecBean;

import java.util.List;

/**
 *
 * Created by ouyang on 15/7/7.
 */
public class PlanNursingRecAdapter extends BaseAdapter{
    private Context context;
    private List<PlanNursingRecBean> list;

    public PlanNursingRecAdapter(Context context, List<PlanNursingRecBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder=null;
        if (view==null){
            view= LayoutInflater.from(context).inflate(R.layout.act_plannursingrec_item, null);
            viewHolder=new ViewHolder();
            viewHolder.tv_left= (TextView) view.findViewById(R.id.tv_left);
            viewHolder.tv_center= (TextView) view.findViewById(R.id.tv_center);
            viewHolder.tv_right= (TextView) view.findViewById(R.id.tv_right);
            view.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) view.getTag();
        }
        viewHolder.tv_left.setText(list.get(i).getLogTime());
        viewHolder.tv_center.setText(list.get(i).getName());
        viewHolder.tv_right.setText(list.get(i).getPerformDesc());
        return view;
    }
    private class ViewHolder{
        private TextView tv_left;
        private TextView tv_center;
        private TextView tv_right;
    }
}
