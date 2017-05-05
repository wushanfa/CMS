package com.gentlehealthcare.mobilecare.view.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.constant.MedicineConstant;
import com.gentlehealthcare.mobilecare.net.bean.OrderItemBean;
import com.gentlehealthcare.mobilecare.tool.DateTool;

import java.util.List;

/**
 * @author ouyang
 * @ClassName: MedicineListAdapter
 * @Description: 给药 药品列表 适配器
 * @date 2015年3月12日 下午3:06:47
 */
public class MedicineListAdapter extends BaseAdapter {

    private Context context;


    private List<OrderItemBean> orderItemBeans = null;

    private Resources resources;

    private int position = 0;

    private boolean isWhite = false;
    private String group = "";
    private String startTime = "";

    public MedicineListAdapter(Context context, List<OrderItemBean> orderItemBeans) {
        super();
        this.context = context;
        this.orderItemBeans = orderItemBeans;
        resources = context.getResources();
    }

    public void notifyChanged(int position) {

        this.position = position;
        notifyDataSetInvalidated();
    }

    @Override
    public int getCount() {
        return orderItemBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return orderItemBeans.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_medicine_complete, null);
            viewHolder.tv_medicineinfo = (TextView) convertView.findViewById(R.id.tv_medicineinfo);
//            viewHolder.tv_medicineother = (TextView) convertView.findViewById(R.id.tv_medicineother);
            viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            viewHolder.tv_order_qd = (TextView) convertView.findViewById(R.id.tv_order_qd);
            viewHolder.tv_order_speed = (TextView) convertView.findViewById(R.id.tv_order_speed);
            viewHolder.tv_order_method = (TextView) convertView.findViewById(R.id.tv_order_method);
            viewHolder.tv_order_tool = (TextView) convertView.findViewById(R.id.tv_order_tool);
            viewHolder.tv_line = (TextView) convertView.findViewById(R.id.tv_line);
            viewHolder.tv_order_insulin = (TextView) convertView.findViewById(R.id.tv_order_insulin);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        /**
         * 分割子医嘱
         */
        OrderItemBean orderItemBean = orderItemBeans.get(position);
        String[] array = orderItemBean.getOrderText().split("\\|\\|");
        StringBuffer sb = new StringBuffer();
//        for (int i = 0; i < array.length; i++) {
//            String str = array[i];
//            int num = str.lastIndexOf(",");
//            String partOne = str.substring(0, num);
//            String partTwo = str.substring(num + 1);
//            sb.append(" ● " + partOne + "            " + partTwo);
//            if (i != array.length - 1) {
//                sb.append("\n");
//            }
//        } by zyy 2016-5-15
//        viewHolder.tv_medicineinfo.setText(Utils.ToSBC(sb.toString()));
        for(int i=0;i<array.length;i++){
            if(i!=array.length-1){
                sb.append(array[i]+"\n");
            }else{
                sb.append(array[i]);
            }
        }
        viewHolder.tv_medicineinfo.setText(sb.toString());
        /**
         * 判断钢针or留置针是否显示
         */
        if (orderItemBean.getInjectionTool() != null && !"".equals(orderItemBean.getInjectionTool())) {
            String tool = orderItemBean.getInjectionTool().equals("0") ? "留置针" : "钢针";
            viewHolder.tv_order_tool.setText(tool);
        } else {
            viewHolder.tv_order_tool.setVisibility(View.GONE);
        }
        /**
         * qd
         */
        viewHolder.tv_order_qd.setText(orderItemBean.getFrequency());
        /**
         * 判断滴速是否显示
         */
        if (orderItemBean.getSpeed() != null && !orderItemBean.getSpeed().equals("")) {
            viewHolder.tv_order_speed.setText(orderItemBean.getSpeed() + "滴/分");
        } else {
            viewHolder.tv_order_speed.setVisibility(View.GONE);
        }
        /**
         * 判断静滴是否显示
         */
        viewHolder.tv_order_method.setText(orderItemBean.getAdministration());
        if (orderItemBean.getAdministration() != null && !orderItemBean.getAdministration().equals("")) {
            viewHolder.tv_order_method.setText(orderItemBean.getAdministration());
        } else {
            viewHolder.tv_order_method.setVisibility(View.GONE);
        }
        /**
         * 修改时间显示格式
         */
        if (orderItemBean.getPlanStartTime() != null && orderItemBean.getPlanStartTime().length() > 10) {
            viewHolder.tv_time.setText(orderItemBean.getPlanStartTime().substring(0, 10) + " " + DateTool
                    .getHourAndMinute(orderItemBean.getPlanStartTime()));
        }
        /**
         * 判断胰岛素显示哪些内容
         */
        if (orderItemBean.getTemplateId().equals("AA-3")) {
            viewHolder.tv_order_method.setVisibility(View.GONE);
            viewHolder.tv_order_insulin.setVisibility(View.VISIBLE);
            viewHolder.tv_order_insulin.setText(orderItemBean.getDosage() + "U");
            viewHolder.tv_order_speed.setVisibility(View.GONE);
        } else {
//            viewHolder.tv_order_method.setVisibility(View.VISIBLE);
            viewHolder.tv_order_insulin.setVisibility(View.GONE);
//            viewHolder.tv_order_speed.setVisibility(View.VISIBLE);
        }
        if (orderItemBean.getPerformStatus().equals(MedicineConstant.STATE_WAITING)) {
            viewHolder.tv_medicineinfo.setTextColor(resources.getColor(R.color.text_green_dark));
        } else if (orderItemBean.getPerformStatus().equals(MedicineConstant.STATE_EXECUTING)) {
            viewHolder.tv_medicineinfo.setTextColor(resources.getColor(R.color.white));
        } else if (orderItemBean.getPerformStatus().equals(MedicineConstant.STATE_EXECUTED) || orderItemBean
                .getPerformStatus().equals(MedicineConstant.STATE_CANCEL)) {
            viewHolder.tv_medicineinfo.setTextColor(resources.getColor(R.color.text_green_dark));
            viewHolder.tv_medicineinfo.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        } else if (orderItemBean.getPerformStatus().equals(MedicineConstant.STATE_EXCEPTION)) {
            viewHolder.tv_medicineinfo.setTextColor(resources.getColor(R.color.red));
        }
        /**
         * 更换颜色显示样式
         */
        if (position == this.position) {
            convertView.setBackgroundResource(R.drawable.skin_btnblue_normal);
            viewHolder.tv_order_qd.setBackgroundResource(R.color.circuit_piece);
            viewHolder.tv_order_speed.setBackgroundResource(R.color.circuit_piece);
            viewHolder.tv_order_method.setBackgroundResource(R.color.circuit_piece);
            viewHolder.tv_order_tool.setBackgroundResource(R.color.circuit_piece);
            viewHolder.tv_order_qd.setTextColor(resources.getColor(R.color.white));
            viewHolder.tv_order_speed.setTextColor(resources.getColor(R.color.white));
            viewHolder.tv_order_method.setTextColor(resources.getColor(R.color.white));
            viewHolder.tv_order_tool.setTextColor(resources.getColor(R.color.white));
            viewHolder.tv_time.setTextColor(resources.getColor(R.color.white));
            viewHolder.tv_medicineinfo.setTextColor(resources.getColor(R.color.white));
            viewHolder.tv_line.setBackgroundResource(R.color.white);
            viewHolder.tv_order_insulin.setBackgroundResource(R.color.circuit_piece);
            viewHolder.tv_order_insulin.setTextColor(resources.getColor(R.color.white));
        } else {
            viewHolder.tv_time.setTextColor(resources.getColor(R.color.black_text_content));
            convertView.setBackgroundResource(R.drawable.skin_btngray_overdue);
            viewHolder.tv_order_qd.setBackgroundResource(R.drawable.skin_black_transparent);
            viewHolder.tv_order_speed.setBackgroundResource(R.drawable.skin_black_transparent);
            viewHolder.tv_order_method.setBackgroundResource(R.drawable.skin_black_transparent);
            viewHolder.tv_order_tool.setBackgroundResource(R.drawable.skin_black_transparent);
            viewHolder.tv_order_qd.setTextColor(resources.getColor(R.color.black_text_content));
            viewHolder.tv_order_speed.setTextColor(resources.getColor(R.color.black_text_content));
            viewHolder.tv_order_method.setTextColor(resources.getColor(R.color.black_text_content));
            viewHolder.tv_order_tool.setTextColor(resources.getColor(R.color.black_text_content));
            viewHolder.tv_medicineinfo.setTextColor(resources.getColor(R.color.black_text_content));
            viewHolder.tv_line.setBackgroundResource(R.color.black_text_content);
            viewHolder.tv_order_insulin.setTextColor(resources.getColor(R.color.black_text_content));
            viewHolder.tv_order_insulin.setBackgroundResource(R.drawable.skin_black_transparent);
        }

        return convertView;
    }

    private class ViewHolder {
        private TextView tv_medicineinfo;
        private TextView tv_time;
        private TextView tv_order_qd;
        private TextView tv_order_speed;
        private TextView tv_order_method;
        private TextView tv_order_tool;
        private TextView tv_line;
        private TextView tv_order_insulin;
//        private TextView tv_medicineother;
    }

}
