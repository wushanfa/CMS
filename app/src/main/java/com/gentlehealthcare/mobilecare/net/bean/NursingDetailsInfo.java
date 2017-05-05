package com.gentlehealthcare.mobilecare.net.bean;

/**
 * 护理详情实体类
 */
public class NursingDetailsInfo {
    private String dosage;

    private String dosageUnits;
    private String orderNo;//组液
    private String orderId;
    private String orderStatus;//药品状态 @0-待执行；1-执行开始；9-执行完成；-1取消执行


    private String freqDetail;//频率详细
    private String templateId;//模版ID

    private String nursingDesc;//护理说明
    private String frequency;//频率
    private String performResult="";//执行结果
    private String orderText;//药品名称(医嘱名称)
    private  String templateName;//模版名称
    private String nursingType;//护理类型
    private String startTime;//开始时间
    private String orderCode;//药品编号(医嘱代码)
    private String planId;//计划ID
    private String isSolvent;//是否为溶剂 1-溶剂 0 溶质
    private String skinTest;//是否需要皮试 1-需要 0 不需要
    private String planTimeAttr;
    private String speed;
    private String speedUnits;//速度单位
    private String eventStartTime;
    private String injectionTool;//注射工具 0 留置针 1钢针
    private String nurseInOperate;//操作人
    private String inspectionTime;//巡视时间



    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getFreqDetail() {
        return freqDetail;
    }

    public void setFreqDetail(String freqDetail) {
        this.freqDetail = freqDetail;
    }





    public String getNursingDesc() {
        return nursingDesc;
    }

    public void setNursingDesc(String nursingDesc) {
        this.nursingDesc = nursingDesc;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getPerformResult() {
        return performResult;
    }

    public void setPerformResult(String performResult) {
        this.performResult = performResult;
    }

    public String getOrderText() {
        return orderText;
    }

    public void setOrderText(String orderText) {
        this.orderText = orderText;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getNursingType() {
        return nursingType;
    }

    public void setNursingType(String nursingType) {
        this.nursingType = nursingType;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }



    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getDosageUnits() {
        return dosageUnits;
    }

    public void setDosageUnits(String dosageUnits) {
        this.dosageUnits = dosageUnits;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }



    public String getIsSolvent() {
        return isSolvent;
    }

    public void setIsSolvent(String isSolvent) {
        this.isSolvent = isSolvent;
    }

    public String getSkinTest() {
        return skinTest;
    }

    public void setSkinTest(String skinTest) {
        this.skinTest = skinTest;
    }

    public String getPlanTimeAttr() {
        return planTimeAttr;
    }

    public void setPlanTimeAttr(String planTimeAttr) {
        this.planTimeAttr = planTimeAttr;
    }

    public String getSpeedUnits() {
        return speedUnits;
    }

    public void setSpeedUnits(String speedUnits) {
        this.speedUnits = speedUnits;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getEventStartTime() {
        return eventStartTime;
    }

    public void setEventStartTime(String eventStartTime) {
        this.eventStartTime = eventStartTime;
    }

    public String getInjectionTool() {
        return injectionTool;
    }

    public void setInjectionTool(String injectionTool) {
        this.injectionTool = injectionTool;
    }

    public String getNurseInOperate() {
        return nurseInOperate;
    }

    public void setNurseInOperate(String nurseInOperate) {
        this.nurseInOperate = nurseInOperate;
    }

    public String getInspectionTime() {
        return inspectionTime;
    }

    public void setInspectionTime(String inspectionTime) {
        this.inspectionTime = inspectionTime;
    }
}
