package com.gentlehealthcare.mobilecare.view.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.activity.home.RecordPioAppraisalAct;
import com.gentlehealthcare.mobilecare.net.bean.LoadPioRecordBean;
import com.gentlehealthcare.mobilecare.net.bean.PioItemInfo;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;

/**
 * Created by fengdianxun on 15-5-31.
 */
public class PioRecordAdapter extends BaseAdapter {

    private List<LoadPioRecordBean> list;
    private Context context;
    private SyncPatientBean patient;

    public PioRecordAdapter(List<LoadPioRecordBean> list, Context context,
                            SyncPatientBean patient) {
        this.list = list;
        this.context = context;
        this.patient = patient;
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
        ViewHolder viewHolder = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_pio_records, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.tv_cause = (TextView) view.findViewById(R.id.tv_cause);
            viewHolder.tv_effect = (TextView) view.findViewById(R.id.tv_effect);
            viewHolder.tv_estimatetime = (TextView) view.findViewById(R.id.tv_estimatetime);
            viewHolder.tv_target = (TextView) view.findViewById(R.id.tv_target);
            viewHolder.tv_measures = (TextView) view.findViewById(R.id.tv_measures);
            viewHolder.tv_problem = (TextView) view.findViewById(R.id.tv_problem);
            viewHolder.tv_person = (TextView) view.findViewById(R.id.tv_person);
            viewHolder.tv_time = (TextView) view.findViewById(R.id.tv_time);
            viewHolder.btn_comment = (Button) view.findViewById(R.id.btn_comment);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if (!list.get(i).getAppraisal().isEmpty()) {
            viewHolder.tv_person.setVisibility(View.VISIBLE);
            viewHolder.tv_estimatetime.setVisibility(View.VISIBLE);
            viewHolder.tv_effect.setVisibility(View.VISIBLE);
            viewHolder.tv_estimatetime.setText("评价:" + list.get(i).getLogTime2().substring(0,16));
            viewHolder.tv_effect.setText("内容:" + getItemNameToString(list.get(i).getAppraisal()));
            viewHolder.tv_person.setText("评价人:" + list.get(i).getLogBy2());
            viewHolder.btn_comment.setVisibility(View.GONE);
        } else {
            viewHolder.tv_person.setVisibility(View.GONE);
            viewHolder.tv_estimatetime.setVisibility(View.GONE);
            viewHolder.tv_effect.setVisibility(View.GONE);
            viewHolder.btn_comment.setVisibility(View.VISIBLE);
        }
        viewHolder.tv_time.setText("" + list.get(i).getLogTim1().substring(0,16));
        viewHolder.tv_problem.setText("问题:" + list.get(i).getProblemName());
        String measure = "";
        if (!list.get(i).getMeasure().isEmpty())
            measure = getItemNameToString(list.get(i).getMeasure());
        viewHolder.tv_measures.setText("措施:" + (measure));
        String target = "";
        if (!list.get(i).getTarget().isEmpty())
            target = getItemNameToString(list.get(i).getTarget());
        viewHolder.tv_target.setText("目标:" + (target));
        String cause = "";
        if (!list.get(i).getCauses().isEmpty())
            cause = getItemNameToString(list.get(i).getCauses());
        viewHolder.tv_cause.setText("致因:" + (cause));
        viewHolder.position = i;
        viewHolder.SetOnClick();

        return view;
    }

    private String getItemNameToString(List<PioItemInfo> list) {
        StringBuffer sb = new StringBuffer();
        if (list.get(0).getItemName() == null) {
            sb.append("");
        } else {
            for (int i = 0; i < list.size(); i++) {
                sb.append(i + 1);
                sb.append(".");
                sb.append(list.get(i).getItemName());
                sb.append("\b\b");
            }
        }
        return sb.toString();
    }

    private class ViewHolder {
        private TextView tv_time, tv_problem, tv_cause, tv_target, tv_measures, tv_estimatetime, tv_person,
                tv_effect;
        private Button btn_comment;
        private int position;

        public void SetOnClick() {
            btn_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, RecordPioAppraisalAct.class);
                    intent.putExtra("pio", list.get(position));
                    intent.putExtra("patient", patient);
                    context.startActivity(intent);
                }
            });
        }
    }
}
