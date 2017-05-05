package com.gentlehealthcare.mobilecare.presenter;

import android.content.Context;

import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.model.impl.MsgModel;
import com.gentlehealthcare.mobilecare.model.impl.PatientModel;
import com.gentlehealthcare.mobilecare.net.bean.SyncDeptPatientBean;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.tool.GroupInfoSave;
import com.gentlehealthcare.mobilecare.view.IMsgView;

import java.util.List;

/**
 * Created by Zyy on 2016/8/31.
 * 类说明：
 */

public class MsgPresenter {
    private IMsgView view;
    private PatientModel patientModel;
    private MsgModel msgModel;
    public MsgPresenter(IMsgView view){
        this.view=view;
    }
    public void receiveData(){
        view.receiveData();
    }

    public void intialView(){
        view.intialView();
    }

    public void getPatients(Context context, final String patCode) {

        patientModel = new PatientModel();
        final List<SyncPatientBean> bean = UserInfo.getDeptPatient();
        if (bean.isEmpty()) {
            String str = GroupInfoSave.getInstance(context).get();
            SyncDeptPatientBean syncDeptPatientBean = new SyncDeptPatientBean();
            syncDeptPatientBean.setWardCode(str);
            String url = UrlConstant.GetSyncDeptPatient()
                    + syncDeptPatientBean.toString();
            patientModel.syncDeptPatientTable(url, new PatientModel.OnLoadPatientListListener() {
                @Override
                public void onSuccess(List<SyncPatientBean> list) {
                    UserInfo.setDeptPatient(list);
                    for (int i = 0; i < UserInfo.getDeptPatient().size(); i++) {
                        SyncPatientBean patientBean = UserInfo.getDeptPatient().get(i);
                        if (patCode.equals(patientBean.getPatCode())) {
                            view.setPatientInfo(patientBean);
                            break;
                        }
                    }
                }

                @Override
                public void onFailure(String msg, Exception e) {

                }
            });
        }else {
            for (int i = 0; i < bean.size(); i++) {
                SyncPatientBean  patientBean= bean.get(i);
                if (patCode.equals(patientBean.getPatCode())) {
                    view.setPatientInfo(patientBean);
                    break;
                }
            }
        }
    }

    public void handlerMsg(String confirmStatus,String username,String noticeId){
        msgModel=new MsgModel();
        msgModel.handleMsg(confirmStatus,username,noticeId, new MsgModel.handleMsgListener() {
            @Override
            public void handlerMsgSucessed(String msg) {

            }

            @Override
            public void handlerMsgFailed(String msg, Exception e) {

            }
        });
    }
}
