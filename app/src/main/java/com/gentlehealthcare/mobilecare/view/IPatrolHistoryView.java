package com.gentlehealthcare.mobilecare.view;

import com.gentlehealthcare.mobilecare.bean.NursingHistoryBean;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;

import java.util.List;

/**
 * Created by Zyy on 2016/8/31.
 * 类说明：巡视历史记录
 */

public interface IPatrolHistoryView {

    void setPatientInfo(SyncPatientBean patientInfo);
    void setHistoryData(List<NursingHistoryBean> list);
    void initialSrc();
}
