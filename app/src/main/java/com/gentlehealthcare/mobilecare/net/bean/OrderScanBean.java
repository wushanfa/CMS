package com.gentlehealthcare.mobilecare.net.bean;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by zhiwei on 2015/11/25.
 */
public class OrderScanBean implements Serializable {

    private String patId;
    private String planStartTime;
    private String planOrderNo;
    private String patCode;
    private String relatedBarCode;

    private String performStatus;
    private String performResult;
    private String planId;
    private String eventStartTime;
    private String templateId;
    private boolean result;
    private String msg;

    @Override
    public String toString() {
        String planStartTime = "";
        String patId = "";
        String relatedBarCode = "";
        try {
            planStartTime = getPlanStartTime() == null ? "" : URLEncoder.encode(getPlanStartTime(), "utf-8");
            patId = getPatId() == null ? "" : URLEncoder.encode(getPatId(), "utf-8");
            relatedBarCode = getRelatedBarCode() == null ? "" : URLEncoder.encode(getRelatedBarCode(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "?planStartTime=" + planStartTime + "&relatedBarCode=" + relatedBarCode + "&patId=" + patId +
                "&patCode=" + patCode;
    }

    public String getRelatedBarCode() {
        return relatedBarCode;
    }

    public void setRelatedBarCode(String relatedBarCode) {
        this.relatedBarCode = relatedBarCode;
    }

    public String getPatCode() {
        return patCode;
    }

    public void setPatCode(String patCode) {
        this.patCode = patCode;
    }

    public String getPatId() {
        return patId;
    }

    public void setPatId(String patId) {
        this.patId = patId;
    }

    public String getPlanStartTime() {
        return planStartTime;
    }

    public void setPlanStartTime(String planStartTime) {
        this.planStartTime = planStartTime;
    }

    public String getPlanOrderNo() {
        return planOrderNo;
    }

    public void setPlanOrderNo(String planOrderNo) {
        this.planOrderNo = planOrderNo;
    }

    public String getPerformStatus() {
        return performStatus;
    }

    public void setPerformStatus(String performStatus) {
        this.performStatus = performStatus;
    }

    public String getPerformResult() {
        return performResult;
    }

    public void setPerformResult(String performResult) {
        this.performResult = performResult;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getEventStartTime() {
        return eventStartTime;
    }

    public void setEventStartTime(String eventStartTime) {
        this.eventStartTime = eventStartTime;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
