package com.gentlehealthcare.mobilecare.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.bean.PatientWorkBean;
import com.gentlehealthcare.mobilecare.constant.MedicineConstant;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientWorkInfo;

import java.util.List;

/**
 *  冰冷工作界面适配器
 * Created by ouyang on 15/7/6.
 */
public class PatientWorkAdapter extends BaseAdapter {
    private Context context;
    private List<SyncPatientWorkInfo> list;
    private int position = -1;

    public PatientWorkAdapter(Context context, List<SyncPatientWorkInfo> list) {
        this.context = context;
        this.list = list;
    }

    public void notifyChanged(int position) {

        this.position = position;
        notifyDataSetInvalidated();
//		notifyDataSetChanged();
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
        if(view==null){
            view= LayoutInflater.from(context).inflate(R.layout.activity_patientwork_item,null);
            viewHolder=new ViewHolder();
            viewHolder.tv_left= (TextView) view.findViewById(R.id.tv_left);
            viewHolder.tv_right= (TextView) view.findViewById(R.id.tv_right);
            view.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) view.getTag();
        }
        SyncPatientWorkInfo syncPatientWorkInfo=list.get(i);
        viewHolder.tv_left.setText(syncPatientWorkInfo.getTime());
        String[] strarray = syncPatientWorkInfo.getContent().split("\\|\\|");
        StringBuffer sb = new StringBuffer();
        for (int j=0;j<strarray.length;j++){
            sb.append(strarray[j]);
            if (j!=strarray.length-1)
                sb.append("\n");
        }
        viewHolder.tv_right.setText(sb.toString());
        viewHolder.tv_left.setTextColor(Color.BLACK);
        viewHolder.tv_right.setTextColor(Color.WHITE);
        if (i == this.position) {
            view.setBackgroundResource(R.drawable.skin_btnblue_normal);
        } else {
            if (syncPatientWorkInfo.getPerformStatus()==null)
                viewHolder.tv_right.setBackgroundResource(R.drawable.select_medicinelist_1);
            else if (syncPatientWorkInfo.getPerformStatus().equals(MedicineConstant.STATE_WAITING))
            viewHolder.tv_right.setBackgroundResource(R.drawable.select_medicinelist_complate);
            else if (syncPatientWorkInfo.getPerformStatus().equals(MedicineConstant.STATE_EXECUTING))
                viewHolder.tv_right.setBackgroundResource(R.drawable.select_medicinelist_ing);
            else if (syncPatientWorkInfo.getPerformStatus().equals(MedicineConstant.STATE_EXECUTED))
                viewHolder.tv_right.setBackgroundResource(R.drawable.select_medicinelist_wait);
            else if (syncPatientWorkInfo.getPerformStatus().equals(MedicineConstant.STATE_CANCEL))
                viewHolder.tv_right.setBackgroundResource(R.drawable.select_medicinelist_excepiton);
        }
        return view;
    }

    private class ViewHolder{
        private TextView tv_left,tv_right;
    }
}
