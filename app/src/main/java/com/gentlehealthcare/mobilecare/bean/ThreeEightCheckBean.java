package com.gentlehealthcare.mobilecare.bean;

import java.io.Serializable;

/**
 * @author Zyy
 * @date 2015-9-23上午10:14:09
 * @category 三查8对
 */
public class ThreeEightCheckBean implements Serializable {

    /**
     * 3查8对序列化
     */
    private static final long serialVersionUID = 1L;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPatId() {
        return patId;
    }

    public void setPatId(String patId) {
        this.patId = patId;
    }

    public String getPlanOrderNo() {
        return planOrderNo;
    }

    public void setPlanOrderNo(String planOrderNo) {
        this.planOrderNo = planOrderNo;
    }

    public String getEffective() {
        return effective;
    }

    public void setEffective(String effective) {
        this.effective = effective;
    }

    public String getEffectiveCheckTime() {
        return effectiveCheckTime;
    }

    public void setEffectiveCheckTime(String effectiveCheckTime) {
        this.effectiveCheckTime = effectiveCheckTime;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getQualityCheckTime() {
        return qualityCheckTime;
    }

    public void setQualityCheckTime(String qualityCheckTime) {
        this.qualityCheckTime = qualityCheckTime;
    }

    public String getPacking() {
        return packing;
    }

    public void setPacking(String packing) {
        this.packing = packing;
    }

    public String getPackingCheckTime() {
        return packingCheckTime;
    }

    public void setPackingCheckTime(String packingCheckTime) {
        this.packingCheckTime = packingCheckTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameCheckTime() {
        return nameCheckTime;
    }

    public void setNameCheckTime(String nameCheckTime) {
        this.nameCheckTime = nameCheckTime;
    }

    public String getBedLabel() {
        return bedLabel;
    }

    public void setBedLabel(String bedLabel) {
        this.bedLabel = bedLabel;
    }

    public String getBedLabelCheckTime() {
        return bedLabelCheckTime;
    }

    public void setBedLabelCheckTime(String bedLabelCheckTime) {
        this.bedLabelCheckTime = bedLabelCheckTime;
    }

    public String getPatCode() {
        return patCode;
    }

    public void setPatCode(String patCode) {
        this.patCode = patCode;
    }

    public String getPatCodeCheckTime() {
        return patCodeCheckTime;
    }

    public void setPatCodeCheckTime(String patCodeCheckTime) {
        this.patCodeCheckTime = patCodeCheckTime;
    }

    public String getBloodId() {
        return bloodId;
    }

    public void setBloodId(String bloodId) {
        this.bloodId = bloodId;
    }

    public String getBloodIdCheckTime() {
        return bloodIdCheckTime;
    }

    public void setBloodIdCheckTime(String bloodIdCheckTime) {
        this.bloodIdCheckTime = bloodIdCheckTime;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getBloodGroupCheckTime() {
        return bloodGroupCheckTime;
    }

    public void setBloodGroupCheckTime(String bloodGroupCheckTime) {
        this.bloodGroupCheckTime = bloodGroupCheckTime;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getBloodTypeCheckTime() {
        return bloodTypeCheckTime;
    }

    public void setBloodTypeCheckTime(String bloodTypeCheckTime) {
        this.bloodTypeCheckTime = bloodTypeCheckTime;
    }

    public String getMatchResult() {
        return matchResult;
    }

    public void setMatchResult(String matchResult) {
        this.matchResult = matchResult;
    }

    public String getMatchResultCheckTime() {
        return matchResultCheckTime;
    }

    public void setMatchResultCheckTime(String matchResultCheckTime) {
        this.matchResultCheckTime = matchResultCheckTime;
    }

    public String getBloodCapacity() {
        return bloodCapacity;
    }

    public void setBloodCapacity(String bloodCapacity) {
        this.bloodCapacity = bloodCapacity;
    }

    public String getBloodCapacityCheckTime() {
        return bloodCapacityCheckTime;
    }

    public void setBloodCapacityCheckTime(String bloodCapacityCheckTime) {
        this.bloodCapacityCheckTime = bloodCapacityCheckTime;
    }

    public String getHandling() {
        return handling;
    }

    public void setHandling(String handling) {
        this.handling = handling;
    }

    @Override
    public String toString() {
        String result = "?username=" + getUsername() + "&patId=" + getPatId()
                + "&planOrderNo=" + getPlanOrderNo() + "&effective="
                + getEffective() + "&effectiveCheckTime="
                + spaceToT(getEffectiveCheckTime()) + "&quality=" + getQuality()
                + "&qualityCheckTime=" + spaceToT(getQualityCheckTime()) + "&packing="
                + getPacking() + "&packingCheckTime=" + spaceToT(getPackingCheckTime())
                + "&name=" + getName() + "&nameCheckTime=" + spaceToT(getNameCheckTime())
                + "&bedLabel=" + getBedLabel() + "&bedLabelCheckTime="
                + spaceToT(getBedLabelCheckTime()) + "&patCode=" + getPatCode()
                + "&patCodeCheckTime=" + spaceToT(getPatCodeCheckTime()) + "&bloodId="
                + getBloodId() + "&bloodIdCheckTime=" + spaceToT(getBloodIdCheckTime())
                + "&bloodGroup=" + getBloodGroup() + "&bloodGroupCheckTime="
                + spaceToT(getBloodGroupCheckTime()) + "&bloodType=" + getBloodType()
                + "&bloodTypeCheckTime=" + spaceToT(getBloodTypeCheckTime())
                + "&matchResult=" + getMatchResult() + "&matchResultCheckTime="
                + spaceToT(getMatchResultCheckTime()) + "&bloodCapacity="
                + getBloodCapacity() + "&bloodCapacityCheckTime="
                + spaceToT(getBloodCapacityCheckTime()) + "&handling=" + getHandling()+"&bloodStatus="+getBloodStatus()+"&logId="+getLogId();

        return result;
    }

    /**
     * 空格替换成"T"
     *
     * @param str
     * @return
     */
    private String spaceToT(String str) {
        if (str != null && str.length() > 0 && !str.equals("null")) {
            return str.replace(" ", "T");
        } else {
            return str;
        }
    }

    public String username;// 用户名
    public String patId;// 病人id
    public String planOrderNo;// 医嘱执行任务id
    public String effective;// 有效期
    public String effectiveCheckTime;// 有效期核对时间
    public String quality;// 质量
    public String qualityCheckTime;// 质量核对时间
    public String packing;// 包装
    public String packingCheckTime;// 包装核对时间
    public String name;// 姓名
    public String nameCheckTime;// 姓名核对时间
    public String bedLabel;// 床号
    public String bedLabelCheckTime;// 床号核对时间
    public String patCode;// 病历号
    public String patCodeCheckTime;// 病历号核对时间
    public String bloodId;// 血袋号
    public String bloodIdCheckTime;// 血袋号核对时间
    public String bloodGroup;// 血型
    public String bloodGroupCheckTime;// 血型核对时间
    public String bloodType;// 血品
    public String bloodTypeCheckTime;// 血品核对时间
    public String matchResult;// 配血结果
    public String matchResultCheckTime;// 配血结果核对时间
    public String bloodCapacity;// 血量
    public String bloodCapacityCheckTime;// 血量核对时间
    public String handling;// 处理结果

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String logId;//血袋主键

    public String getBloodStatus() {
        return bloodStatus;
    }

    public void setBloodStatus(String bloodStatus) {
        this.bloodStatus = bloodStatus;
    }

    public String bloodStatus;// 血袋状态


}
