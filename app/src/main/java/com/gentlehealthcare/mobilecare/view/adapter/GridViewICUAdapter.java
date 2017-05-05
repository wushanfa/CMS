package com.gentlehealthcare.mobilecare.view.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhiwei on 2015/11/8.
 */
public class GridViewICUAdapter extends BaseAdapter {
    private List<SyncPatientBean> patients = new ArrayList<SyncPatientBean>();
    private Context context;
    private int whichOne;

    public GridViewICUAdapter(Context context, List<SyncPatientBean> patients, int whichOne) {
        this.patients = patients;
        this.context = context;
        this.whichOne = whichOne;
    }

    @Override
    public int getCount() {
        return patients.size();
    }

    @Override
    public Object getItem(int position) {
        return patients.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_gv_icu_b, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tv_line_gv_item = (TextView) convertView.findViewById(R.id.tv_line_gv_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(patients.get(position).getName()!=null){
            viewHolder.tv_name.setText(patients.get(position).getName());
        }
        if (whichOne == position) {
            viewHolder.tv_name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            viewHolder.tv_name.setTypeface(Typeface.DEFAULT_BOLD);
            viewHolder.tv_line_gv_item.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tv_name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            viewHolder.tv_line_gv_item.setVisibility(View.GONE);
        }

        return convertView;
    }

    public class ViewHolder {

        private TextView tv_name, tv_line_gv_item;
    }

    public void setWhichOne(int whichOne) {
        this.whichOne = whichOne;
    }
}
