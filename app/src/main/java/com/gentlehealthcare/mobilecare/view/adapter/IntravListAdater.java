package com.gentlehealthcare.mobilecare.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.net.bean.OrderItemBean;
import com.gentlehealthcare.mobilecare.tool.StringTool;

import java.util.List;

public class IntravListAdater extends BaseAdapter {

    private List<OrderItemBean> OrderList;
    private Context context;

    public IntravListAdater(Context context, List<OrderItemBean> data) {
        this.OrderList = data;
        this.context = context;
    }

    @Override
    public int getCount() {

        return OrderList.size();
    }

    @Override
    public Object getItem(int position) {

        return OrderList.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_intrav, null);
            viewHolder = new ViewHolder();
            viewHolder.time = (TextView) convertView.findViewById(R.id.tv_time);
            viewHolder.orders = (TextView) convertView.findViewById(R.id.tv_medicineinfo);
            viewHolder.qd = (TextView) convertView.findViewById(R.id.tv_order_qd);

            viewHolder.speed = (TextView) convertView.findViewById(R.id.tv_order_speed);
            viewHolder.type = (TextView) convertView.findViewById(R.id.tv_order_method);
            viewHolder.tool = (TextView) convertView.findViewById(R.id.tv_order_tool);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        OrderItemBean OrderItemBean = OrderList.get(position);
        viewHolder.time.setText(OrderItemBean.getPlanStartTime());
        if(OrderItemBean.getOrderText().contains("||")){
            String orderText=OrderItemBean.getOrderText().replace("||","\n");
            viewHolder.orders.setText(StringTool.toUnify(orderText));
        }else{
            viewHolder.orders.setText(StringTool.toUnify(OrderItemBean.getOrderText()));
        }
        viewHolder.qd.setText(OrderItemBean.getFrequency());
        viewHolder.speed.setText(OrderItemBean.getSpeed()+"滴/分");
        viewHolder.type.setText(OrderItemBean.getAdministration());
        if(OrderItemBean.getInjectionTool()=="1"){
            viewHolder.tool.setText("钢针");
        }else{
            viewHolder.tool.setText("留置针");
        }


        return convertView;
    }

    private static class ViewHolder {
        TextView time;
        TextView orders;
        TextView qd;
        TextView speed;
        TextView type;
        TextView tool;
    }


}
