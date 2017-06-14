package com.gentlehealthcare.mobilecare.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.bean.GradedCareEvaluationBean;
import com.gentlehealthcare.mobilecare.bean.PopGradedCareMeasureBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/5.
 */

public class Pop_Grad_Care_MeasureAdapter extends BaseAdapter{
    private ArrayList<PopGradedCareMeasureBean> mList;
    private Context mContext;
    private String charSequence;

    public Pop_Grad_Care_MeasureAdapter(ArrayList<PopGradedCareMeasureBean> list, Context context) {
        mList = list;
        mContext = context;
    }

    @Override
    public int getCount() {
        if (mList != null) {
            return mList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHodler view;
        if (convertView == null) {
            view = new ViewHodler();
            convertView = View.inflate(mContext, R.layout.item_pop_grad_care_measure, null);
            view.measure = (TextView) convertView.findViewById(R.id.tv_pop_measures);
            convertView.setTag(view);
        } else {
            view = (ViewHodler) convertView.getTag();
        }
        view.measure.setText(mList.get(position).getMeasure());
        return convertView;
    }

    class ViewHodler {
        TextView measure;
    }
}
