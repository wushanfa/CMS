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
 * @author HR_ZYY
 * @ClassName: BloodProductsListAdapter
 * @Description: 输血 血品列表 适配器
 * @date 2015年9月14日
 */
public class ScanYZBloodProductsListAdapter extends BaseAdapter {

    private Context context;

    private List<BloodProductBean2> bloodproductItemBeans = null;

    private int position = 0;

    private String strDate=null;

    private String strTime=null;

    public ScanYZBloodProductsListAdapter(Context context,
                                          List<BloodProductBean2> bloodproductItemBeans,String strDate,String strTime) {

        super();

        this.context = context;

        this.bloodproductItemBeans = bloodproductItemBeans;

        this.strDate=strDate;

        this.strTime=strTime;

    }

    public void notifyChanged(int position) {

        this.position = position;
        notifyDataSetInvalidated();
        // notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return bloodproductItemBeans.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return bloodproductItemBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_yizhu_xuepin, null);
            viewHolder.tv_xuepin_year = (TextView) convertView
                    .findViewById(R.id.tv_xuepin_year);
            viewHolder.tv_xuepin_time = (TextView) convertView
                    .findViewById(R.id.tv_xuepin_time);
            viewHolder.tv_xuepin_description = (TextView) convertView
                    .findViewById(R.id.tv_xuepin_description);
            viewHolder.tv_xuepin_status = (TextView) convertView.findViewById(R.id.tv_xuepin_status);

            viewHolder.tv_xuepin_frequency = (TextView) convertView.findViewById(R.id.tv_xuepin_frequency);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        BloodProductBean2 mBloodProductBean3 = bloodproductItemBeans
                .get(position);

        viewHolder.tv_xuepin_year.setText(strDate);
        viewHolder.tv_xuepin_time.setText(strTime);
        viewHolder.tv_xuepin_description.setText(mBloodProductBean3.getBloodTypeName()+mBloodProductBean3.getBloodCapacity()+mBloodProductBean3.getUnit());
        viewHolder.tv_xuepin_frequency.setText(mBloodProductBean3.getBloodGroup());

        if (mBloodProductBean3.getBloodStatus().equals("0")) {
            viewHolder.tv_xuepin_status.setText("待执行");
        } else if (mBloodProductBean3.getBloodStatus().equals("1")) {
            viewHolder.tv_xuepin_status.setText("执行中");
        } else if (mBloodProductBean3.getBloodStatus().equals("9")||mBloodProductBean3.getBloodStatus().equals("-1")) {
            viewHolder.tv_xuepin_status.setText("已执行");
        }


//        if (position == this.position) {
//            convertView.setBackgroundResource(R.drawable.skin_btnblue_normal);
//        } else {
//            convertView.setBackgroundResource(R.drawable.skin_btngray_overdue);
//
//        }

        return convertView;
    }

    private static class ViewHolder {
        private TextView tv_xuepin_year;
        private TextView tv_xuepin_time;
        private TextView tv_xuepin_description;
        private TextView tv_xuepin_frequency;
        private TextView tv_xuepin_status;
    }

}
