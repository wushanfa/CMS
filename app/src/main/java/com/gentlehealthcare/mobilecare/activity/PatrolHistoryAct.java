package com.gentlehealthcare.mobilecare.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.adapter.NursingPatrolHistoryAdapter;
import com.gentlehealthcare.mobilecare.bean.NursingHistoryBean;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.presenter.PatrolHistoryPresenter;
import com.gentlehealthcare.mobilecare.view.IPatrolHistoryView;

import java.util.List;

/**
 * Created by Zyy on 2016/11/4.
 * 类说明：巡视历史记录
 */

public class PatrolHistoryAct extends BaseActivity implements IPatrolHistoryView ,View.OnClickListener{
    private ListView lv_patrol_history_list;
    private PatrolHistoryPresenter patrolHistoryPresenter;
    private SyncPatientBean patientBean;
    private TextView tv_pat_name;
    private Button btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_patrol);
        patrolHistoryPresenter = new PatrolHistoryPresenter(this);
        patrolHistoryPresenter.initialSrc();
        patientBean= (SyncPatientBean) this.getIntent().getSerializableExtra(GlobalConstant.KEY_PATIENT);
        patrolHistoryPresenter.setPatientInfo(patientBean);
        patrolHistoryPresenter.getNursingHistory(patientBean.getPatId(),patientBean.getWardCode());
    }

    @Override
    protected void resetLayout() {

    }



    @Override
    public void setPatientInfo(SyncPatientBean patientInfo) {
        tv_pat_name.setText(patientInfo.getName() + " " + patientInfo.getWardCode() + " " + patientInfo.getSex());
    }

    @Override
    public void setHistoryData(List<NursingHistoryBean> list) {
        //NursingPatrolHistoryAdapter
        lv_patrol_history_list.setAdapter(new NursingPatrolHistoryAdapter(this,list));
    }

    @Override
    public void initialSrc() {
        lv_patrol_history_list= (ListView) findViewById(R.id.lv_patrol_history_list);
        tv_pat_name= (TextView) findViewById(R.id.tv_pat_name);
        btn_back= (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
        }
    }
}
