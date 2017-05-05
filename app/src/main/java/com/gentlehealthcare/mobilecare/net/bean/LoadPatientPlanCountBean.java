package com.gentlehealthcare.mobilecare.net.bean;

/**
 * 加载病人计划时间值
 * Created by ouyang on 15/8/26.
 */
public class LoadPatientPlanCountBean {
    private String patId;//病人ID
    private String templateId;//模块ID
    private String performStatus;//状态 待执行0 执行中1 完成9
    private String planStartDate;//日期

    public String getPatId() {
        return patId;
    }

    public void setPatId(String patId) {
        this.patId = patId;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getPerformStatus() {
        return performStatus;
    }

    public void setPerformStatus(String performStatus) {
        this.performStatus = performStatus;
    }

    public String getPlanStartDate() {
        return planStartDate;
    }

    public void setPlanStartDate(String planStartDate) {
        this.planStartDate = planStartDate;
    }

    @Override
    public String toString() {
        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append("?");
        if (getTemplateId()!=null){
            stringBuffer.append("templateId="+ getTemplateId());
        }
        if (getPerformStatus()!=null){
            if (stringBuffer.length()!=1)
                stringBuffer.append("&");
            stringBuffer.append("performStatus="+getPerformStatus());
        }

        if (getPlanStartDate()!=null){
            if (stringBuffer.length()!=1)
                stringBuffer.append("&");
            stringBuffer.append("planStartDate="+getPlanStartDate());
        }


        return stringBuffer.toString()+"&patId="+getPatId();
    }
}
