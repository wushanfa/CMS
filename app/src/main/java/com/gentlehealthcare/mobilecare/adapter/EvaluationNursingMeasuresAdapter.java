package com.gentlehealthcare.mobilecare.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.activity.evaluation.EvaluationNursingMeasuresDetailsAct;
import com.gentlehealthcare.mobilecare.activity.gradedcareevaluation.NursingMeasuresDetailsAct;
import com.gentlehealthcare.mobilecare.bean.NursingMeasureBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/5.
 */

public class EvaluationNursingMeasuresAdapter extends BaseAdapter {
    private ArrayList<NursingMeasureBean> mList;
    private Context mContext;
    private String charSequence;

    public EvaluationNursingMeasuresAdapter(ArrayList<NursingMeasureBean> list, Context context) {
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
        final ViewHodler view;
        if (convertView == null) {
            view = new ViewHodler();
            convertView = View.inflate(mContext, R.layout.item_nursing_measure, null);
            view.cbox_agreement = (CheckBox) convertView.findViewById(R.id.cbox_agreement);
            view.tv_nursing_measure = (TextView) convertView.findViewById(R.id.tv_nursing_measure);
            convertView.setTag(view);
        } else {
            view = (ViewHodler) convertView.getTag();
        }
        view.tv_nursing_measure.setText(mList.get(position).getMeasure());
        view.tv_nursing_measure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mContext, EvaluationNursingMeasuresDetailsAct.class);
                intent.putExtra("position",position);
                intent.putExtra("details", view.tv_nursing_measure.getText().toString());
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    class ViewHodler {
        TextView tv_nursing_measure;
        CheckBox cbox_agreement;
    }
}
