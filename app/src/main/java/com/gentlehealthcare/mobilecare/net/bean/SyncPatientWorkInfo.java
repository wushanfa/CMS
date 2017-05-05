package com.gentlehealthcare.mobilecare.net.bean;

/**
 * Created by ouyang on 15/9/1.
 */
public class SyncPatientWorkInfo {
    private String planOrderNo;
    private String time;
    private String templateId;
    private String performStatus;
    private String content;
    private String planTimeAttr;

    public String getPlanOrderNo() {
        return planOrderNo;
    }

    public void setPlanOrderNo(String planOrderNo) {
        this.planOrderNo = planOrderNo;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPlanTimeAttr() {
        return planTimeAttr;
    }

    public void setPlanTimeAttr(String planTimeAttr) {
        this.planTimeAttr = planTimeAttr;
    }
}
