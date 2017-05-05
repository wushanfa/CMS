package com.gentlehealthcare.mobilecare.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.bean.MedicineListBean;
import com.gentlehealthcare.mobilecare.constant.MedicineConstant;
import com.gentlehealthcare.mobilecare.db.table.TB_MedicineInfo;
import com.gentlehealthcare.mobilecare.net.bean.OrderItemBean;
import com.gentlehealthcare.mobilecare.tool.DateTool;

import java.util.List;

/**
 *完成给药适配器
 * Created by ouyang on 2015/3/18.
 */
public class ComleteMedicineAdapter extends BaseAdapter {
    private Context context;
    private List<OrderItemBean> orderItemBeans=null;

    public ComleteMedicineAdapter(Context context, List<OrderItemBean> orderItemBeans) {
        this.context = context;
        this.orderItemBeans=orderItemBeans;
    }


    @Override
    public int getCount() {
        return orderItemBeans.size();
    }

    @Override
    public Object getItem(int i) {
        return orderItemBeans.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder=null;
        if (view==null){
            view=LayoutInflater.from(context).inflate(R.layout.activity_medicine_complete,null);
            viewHolder=new ViewHolder();
            viewHolder.tv_medicineinfo= (TextView) view.findViewById(R.id.tv_medicineinfo);
            viewHolder.tv_medicineother= (TextView) view.findViewById(R.id.tv_medicineother);
            view.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) view.getTag();
        }

        StringBuffer sb=new StringBuffer();
        OrderItemBean orderItemBean=orderItemBeans.get(position);
        String[] strarray = orderItemBean.getOrderText().split("\\|\\|");
        for (int i=0;i<strarray.length;i++){
            sb.append(strarray[i]);
            if (i!=strarray.length-1)
                sb.append("\n");
        }
        viewHolder.tv_medicineinfo.setText(sb.toString());
        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append("计划:"+DateTool.getHourAndMinute(orderItemBean.getPlanStartTime())+"\n");
        stringBuffer.append("频次:"+(orderItemBean.getFrequency()==null?"":orderItemBean.getFrequency())+"\n");
        stringBuffer.append("滴速:"+"\n");
        stringBuffer.append("给药途径:"+"\n");
        stringBuffer.append("执行:"+DateTool.getHourAndMinute(orderItemBean.getEventEntTime())+"\n");
        stringBuffer.append("操作人:"+orderItemBean.getNurseInOperate());
        viewHolder.tv_medicineother.setText(stringBuffer.toString());

        return view;
    }

    private class ViewHolder{
        private TextView tv_medicineother;
        private TextView tv_medicineinfo;
    }


}
