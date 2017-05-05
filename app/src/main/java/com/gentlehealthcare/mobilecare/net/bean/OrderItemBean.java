package com.gentlehealthcare.mobilecare.net.bean;

import java.io.Serializable;

/**
 * Created by ouyang on 15/8/27.
 */
public class OrderItemBean implements Serializable {

    private String dosage;
    private String performResult;
    private String dosageUnits;
    private String orderText;
    private String planStartTime;
    private String administration;
    private String speedUnits;
    private String planOrderNo;
    private String performStatus;//状态
    private String speed;//滴速
    private String frequency;
    private String injectionTool;//注射工具 0留置针 1钢针
    private String templateId;
    private String dosage2;//完成  实际的剂量
    private String eventEntTime;//完成  执行时间
    private String nurseInOperate;//完成 操作人
    private String planTimeAttr;//0 临时 1饭前 2饭后
    private String eventStartTime;
    private String relatedBarCode;
    private String eventEndTime;

    public String getRelatedBarCode() {
        return relatedBarCode;
    }

    public void setRelatedBarCode(String relatedBarCode) {
        this.relatedBarCode = relatedBarCode;
    }

    public String getEventEndTime() {
        return eventEndTime;
    }

    public void setEventEndTime(String eventEndTime) {
        this.eventEndTime = eventEndTime;
    }

    public String getEventStartTime() {
        return eventStartTime;
    }

    public void setEventStartTime(String eventStartTime) {
        this.eventStartTime = eventStartTime;
    }

    public String getFrequency() {
        return GetResult(frequency);
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getSpeed() {
        return GetResult(speed);
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getDosage() {
        return GetResult(dosage);
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getPerformResult() {
        return GetResult(performResult);
    }

    public void setPerformResult(String performResult) {
        this.performResult = performResult;
    }

    public String getDosageUnits() {
        return GetResult(dosageUnits);
    }

    public void setDosageUnits(String dosageUnits) {
        this.dosageUnits = dosageUnits;
    }

    public String getOrderText() {
        return GetResult(orderText);
    }

    public void setOrderText(String orderText) {
        this.orderText = orderText;
    }

    public String getPlanStartTime() {

        return GetResult(planStartTime);
    }

    public void setPlanStartTime(String planStartTime) {
        this.planStartTime = planStartTime;
    }

    public String getAdministration() {
        return GetResult(administration);
    }

    public void setAdministration(String administration) {
        this.administration = administration;
    }

    public String getSpeedUnits() {
        return GetResult(speedUnits);
    }

    public void setSpeedUnits(String speedUnits) {
        this.speedUnits = speedUnits;
    }

    public String getPlanOrderNo() {
        return GetResult(planOrderNo);
    }

    public void setPlanOrderNo(String planOrderNo) {
        this.planOrderNo = planOrderNo;
    }

    public String getPerformStatus() {
        return GetResult(performResult);
    }

    public void setPerformStatus(String performStatus) {
        this.performStatus = performStatus;
    }

    private String GetResult(String value) {
        return value == null ? "" : value;
    }

    public String getInjectionTool() {
        return injectionTool;
    }

    public void setInjectionTool(String injectionTool) {
        this.injectionTool = injectionTool;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getDosage2() {
        return dosage2;
    }

    public void setDosage2(String dosage2) {
        this.dosage2 = dosage2;
    }

    public String getEventEntTime() {
        return eventEntTime;
    }

    public void setEventEntTime(String eventEntTime) {
        this.eventEntTime = eventEntTime;
    }

    public String getNurseInOperate() {
        return nurseInOperate;
    }

    public void setNurseInOperate(String nurseInOperate) {
        this.nurseInOperate = nurseInOperate;
    }

    public String getPlanTimeAttr() {
        return planTimeAttr;
    }

    public void setPlanTimeAttr(String planTimeAttr) {
        this.planTimeAttr = planTimeAttr;
    }
}
