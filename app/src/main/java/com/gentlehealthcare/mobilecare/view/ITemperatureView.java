package com.gentlehealthcare.mobilecare.view;

import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;

import java.util.List;

/**
 * Created by zyy on 2016/5/18.
 */
public interface ITemperatureView {

    void showProgressDialog(String msg);

    void dismissProgressDialog();

    void showToast(String str);

    void initialSrc();

    void showKey(String[] strs, int flag);

    void setPatientInfo(SyncPatientBean patientInfo);
    /**
     * 获取部门病人数据
     */
    void setPatient(List<SyncPatientBean> list);
    void showPatients();
}
