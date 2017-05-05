package com.gentlehealthcare.mobilecare.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.net.bean.LoadIcuOrderBean;

import java.util.ArrayList;
import java.util.List;

public class ICUOrderDialogAdapter extends BaseAdapter {

    private Context context;
    private List<List<LoadIcuOrderBean>> list = new ArrayList<List<LoadIcuOrderBean>>();

    public ICUOrderDialogAdapter(Context context, List<List<LoadIcuOrderBean>> list) {
        super();
        this.context = context;
        this.list = list;
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
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.act_load_order_content, null);
            viewHolder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
            viewHolder.orderIcuLine = (TextView) convertView.findViewById(R.id.order_icu_line);
            viewHolder.tv_pinci = (TextView) convertView.findViewById(R.id.tv_pinci);
            viewHolder.tv_jigndi = (TextView) convertView.findViewById(R.id.tv_jigndi);
            viewHolder.tv_doctor = (TextView) convertView.findViewById(R.id.tv_doctor);
            viewHolder.ll_administration_layout = (LinearLayout) convertView.findViewById(R.id
                    .ll_administration_layout);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        List<LoadIcuOrderBean> orders = list.get(position);
        StringBuffer sb = new StringBuffer();
        /** 显示每条医嘱 */
        for (int i = 0; i < orders.size(); i++) {
            LoadIcuOrderBean order = orders.get(i);
            sb.append(orders.size() > 1 ? (i + 1 + ".") : "");
            sb.append(order.getOrderText()+"\n");
            sb.append("\t剂 量:" + order.getDosage() + order.getDosageUnits()+"\t");
            if (order.getOrderSubClass() != null) {
                sb.append("\t类 别：" + order.getOrderSubClass()+"\n");
            }
            String administration = (order.getAdministration() != null) ? order.getAdministration() + " " : "";
            viewHolder.tv_jigndi.setText("类型:" + administration);
            viewHolder.tv_pinci.setText("频次：" + order.getFrequency());
            viewHolder.tv_doctor.setText("医师：" + order.getOrderedDoctor());
            if (i < (orders.size() - 1)) {
                viewHolder.orderIcuLine.setVisibility(View.VISIBLE);
                viewHolder.ll_administration_layout.setVisibility(View.VISIBLE);
            }
        }
        viewHolder.tvContent.setText(sb.toString());
        return convertView;
    }

    private class ViewHolder {
        private LinearLayout ll_administration_layout;
        private TextView tvContent, orderIcuLine, tv_pinci, tv_jigndi, tv_doctor;
    }

}
