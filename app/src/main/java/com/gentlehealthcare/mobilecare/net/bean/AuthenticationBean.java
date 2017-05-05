package com.gentlehealthcare.mobilecare.net.bean;

import com.gentlehealthcare.mobilecare.UserInfo;

/**
 * 核对患者身份
 * Created by ouyang on 2015/3/17.
 */
public class AuthenticationBean {
    private String patId;//病人ID
    private String eventAttr;//1:病人身份核对成功 -1:病人身份核对失败
    private String templateId;//模版ID
    private String patCode;//病人编号
    private String eventType;
    private String performType;
    private String performDesc;
    private String msg;
    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    private String eventId;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    private boolean result;


    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getPerformType() {
        return performType;
    }

    public void setPerformType(String performType) {
        this.performType = performType;
    }

    public String getPerformDesc() {
        return performDesc;
    }

    public void setPerformDesc(String performDesc) {
        this.performDesc = performDesc;
    }


    @Override
    public String toString() {
        return "?username="+ UserInfo.getUserName()+"&patId="+getPatId()+"&patCode="+getPatCode()+"&eventAttr="+getEventAttr()+"&templateId="+getTemplateId();
    }

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

    public String getEventAttr() {
        return eventAttr;
    }

    public void setEventAttr(String eventAttr) {
        this.eventAttr = eventAttr;
    }

    public String getPatCode() {
        return patCode;
    }

    public void setPatCode(String patCode) {
        this.patCode = patCode;
    }
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
