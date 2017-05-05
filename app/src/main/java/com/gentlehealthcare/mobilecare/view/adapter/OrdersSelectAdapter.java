package com.gentlehealthcare.mobilecare.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.net.bean.BloodProductBean2;

import java.util.List;

/**
 * Create by zyy
 * 血袋选择Dialog
 */
public class OrdersSelectAdapter extends BaseAdapter {

    private Context context;

    private List<BloodProductBean2> orderListBeanList;

    public OrdersSelectAdapter(Context context, List<BloodProductBean2> orderListBeanList) {
        super();
        this.context = context;
        this.orderListBeanList = orderListBeanList;
    }

    @Override
    public int getCount() {
        if (orderListBeanList == null || orderListBeanList.isEmpty() || orderListBeanList.size() == 0) {
            return 0;
        } else {
            return orderListBeanList.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return orderListBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BloodProductBean2 orderListBean = orderListBeanList.get(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_orders_select, null);
            viewHolder.tvOrder = (TextView) convertView.findViewById(R.id.tv_order);
            viewHolder.tvPatInfo = (TextView) convertView.findViewById(R.id.tv_pat_info);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvPatInfo.setText(orderListBean.getPatName()+" "+orderListBean.getPatCode());
        viewHolder.tvOrder.setText(orderListBean.getBloodTypeName()+"\n"+"("+orderListBean.getBloodDonorCode()+"/"+orderListBean.getBloodProductCode()+")");
        return convertView;
    }

    class ViewHolder {
        TextView tvOrder;
        TextView tvPatInfo;

    }
}
