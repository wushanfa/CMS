package com.gentlehealthcare.mobilecare.bean.transfusion;

/**
 * Created by zhiwei on 2016/5/23.
 */
public class TransBloodBean {

    /**
     * patCode : 0000114374
     * bloodProductCode : =<E5445000
     * itemNo : 1
     * bloodGroup : A型/阳性(+)
     * patId : 72687
     * applyNo : 73319||146
     * bloodGroupCode : =%6200
     * bloodStatus : 0
     * bloodCapacity : 2单位
     * transDate : 2016-05-22 09:00:00
     * bloodInvalCode : 201604220950
     * bloodTypeName : 备去白细胞悬浮红细胞[A型Rh阳性(+)]
     * planOrderNo : 160522080000_146
     * bloodDonorCode : =410301602203855
     * bloodInvalDate : 1461289800000
     */

    private String patCode;
    private String bloodProductCode;
    private int itemNo;
    private String bloodGroup;
    private String patId;
    private String applyNo;
    private String bloodGroupCode;
    private String bloodStatus;
    private String bloodCapacity;
    private String transDate;
    private String bloodInvalCode;
    private String bloodTypeName;
    private String planOrderNo;
    private String bloodDonorCode;
    private long bloodInvalDate;

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

    public int getItemNo() {
        return itemNo;
    }

    public void setItemNo(int itemNo) {
        this.itemNo = itemNo;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getPatId() {
        return patId;
    }

    public void setPatId(String patId) {
        this.patId = patId;
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

    public String getTransDate() {
        return transDate;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
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

    public long getBloodInvalDate() {
        return bloodInvalDate;
    }

    public void setBloodInvalDate(long bloodInvalDate) {
        this.bloodInvalDate = bloodInvalDate;
    }
}
