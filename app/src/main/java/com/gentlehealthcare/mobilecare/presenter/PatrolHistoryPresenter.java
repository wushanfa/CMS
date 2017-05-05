package com.gentlehealthcare.mobilecare.presenter;

import com.gentlehealthcare.mobilecare.bean.NursingHistoryBean;
import com.gentlehealthcare.mobilecare.model.IPatrolHistoryModel;
import com.gentlehealthcare.mobilecare.model.impl.PatroyHistoryModel;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.view.IPatrolHistoryView;

import java.util.List;

/**
 * Created by zyy on 2016/11/4.
 */
public class PatrolHistoryPresenter {

    private IPatrolHistoryView view;
    private IPatrolHistoryModel patrolHistoryModel;

    public PatrolHistoryPresenter(IPatrolHistoryView view) {
        this.view = view;
        patrolHistoryModel = new PatroyHistoryModel();
    }

    public void initialSrc(){
        view.initialSrc();
    }

    public void getNursingHistory(String patId, String wardCode) {
        patrolHistoryModel.getHistory(patId, wardCode, new PatroyHistoryModel.getPatrolHistoryListener() {
            @Override
            public void getPatrolHistorySucceed(List<NursingHistoryBean> msg) {
                view.setHistoryData(msg);
            }

            @Override
            public void getPatrolHistoryFailed(String msg, Exception e) {

            }
        });
    }

    public void setPatientInfo(SyncPatientBean patientInfo){
        view.setPatientInfo(patientInfo );
    }

}
