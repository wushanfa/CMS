package com.gentlehealthcare.mobilecare.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.activity.gradedcareevaluation.NursingMeasuresAct;
import com.gentlehealthcare.mobilecare.bean.GradedCareEvaluationBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/2.
 */

public class GradedCareEvaluationAdapter extends BaseAdapter {
    private ArrayList<GradedCareEvaluationBean> mList;
    private Context mContext;
    private String charSequence;

    public GradedCareEvaluationAdapter(ArrayList<GradedCareEvaluationBean> list, Context context) {
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
            convertView = View.inflate(mContext, R.layout.item_graded_care_evaluation, null);
            view.time = (TextView) convertView.findViewById(R.id.tv_graded_care_evaluation_time);
            view.name = (TextView) convertView.findViewById(R.id.tv_graded_care_evaluation_name);
            view.type = (TextView) convertView.findViewById(R.id.tv_graded_care_evaluation_type);
            view.type.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线;
            view.measures = (TextView) convertView.findViewById(R.id.tv_graded_care_evaluation_measures);
            view.rl_measures = (RelativeLayout) convertView.findViewById(R.id.rl_graded_care_evaluation_measures);
            view.rl_measures.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent Intent = new Intent();
                    Intent.setClass(mContext, NursingMeasuresAct.class);
                    mContext.startActivity(Intent);
                }
            });
            convertView.setTag(view);
        } else {
            view = (ViewHodler) convertView.getTag();
        }
        view.time.setText(mList.get(position).getTime());
        view.name.setText(mList.get(position).getName());
        view.type.setText(mList.get(position).getType());
        if ("无需依赖".equals(mList.get(position).getType())) {
            view.type.setTextColor(mContext.getResources().getColor(R.color.black));
        } else {
            view.type.setTextColor(mContext.getResources().getColor(R.color.red));
        }
        view.measures.setText(mList.get(position).getMeasure());
        return convertView;
    }

    class ViewHodler {
        TextView time, type, name, measures;
        RelativeLayout rl_measures;
    }
}

