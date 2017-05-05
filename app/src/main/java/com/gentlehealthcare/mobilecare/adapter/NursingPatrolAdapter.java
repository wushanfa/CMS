package com.gentlehealthcare.mobilecare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.bean.orders.OrderListBean;
import com.gentlehealthcare.mobilecare.tool.StringTool;

import java.util.List;

import static com.gentlehealthcare.mobilecare.R.id.btn_check;
import static com.gentlehealthcare.mobilecare.R.id.tv_dosage;
import static com.gentlehealthcare.mobilecare.R.id.tv_order_context;
import static com.gentlehealthcare.mobilecare.R.id.tv_order_title;

/**
 * @author zyy
 * @Description: 护理巡视底部执行中医嘱适配器
 * @date 2015年3月11日 上午9:21:38
 */
public class NursingPatrolAdapter extends BaseAdapter {

    private Context context;
    private List<OrderListBean> orderListBeanList;
    private int lineCount;


    public NursingPatrolAdapter(Context context, List<OrderListBean> orderListBeanList) {

        super();
        this.context = context;
        this.orderListBeanList = orderListBeanList;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return (orderListBeanList.size() == 0) ? 0 : orderListBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return orderListBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //OrderListBean orderListBean = orderListBeanList.get(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_orders_patrol, null);
            viewHolder.btn_check = (TextView) convertView.findViewById(btn_check);
            viewHolder.tv_order_title = (TextView) convertView.findViewById(tv_order_title);
            viewHolder.tv_order_context = (TextView) convertView.findViewById(tv_order_context);
            viewHolder.tv_dosage = (TextView) convertView.findViewById(tv_dosage);
           // viewHolder.tv_date = (TextView) convertView.findViewById(tv_date);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //viewHolder.tv_date.setText(DateTool.todayTomorryYesterday(bloodProductBean2.getTransDate()));
        viewHolder.tv_order_title.setText(StringTool.pieceSection(orderListBeanList.get(position)));
        String OrderContext = StringTool.toUnify(
                orderListBeanList.get(position).getOrderText()).replace("||", "\n");
        viewHolder.tv_order_context.setText(OrderContext);
        String orderDosage = orderListBeanList.get(position).getDosage()
                .replace("||", StringTool.cloneOrg("\n", lineCount));

        if (orderDosage == null || orderDosage == ""
                || "null".equals(orderDosage)) {
            viewHolder.tv_dosage.setText("");
        } else {
            String nullToSpace = orderDosage.replace("null", "");
            viewHolder.tv_dosage.setText(nullToSpace);
        }

        viewHolder.btn_check.setBackgroundResource(R.drawable.btn_exceting);

        return convertView;
    }

    class ViewHolder {
        TextView btn_check;
        TextView tv_order_title;
        TextView tv_order_context;
        TextView tv_dosage;
        TextView tv_date;
    }
}
