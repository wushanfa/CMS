package com.gentlehealthcare.mobilecare.net.bean;


import com.gentlehealthcare.mobilecare.UserInfo;
/**
 * 
 * @ClassName: SyncWillExecutionWorkBean 
 * @Description: 同步待执行工作 实体类
 * @author ouyang
 * @date 2015年3月10日 上午9:14:20 
 *
 */
public class SyncWillExecutionWorkBean {

    private String dosage;
    private String     dosageUnits;
    private String     orderNo;//药品编号(医嘱代码)
    private String      orderId;////药品ID(医嘱ID)
    private String      patId;//病人ID
    private String     patName;//病人名称
    private String    orderStatus;////药品状态 @0-待执行；1-执行开始；9-执行完成；-1取消执行
    private String     freqDetail;//频率详细
    private String     templateId;//模版ID
    private String     nursingDesc;//护理说明
    private String    frequency;//频率
    private String   performResult;//执行结果
    private String   orderText;//药品名称(医嘱名称)
    private String    templateNam;//模版名称
    private String    nursingType;//护理类型
    private String     startTime;
    private String  eventStartTime;//时间开始时间
    private String    orderCode;//药品编号(医嘱代码)
    private String planId;//计划ID
    private String name;//操作人

    private String isSolvent;//是否为溶剂 1-溶剂 0 溶质
    private String skin_test;//是否需要皮试 1-需要 0 不需要



    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getDosageUnits() {
        return dosageUnits;
    }

    public void setDosageUnits(String dosageUnits) {
        this.dosageUnits = dosageUnits;
    }

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

    public String getPatId() {
        return patId;
    }

    public void setPatId(String patId) {
        this.patId = patId;
    }

    public String getPatName() {
        return patName;
    }

    public void setPatName(String patName) {
        this.patName = patName;
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

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
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

    public String getTemplateNam() {
        return templateNam;
    }

    public void setTemplateNam(String templateNam) {
        this.templateNam = templateNam;
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

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    @Override
	public String toString() {
		// TODO Auto-generated method stub
		return "?username="+UserInfo.getUserName()+"&wardCode="+UserInfo.getWardCode();
	
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsSolvent() {
        return isSolvent;
    }

    public void setIsSolvent(String isSolvent) {
        this.isSolvent = isSolvent;
    }

    public String getSkin_test() {
        return skin_test;
    }

    public void setSkin_test(String skin_test) {
        this.skin_test = skin_test;
    }
}
