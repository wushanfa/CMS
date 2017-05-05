package com.gentlehealthcare.mobilecare.model;

import com.gentlehealthcare.mobilecare.model.impl.PatientModel;

/**
 * Created by zhiwei on 2016/5/6.
 */
public interface IPatientModel {

    /**
     * 同步部门病人
     */
    void syncDeptPatientTable(String url, PatientModel.OnLoadPatientListListener listListener);

}
