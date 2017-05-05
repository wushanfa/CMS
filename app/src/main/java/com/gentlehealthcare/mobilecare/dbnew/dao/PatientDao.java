package com.gentlehealthcare.mobilecare.dbnew.dao;

import android.database.sqlite.SQLiteDatabase;

import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;

import java.util.List;

/**
 * Created by zhiwei on 2016/4/8.
 */
public interface PatientDao {

    /**
     * 查找病人信息
     *
     * @return
     */
    List<SyncPatientBean> queryPatients(SQLiteDatabase database);

    /**
     * 跟新本地数据patient表
     */
    void updatePatient(SQLiteDatabase database, List<SyncPatientBean> syncPatientBeans);

}
