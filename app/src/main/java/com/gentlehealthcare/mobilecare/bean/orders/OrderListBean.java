package com.gentlehealthcare.mobilecare.bean.orders;

import com.gentlehealthcare.mobilecare.net.bean.BloodProductBean2;

import java.io.Serializable;
import java.util.List;

/**
 *
 */
public class OrderListBean implements Serializable {
    /**
     * patId
     * wardCode
     * <p>
     * startTime : 2016-05-11 16:00:00
     * templateId : null
     * relatedBarcode : 73319||230||3
     * performStatus : 0
     * eventEndTime : null
     * dosage : 20ML||30mg||5mg||4000u
     * planOrderNo : 72687_160403160000_230
     * eventStartTime : null
     * orderClass : 西药
     * frequency : Bid
     * orderText : 0.9%氯化钠注射液[10ml:90mg/支](济南)||注射用盐酸氨溴索[30mg/支]||地塞米松磷酸钠注射液[5mg:1ml*10支]||注射用糜蛋白酶[4000U*2]
     * administration : 雾化吸入
     */

    private String startTime;
    private Object templateId;
    private String relatedBarcode;
    private String relatedBarcode2;
    private String performStatus;
    private Object eventEndTime;
    private String dosage;
    private String planOrderNo;
    private Object eventStartTime;
    private String orderClass;
    private String frequency;
    private String orderText;
    private String administration;
    private String orderId;
    private String nurseInOperate;
    private String planId;
    private String injectionTool;
    private String patId;
    private String skinTestResult;
    private String skinTest;
    private String scanMode;
    private String wardCode;
    private String scanMode2;
    private String startDateTime;
    private String stopDateTime;
    private String orderedDoctor;
	private String stopDoctor;
	private String stopAttr;
	private String stopAttr2;
	private String repeatIndicator;

    public String getCheckBy2() {
        return checkBy2;
    }

    public void setCheckBy2(String checkBy2) {
        this.checkBy2 = checkBy2;
    }

    public String getPrintBy() {
        return printBy;
    }

    public void setPrintBy(String printBy) {
        this.printBy = printBy;
    }

    private String printBy;
	private String checkBy2;
    private boolean isSelect=false;
    private boolean batch = false;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getRepeatIndicator() {
        return repeatIndicator;
    }

    public void setRepeatIndicator(String repeatIndicator) {
        this.repeatIndicator = repeatIndicator;
    }

    public String getStopAttr2() {
		return stopAttr2;
	}

	public void setStopAttr2(String stopAttr2) {
		this.stopAttr2 = stopAttr2;
	}
    
    public String getStopAttr() {
		return stopAttr;
	}

	public void setStopAttr(String stopAttr) {
		this.stopAttr = stopAttr;
	}

	public String getStopDoctor() {
		return stopDoctor;
	}

	public void setStopDoctor(String stopDoctor) {
		this.stopDoctor = stopDoctor;
	}
	
    public String getWardCode() {
        return wardCode;
    }

    public void setWardCode(String wardCode) {
        this.wardCode = wardCode;
    }

    public String getScanMode() {
        return scanMode;
    }

    public void setScanMode(String scanMode) {
        this.scanMode = scanMode;
    }

    public String getInspectionTime() {
        return inspectionTime;
    }

    public void setInspectionTime(String inspectionTime) {
        this.inspectionTime = inspectionTime;
    }

    private String inspectionTime;

    public String getPlanBatchNo() {
        return planBatchNo;
    }

    public void setPlanBatchNo(String planBatchNo) {
        this.planBatchNo = planBatchNo;
    }

    private String planBatchNo;


    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }

    public String getSkinTest() {
        return skinTest;
    }

    public void setSkinTest(String skinTest) {
        this.skinTest = skinTest;
    }

    private String templateType;


    public boolean isBatch() {
        return batch;
    }

    public void setBatch(boolean batch) {
        this.batch = batch;
    }

    public String getSkinTestResult() {
        return skinTestResult;
    }

    public void setSkinTestResult(String skinTestResult) {
        this.skinTestResult = skinTestResult;
    }

    public String getDosageActual() {
        return dosageActual;
    }

    public void setDosageActual(String dosageActual) {
        this.dosageActual = dosageActual;
    }

    private String dosageActual;

    public String getNursingDesc() {
        return nursingDesc;
    }

    public void setNursingDesc(String nursingDesc) {
        this.nursingDesc = nursingDesc;
    }

    private String nursingDesc;


    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    private String reqId;

    public List<BloodProductBean2> getBags() {
        return bags;
    }

    public void setBags(List<BloodProductBean2> bags) {
        this.bags = bags;
    }

    private List<BloodProductBean2> bags = null;

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

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    private String speed;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public Object getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Object templateId) {
        this.templateId = templateId;
    }

    public String getRelatedBarcode() {
        return relatedBarcode;
    }

    public void setRelatedBarcode(String relatedBarcode) {
        this.relatedBarcode = relatedBarcode;
    }

    public String getPerformStatus() {
        return performStatus;
    }

    public void setPerformStatus(String performStatus) {
        this.performStatus = performStatus;
    }

    public Object getEventEndTime() {
        return eventEndTime;
    }

    public void setEventEndTime(Object eventEndTime) {
        this.eventEndTime = eventEndTime;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getPlanOrderNo() {
        return planOrderNo;
    }

    public void setPlanOrderNo(String planOrderNo) {
        this.planOrderNo = planOrderNo;
    }

    public Object getEventStartTime() {
        return eventStartTime;
    }

    public void setEventStartTime(Object eventStartTime) {
        this.eventStartTime = eventStartTime;
    }

    public String getOrderClass() {
        return orderClass;
    }

    public void setOrderClass(String orderClass) {
        this.orderClass = orderClass;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getOrderText() {
        return orderText;
    }

    public void setOrderText(String orderText) {
        this.orderText = orderText;
    }

    public String getAdministration() {
        return administration;
    }

    public void setAdministration(String administration) {
        this.administration = administration;
    }

    public String getOrderedDoctor() {
        return orderedDoctor;
    }

    public void setOrderedDoctor(String orderedDoctor) {
        this.orderedDoctor = orderedDoctor;
    }

    public String getScanMode2() {
        return scanMode2;
    }

    public void setScanMode2(String scanMode2) {
        this.scanMode2 = scanMode2;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getStopDateTime() {
        return stopDateTime;
    }

    public void setStopDateTime(String stopDateTime) {
        this.stopDateTime = stopDateTime;
    }


    public String getRelatedBarcode2() {
        return relatedBarcode2;
    }

    public void setRelatedBarcode2(String relatedBarcode2) {
        this.relatedBarcode2 = relatedBarcode2;
    }
}
