package com.gentlehealthcare.mobilecare.db.table;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * 巡视报警表
 * Created by ouyang on 2015/3/19.
 */
@DatabaseTable
public class TB_VisitsAlert implements Serializable{
    @DatabaseField(generatedId = true)
    private int visitasAlertId;
    @DatabaseField
    private String patientId;//病人ID
    @DatabaseField
    private String injectionTime;//注射时间
    @DatabaseField
    private String visitsTime;//巡视时间
   @DatabaseField
    private boolean ignore;//是否忽略
    @DatabaseField
    private String medicineCode;
    @DatabaseField
    private String planId;
    @DatabaseField
    private String alarmTime;//提醒
    @DatabaseField
    private String state;//0 待执行 1执行中

    public int getVisitasAlertId() {
        return visitasAlertId;
    }

    public void setVisitasAlertId(int visitasAlertId) {
        this.visitasAlertId = visitasAlertId;
    }

    public boolean isIgnore() {
        return ignore;
    }

    public void setIgnore(boolean ignore) {
        this.ignore = ignore;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getInjectionTime() {
        return injectionTime;
    }

    public void setInjectionTime(String injectionTime) {

        this.injectionTime = injectionTime;
    }

    public String getVisitsTime() {
        return visitsTime;
    }

    public void setVisitsTime(String visitsTime) {
//        Long visitTime=Long.valueOf(visitsTime);
//        Long time=System.currentTimeMillis();
//        long space=(visitTime-time)/(60*1000);
//        if (space>5)
//            this.alarmTime=String.valueOf(visitTime-SettingsConfigBean.FIRST_VISIT_SPACE);
//        else
//          this.alarmTime=visitsTime;
        this.visitsTime = visitsTime;
    }

    public String getMedicineCode() {
        return medicineCode;
    }

    public void setMedicineCode(String medicineCode) {
        this.medicineCode = medicineCode;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }


    public String getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime) {

        this.alarmTime = alarmTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
