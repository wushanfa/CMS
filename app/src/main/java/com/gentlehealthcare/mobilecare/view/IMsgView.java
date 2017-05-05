package com.gentlehealthcare.mobilecare.view;

import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;

/**
 * Created by Zyy on 2016/8/31.
 * 类说明：消息提示
 */

public interface IMsgView {
    void receiveData();
    void intialView();
    void setPatientInfo(SyncPatientBean patientInfo);
}
