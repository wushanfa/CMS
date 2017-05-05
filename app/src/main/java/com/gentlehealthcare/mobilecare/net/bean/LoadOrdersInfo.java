package com.gentlehealthcare.mobilecare.net.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 加载医嘱子项 Created by ouyang on 15/6/2.
 */
public class LoadOrdersInfo implements Serializable {
    private String speed;// 滴速
    private String performStatus;// 执行状态
    private String dosage;// 计量
    private String frequency;// 频次
    private String planId;// 计划ID
    private String orderText;// 医嘱文本
    private String nurseEffect;
    private String varianceCause;
    private String varianceCauseDesc;
    private String eventEndTime;
    private String eventStartTime;
    private String skinTest;
    private String isSolvent;
    private String orderId;
    private String skinTestResult;
    private String nurseInOperate;
    private String injectionTool;
    private String templateId;
    private String repeatIndicator;
    private String orderClass;
    private String administration;
    private String relatedBarcode;

    private String siteDesc;

    private String siteCode;
    private String siteNo;
    private String itemNo;
    private List<BloodProductBean2> bags;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getAdministration() {
        return administration;
    }

    public void setAdministration(String administration) {
        this.administration = administration;
    }

    public String getOrderClass() {
        return orderClass;
    }

    public void setOrderClass(String orderClass) {
        this.orderClass = orderClass;
    }

    public String getRepeatIndicator() {
        return repeatIndicator;
    }

    public void setRepeatIndicator(String repeatIndicator) {
        this.repeatIndicator = repeatIndicator;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getSiteNo() {
        return siteNo;
    }

    public void setSiteNo(String siteNo) {
        this.siteNo = siteNo;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getPerformStatus() {
        return performStatus;
    }

    public void setPerformStatus(String performStatus) {
        this.performStatus = performStatus;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getOrderText() {
        return orderText;
    }

    public void setOrderText(String orderText) {
        this.orderText = orderText;
    }

    public String getNurseEffect() {
        return nurseEffect;
    }

    public void setNurseEffect(String nurseEffect) {
        this.nurseEffect = nurseEffect;
    }

    public String getVarianceCause() {
        return varianceCause;
    }

    public void setVarianceCause(String varianceCause) {
        this.varianceCause = varianceCause;
    }

    public String getVarianceCauseDesc() {
        return varianceCauseDesc;
    }

    public void setVarianceCauseDesc(String varianceCauseDesc) {
        this.varianceCauseDesc = varianceCauseDesc;
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

    public String getSkinTest() {
        return skinTest;
    }

    public void setSkinTest(String skinTest) {
        this.skinTest = skinTest;
    }

    public String getIsSolvent() {
        return isSolvent;
    }

    public void setIsSolvent(String isSolvent) {
        this.isSolvent = isSolvent;
    }

    public String getSkinTestResult() {
        return skinTestResult;
    }

    public void setSkinTestResult(String skinTestResult) {
        this.skinTestResult = skinTestResult;
    }

    public String getNurseInOperate() {
        return nurseInOperate;
    }

    public void setNurseInOperate(String nurseInOperate) {
        this.nurseInOperate = nurseInOperate;
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

    public List<BloodProductBean2> getBags() {
        return bags;
    }

    public void setBags(List<BloodProductBean2> bags) {
        this.bags = bags;
    }

    public String getSiteDesc() {
        return siteDesc;
    }

    public void setSiteDesc(String siteDesc) {
        this.siteDesc = siteDesc;
    }

    public String getRelatedBarcode() {
        return relatedBarcode;
    }

    public void setRelatedBarcode(String relatedBarcode) {
        this.relatedBarcode = relatedBarcode;
    }
}
