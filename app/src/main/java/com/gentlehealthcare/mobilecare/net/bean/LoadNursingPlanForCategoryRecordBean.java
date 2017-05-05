package com.gentlehealthcare.mobilecare.net.bean;

import com.gentlehealthcare.mobilecare.bean.PlanTimeCountBean;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by ouyang on 15/9/2.
 */
public class LoadNursingPlanForCategoryRecordBean implements Serializable{
    private String templateId;
    private String performStatus;
    private Map<String, PlanTimeCountBean> patient;
    private boolean result;
    private String  msg;
    private String patientName;
    public String getPerformStatus() {
        return performStatus;
    }

    public void setPerformStatus(String performStatus) {
        this.performStatus = performStatus;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    @Override
    public String toString() {
        return "?templateId="+getTemplateId()+"&performStatus="+getPerformStatus();
    }



    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public Map<String, PlanTimeCountBean> getPatient() {
        return patient;
    }

    public void setPatient(Map<String, PlanTimeCountBean> patient) {
        this.patient = patient;
    }
}

