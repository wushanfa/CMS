package com.gentlehealthcare.mobilecare.activity.patient.blood;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.activity.BaseFragment;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;

/**
 * 血糖检测值异常界面
 * Created by ouyang on 2015/5/28.
 */

@SuppressLint("ValidFragment")
public class BloodExeptionFra extends BaseFragment{
    private SyncPatientBean patient;

    public String getValue() {
        return value;
    }

    private String value;
    private TextView textView;
    private RadioGroup rg_blood_do;

    public  BloodExeptionFra(){

    }

    public BloodExeptionFra(SyncPatientBean patient, String value) {
        this.patient = patient;
        this.value = value;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_blood_exeption,null);
        textView= (TextView) view.findViewById(R.id.tv_exception_title);
        rg_blood_do= (RadioGroup) view.findViewById(R.id.rg_blood_do);
        rg_blood_do.check(R.id.rbtn_tab_stop);
        textView.setText("血糖值"+value+"异常，通知主治医师"+patient.getDoctorInCharge()+",确定异常处理方式");
        return view;
    }

    public int check(){
        int result=0;
        switch (rg_blood_do.getCheckedRadioButtonId()){
            case R.id.rbtn_tab_stop:
                result=0;
                break;
            case R.id.rbtn_tab_yanchi:
                result=1;
                break;
            case R.id.rbtn_tab_goon:
                result=2;
                break;
            case R.id.rbtn_tab_wait:
                result=3;
                break;
        }
        return result;
    }

    @Override
    protected void resetLayout(View view) {
        LinearLayout root = (LinearLayout) view
                .findViewById(R.id.root_fra_blood_exception);
        SupportDisplay.resetAllChildViewParam(root);
    }
}
