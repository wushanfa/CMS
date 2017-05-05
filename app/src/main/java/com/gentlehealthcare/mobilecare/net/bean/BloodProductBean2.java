package com.gentlehealthcare.mobilecare.net.bean;

import java.io.Serializable;

/**
 * @author Zyy
 * @date 2015-11-24上午15:02:36
 * @todo 血品Bean
 */
public class BloodProductBean2 implements Serializable {

    private String patCode;//患者代号
    private String bloodProductCode;//产品码
    private String itemNo;//
    private String patId;//患者代号
    private String bloodGroup;//血型
    private String transStartDate;
    private String transEndDate;
    private String applyNo;//申请单号
    private String bloodGroupCode;//血型码
    private String bloodStatus;//血袋状态:0 待用 ; 1-执行中；9-执行完
    private String bloodCapacity;//血量
    private String bloodId;//血袋内部流水号
    private String transDate;//
    private String unit;//单位
    private String bloodInvalCode;//失效日期码@条码
    private String bloodTypeName;//血液成份名称
    private String suspendAttr;//暂停属性@APP端专用 1-暂停 0-正常
    private String planOrderNo;//关联计划医嘱号
    private String bloodDonorCode;//献血码@条码，注：当成血袋号显示
    private String bloodCapacityCode;//血量码@条码
    private String bloodInvalDate;//失效日期
    private String planStartTime;
    private String bloodGroupDesc;
    private String memo;
    private String logId;
    private String patName;

    public String getBloodUnit() {
        return bloodUnit;
    }

    public void setBloodUnit(String bloodUnit) {
        this.bloodUnit = bloodUnit;
    }

    private String bloodUnit;

    public String getBloodOutDate() {
        return bloodOutDate;
    }

    public void setBloodOutDate(String bloodOutDate) {
        this.bloodOutDate = bloodOutDate;
    }

    public String getPatName() {
        return patName;
    }

    public void setPatName(String patName) {
        this.patName = patName;
    }

    private String bloodOutDate;

    public String getBloodGroupDesc() {
        return bloodGroupDesc;
    }

    public void setBloodGroupDesc(String bloodGroupDesc) {
        this.bloodGroupDesc = bloodGroupDesc;
    }

    public String getPatCode() {
        return patCode;
    }

    public void setPatCode(String patCode) {
        this.patCode = patCode;
    }

    public String getBloodProductCode() {
        return bloodProductCode;
    }

    public void setBloodProductCode(String bloodProductCode) {
        this.bloodProductCode = bloodProductCode;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getPatId() {
        return patId;
    }

    public void setPatId(String patId) {
        this.patId = patId;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getTransStartDate() {
        return transStartDate;
    }

    public void setTransStartDate(String transStartDate) {
        this.transStartDate = transStartDate;
    }

    public String getTransEndDate() {
        return transEndDate;
    }

    public void setTransEndDate(String transEndDate) {
        this.transEndDate = transEndDate;
    }

    public String getApplyNo() {
        return applyNo;
    }

    public void setApplyNo(String applyNo) {
        this.applyNo = applyNo;
    }

    public String getBloodGroupCode() {
        return bloodGroupCode;
    }

    public void setBloodGroupCode(String bloodGroupCode) {
        this.bloodGroupCode = bloodGroupCode;
    }

    public String getBloodStatus() {
        return bloodStatus;
    }

    public void setBloodStatus(String bloodStatus) {
        this.bloodStatus = bloodStatus;
    }

    public String getBloodCapacity() {
        return bloodCapacity;
    }

    public void setBloodCapacity(String bloodCapacity) {
        this.bloodCapacity = bloodCapacity;
    }

    public String getBloodId() {
        return bloodId;
    }

    public void setBloodId(String bloodId) {
        this.bloodId = bloodId;
    }

    public String getTransDate() {
        return transDate;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getBloodInvalCode() {
        return bloodInvalCode;
    }

    public void setBloodInvalCode(String bloodInvalCode) {
        this.bloodInvalCode = bloodInvalCode;
    }

    public String getBloodTypeName() {
        return bloodTypeName;
    }

    public void setBloodTypeName(String bloodTypeName) {
        this.bloodTypeName = bloodTypeName;
    }

    public String getSuspendAttr() {
        return suspendAttr;
    }

    public void setSuspendAttr(String suspendAttr) {
        this.suspendAttr = suspendAttr;
    }

    public String getPlanOrderNo() {
        return planOrderNo;
    }

    public void setPlanOrderNo(String planOrderNo) {
        this.planOrderNo = planOrderNo;
    }

    public String getBloodDonorCode() {
        return bloodDonorCode;
    }

    public void setBloodDonorCode(String bloodDonorCode) {
        this.bloodDonorCode = bloodDonorCode;
    }

    public String getBloodCapacityCode() {
        return bloodCapacityCode;
    }

    public void setBloodCapacityCode(String bloodCapacityCode) {
        this.bloodCapacityCode = bloodCapacityCode;
    }

    public String getBloodInvalDate() {
        return bloodInvalDate;
    }

    public void setBloodInvalDate(String bloodInvalDate) {
        this.bloodInvalDate = bloodInvalDate;
    }

    public String getPlanStartTime() {
        return planStartTime;
    }

    public void setPlanStartTime(String planStartTime) {
        this.planStartTime = planStartTime;
    }
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getLogId() {
        return logId;
    }
    public void setLogId(String logId) {
        this.logId = logId;
    }
}
