package com.gentlehealthcare.mobilecare.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.net.bean.BloodProductBean2;
import com.gentlehealthcare.mobilecare.tool.StringTool;

import java.util.List;

/**
 * @author HR_ZYY
 * @ClassName: BloodProductsListAdapter
 * @Description: 输血 血品列表 适配器
 * @date 2015年9月14日
 */
public class BloodProductsListAdapter extends BaseAdapter {

    private Context context;

    private List<BloodProductBean2> trans = null;

    private int position = 0;

    public BloodProductsListAdapter(Context context, List<BloodProductBean2> trans) {
        this.context = context;
        this.trans = trans;
    }

    public void notifyChanged(int position) {

        this.position = position;
        notifyDataSetInvalidated();
    }

    @Override
    public int getCount() {
        return trans.size();
    }

    @Override
    public Object getItem(int position) {
        return trans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.test, null);
            viewHolder.btn_check = (TextView) convertView.findViewById(R.id.btn_check);
            viewHolder.tv_order_title = (TextView) convertView.findViewById(R.id.tv_order_title);
            viewHolder.tv_order_context = (TextView) convertView.findViewById(R.id.tv_order_context);
            viewHolder.tv_dosage = (TextView) convertView.findViewById(R.id.tv_dosage);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        BloodProductBean2 tran = trans.get(position);

        if (tran.getBloodStatus().equals("0")) {
            viewHolder.btn_check.setBackgroundResource(R.drawable.btn_finish);
        }
        String patInfo = tran.getPatName() + " " + tran.getPatCode();
        String date = StringTool.dateToTime(tran.getBloodOutDate());
        viewHolder.tv_order_title.setText(date + " " + patInfo);

        String txt;
        if(StringTool.isEmpty(tran.getBloodTypeName())){
            txt=" ";
        }else{
            txt=tran.getBloodTypeName()+"\n";
        }
        String rh;
       if(StringTool.isEmpty(tran.getBloodGroupDesc())){
           rh=" ";
       }else{
           rh=tran.getBloodGroupDesc();
       }
        viewHolder.tv_order_context.setText(txt+"("+tran.getBloodDonorCode()+","+tran.getBloodProductCode()+","+rh+")");

        String str;
        if (StringTool.isEmpty(tran.getBloodUnit())) {
            str =" ";
        }else{
            str=tran.getBloodUnit();
        }
        viewHolder.tv_dosage.setText(str);
        return convertView;
    }

    private static class ViewHolder {
        private TextView btn_check;
        private TextView tv_order_title;
        private TextView tv_order_context;
        private TextView tv_dosage;


    }

}
