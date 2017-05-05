package com.gentlehealthcare.mobilecare.dbnew.impl;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.gentlehealthcare.mobilecare.dbnew.dao.PatientDao;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zyy
 */
public class PatientDaoImpl implements PatientDao {

    @Override
    public List<SyncPatientBean> queryPatients(SQLiteDatabase database) {
        List<SyncPatientBean> syncPatientBeans = new ArrayList<SyncPatientBean>();
        String sql = "select * from patient";
        try {
            Cursor cursor = database.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                String patId = cursor.getString(cursor.getColumnIndex("patId"));
                String patCode = cursor.getString(cursor.getColumnIndex("patCode"));
                String visitId = cursor.getString(cursor.getColumnIndex("visitId"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String sex = cursor.getString(cursor.getColumnIndex("sex"));
                String age = cursor.getString(cursor.getColumnIndex("age"));
                String wardCode = cursor.getString(cursor.getColumnIndex("wardCode"));
                String wardName = cursor.getString(cursor.getColumnIndex("wardName"));
                String nursingGrade = cursor.getString(cursor.getColumnIndex("nursingGrade"));
                String bedLabel = cursor.getString(cursor.getColumnIndex("bedLabel"));
                String admissionDate = cursor.getString(cursor.getColumnIndex("admissionDate"));
                String admissionDiagName = cursor.getString(cursor.getColumnIndex("admissionDiagName"));

                SyncPatientBean syncPatientBean = new SyncPatientBean();
                syncPatientBean.setPatId(patId);
                syncPatientBean.setPatCode(patCode);
                syncPatientBean.setVisitId(visitId);
                syncPatientBean.setName(name);
                syncPatientBean.setSex(sex);
                syncPatientBean.setAge(age);
                syncPatientBean.setWardCode(wardCode);
                syncPatientBean.setWardName(wardName);
                syncPatientBean.setNursingGrade(nursingGrade);
                syncPatientBean.setBedLabel(bedLabel);
                syncPatientBean.setAdmissionDiagName(admissionDiagName);
                syncPatientBean.setAdmissionDate(admissionDate);

                syncPatientBeans.add(syncPatientBean);
            }
           // database.close();
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
            database.close();
        }
        return syncPatientBeans;
    }

    @Override
    public void updatePatient(SQLiteDatabase database, List<SyncPatientBean> syncPatientBeans) {
        String updataSql = "delete* from patient";
        String insertSql = "insert into patient(patId,patCode,visitId,name,sex,age,wardCode,wardName,nursingGrade,bedLabel,admissionDate) values(?,?,?,?,?,?,?,?,?,?,?)";
        try {
            database.execSQL(updataSql);
            for (SyncPatientBean patientBean : syncPatientBeans) {
                database.execSQL(insertSql, new String[]{patientBean.getPatId(), patientBean.getPatCode(), patientBean.getVisitId(), patientBean.getName(), patientBean.getSex(),patientBean.getAge(),patientBean.getWardCode(),patientBean.getWardName(),patientBean.getNursingGrade(),patientBean.getBedLabel(),patientBean.getAdmissionDate()});
            }
           // database.close();
        } catch (SQLException e) {
            e.printStackTrace();
            database.close();
        }
    }
}
