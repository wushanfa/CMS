package com.gentlehealthcare.mobilecare.view.adapter;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;

import java.util.List;

/**
 * @author ouyang
 * @ClassName: PatientListAdapter
 * @Description:
 * @date 2015年3月11日 上午9:21:38
 */
public class PatientListAdapter extends BaseAdapter {

    private Context context;

    private List<SyncPatientBean> patients;


    public PatientListAdapter(Context context, List<SyncPatientBean> patients) {

        super();
        this.context = context;
        this.patients = patients;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return (patients.size() == 0) ? 0 : patients.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return patients.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SyncPatientBean patient = patients.get(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_patients, null);
            viewHolder.tvBedNumber = (TextView) convertView.findViewById(R.id.tv_bed_number);
            viewHolder.ivPatientPhoto = (ImageView) convertView.findViewById(R.id.iv_patient_photo);
            viewHolder.tvAge = (TextView) convertView.findViewById(R.id.tv_age);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_patientname);
            viewHolder.tvPatientId = (TextView) convertView.findViewById(R.id.tv_patientid);
            viewHolder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
            viewHolder.tvLevel = (TextView) convertView.findViewById(R.id.tv_level);
            viewHolder.tvWorkProgress = (TextView) convertView.findViewById(R.id.tv_work_progress);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(patient.getBedLabel()!=null){
            if(patient.getBedLabel().contains("-")){
                String bedNum=patient.getBedLabel().split("-")[1]+"床";
                String xuNi="<small>"+patient.getBedLabel().split("-")[0]+"</small>";
                viewHolder.tvBedNumber.setText(Html.fromHtml(bedNum+"<br/>"+xuNi));
            }else{
                viewHolder.tvBedNumber.setText(patient.getBedLabel()+"床");
            }
        }else {
            viewHolder.tvBedNumber.setText(Html.fromHtml("<small>未分配</small>"));
        }
        viewHolder.tvWorkProgress.setText("待" + patient.getPerformTask() + "中" + patient.getExecutingTask() +
                "已" + patient.getComplitedTask());
        if (patient.getSex() != null) {
            int photoRes = -1;
            if (patient.getSex().equals("男")) {
                photoRes = R.drawable.patient_male;
            } else if (patient.getSex().equals("女")) {
                photoRes = R.drawable.patient_female;
            }
            if (photoRes != -1) {
                viewHolder.ivPatientPhoto.setImageResource(photoRes);
            } else {
                viewHolder.ivPatientPhoto.setImageBitmap(null);
            }
        } else {
            viewHolder.ivPatientPhoto.setImageBitmap(null);
        }
        if (!TextUtils.isEmpty(patient.getAge())) {
            viewHolder.tvAge.setText(String.format("%s岁", patient.getAge()));
            viewHolder.tvAge.setVisibility(View.VISIBLE);
            Log.v("AA", "VISIBLE");
        } else {
            viewHolder.tvAge.setVisibility(View.GONE);
            Log.v("AA", "GONE");
        }
        if (patient.getName() != null)
            viewHolder.tvName.setText(patient.getName());
        if (patient.getPatId() != null)
            viewHolder.tvPatientId.setText("病人ID:"+patient.getPatCode());
        if (patient.getAdmissionDate() != null && patient.getAdmissionDate().length() > 15) {
            String date = patient.getAdmissionDate();
            viewHolder.tvDate.setText(date.substring(0, 16));
        } else {
            viewHolder.tvDate.setText("");
        }
        String level = null;
        int levelColor = -1;
        if (patient.getNursingGrade() != null) {
            String grade = patient.getNursingGrade();
            if (grade.equals("A")) {
                level = "特级";
                levelColor = R.drawable.cricle_red;
            } else if (grade.equals("B")) {
                level = "一级";
                levelColor = R.drawable.cricle_deep_orange;
            } else if (grade.equals("C")) {
                level = "二级";
                levelColor = R.drawable.cricle_orange;
            } else if (grade.equals("D")) {
                level = "三级";
                levelColor = R.drawable.cricle_yellow;
            }
        }
        if (level != null && levelColor != -1) {
            viewHolder.tvLevel.setText(level);
            viewHolder.tvLevel.setVisibility(View.VISIBLE);
            viewHolder.tvLevel.setBackgroundResource(levelColor);
        } else {
            viewHolder.tvLevel.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ViewHolder {
        TextView tvBedNumber;
        ImageView ivPatientPhoto;
        TextView tvAge;
        TextView tvName;
        TextView tvPatientId;
        TextView tvDate;
        TextView tvLevel;
        TextView tvWorkProgress;
    }
}
