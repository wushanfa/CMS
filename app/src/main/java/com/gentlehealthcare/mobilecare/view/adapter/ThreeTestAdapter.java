package com.gentlehealthcare.mobilecare.view.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;

import java.util.List;

/**
 * Created by ouyang on 2015/5/26.
 */
public class ThreeTestAdapter extends BaseAdapter {
    private List<SyncPatientBean> patientList;
    private Context context;
    private int selectPosition = 0;

    public ThreeTestAdapter(List<SyncPatientBean> patientList, Context context) {
        this.patientList = patientList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return patientList.size();
    }

    @Override
    public Object getItem(int position) {
        return patientList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_threetest_item, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_bednum = (TextView) convertView.findViewById(R.id.tv_bednumber);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_name.setText(patientList.get(position).getName());
        //viewHolder.tv_bednum.setText(patientList.get(position).getBedLabel() + "床");

        if(patientList.get(position).getBedLabel()!=null){
            if(patientList.get(position).getBedLabel().contains("-")){
                String bedNum=patientList.get(position).getBedLabel().split("-")[1]+"床";
               // String xuNi="<small>"+patientList.get(position).getBedLabel().split("-")[0]+"</small>";
               // viewHolder.tv_bednum.setText(Html.fromHtml(bedNum + "<br/>" + xuNi));
                viewHolder.tv_bednum.setText(bedNum);
            }else{
                viewHolder.tv_bednum.setText(patientList.get(position).getBedLabel()+"床");
            }
        }else {
            viewHolder.tv_bednum.setText(Html.fromHtml("<small>未分配</small>"));
        }

        int nameVisibility = 0;
        int bednumVisibility = 0;
        if (selectPosition == position) {
            nameVisibility = View.VISIBLE;
            bednumVisibility = View.GONE;
        } else {
            nameVisibility = View.GONE;
            bednumVisibility = View.VISIBLE;
        }
        viewHolder.tv_name.setVisibility(nameVisibility);
        viewHolder.tv_bednum.setVisibility(bednumVisibility);
        if (position == selectPosition) {
            convertView.setPadding(0, 0, 0, 0);
        } else {
            convertView.setPadding(0, 10, 0, 10);
        }
        int viewBg = 0;
        if (selectPosition == position) {
            viewBg = R.drawable.scroll_button_choosen;
        } else {
            viewBg = R.drawable.scroll_button_not_choosen;
        }
        convertView.setBackgroundResource(viewBg);
        return convertView;
    }
    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
    }

    public class ViewHolder {
        TextView tv_bednum;
        TextView tv_name;
    }
}
