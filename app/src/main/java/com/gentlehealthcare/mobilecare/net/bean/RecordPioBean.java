package com.gentlehealthcare.mobilecare.net.bean;

import java.io.UnsupportedEncodingException;

import com.gentlehealthcare.mobilecare.UserInfo;

/**
 * 记录PIO实体类 Created by ouyang on 15/7/3.
 */
public class RecordPioBean {
    private String patId;
    private String problemCode;// 问题编号
    private String problemName;// 问题
    private String causeCode;// 致因号
    private String causeName;// 致因
    private String targetCode;// 目标号
    private String targetName;// 目标
    private String measureCode;// 措施号
    private String measureName;// 措施
    private String result;
    private String logBy;
    private String causesNo;
    private String targetNo;
    private String measureNo;
    private String pioId;
    private String logTime;

    @Override
    public String toString() {
        String problemName = "";
        String causeName = "";
        String targetName = "";
        String measureName = "";
        String causesCode = "";
        String targetCode = "";
        String measureCode = "";
        String causesNo = "";
        String targetNo = "";
        String measureNo = "";
        String pioId = "";
        String logTime = "";

        try {
            problemName = getProblemName() == null ? "" : java.net.URLEncoder.encode(getProblemName(), "utf-8");
            causeName = getCauseName() == null ? "" : java.net.URLEncoder.encode(getCauseName(), "utf-8");
            targetName = getTargetName() == null ? "" : java.net.URLEncoder.encode(getTargetName(), "utf-8");
            measureName = getMeasureName() == null ? "" : java.net.URLEncoder.encode(getMeasureName(), "utf-8");
            causesCode = getCauseCode() == null ? "" : java.net.URLEncoder.encode(getCauseCode(), "utf-8");
            targetCode = getTargetCode() == null ? "" : java.net.URLEncoder.encode(getTargetCode(), "utf-8");
            measureCode = getMeasureCode() == null ? "" : java.net.URLEncoder.encode(getMeasureCode(), "utf-8");
            pioId = getPioId() == null ? "thisIsNull" : java.net.URLEncoder.encode(getPioId(), "utf-8");
            logTime = getLogTime() == null ? "" : java.net.URLEncoder.encode(getLogTime(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "?username=" + UserInfo.getUserName() + "&patId=" + getPatId() + "&problemCode=" + getProblemCode
                () + "&problemName=" + problemName + "&causesName=" + causeName + "&targetName=" + targetName +
                "&measureName=" + measureName + "&logBy=" + getLogBy() + "&causesCode=" + causesCode +
                "&targetCode=" + targetCode + "&measureCode=" + measureCode + "&pioId=" + pioId + "&logTime=" +
                logTime;
    }

    public String getLogTime() {
        return logTime;
    }

    public void setLogTime(String logTime) {
        this.logTime = logTime;
    }

    public String getMeasureName() {
        return measureName;
    }

    public void setMeasureName(String measureName) {
        this.measureName = measureName;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getCauseName() {
        return causeName;
    }

    public void setCauseName(String causeName) {
        this.causeName = causeName;
    }

    public String getProblemName() {
        return problemName;
    }

    public void setProblemName(String problemName) {
        this.problemName = problemName;
    }

    public String getProblemCode() {
        return problemCode;
    }

    public void setProblemCode(String problemCode) {
        this.problemCode = problemCode;
    }

    public String getCauseCode() {
        return causeCode;
    }

    public void setCauseCode(String causeCode) {
        this.causeCode = causeCode;
    }

    public String getTargetCode() {
        return targetCode;
    }

    public void setTargetCode(String targetCode) {
        this.targetCode = targetCode;
    }

    public String getMeasureCode() {
        return measureCode;
    }

    public void setMeasureCode(String measureCode) {
        this.measureCode = measureCode;
    }

    public String getPatId() {
        return patId;
    }

    public void setPatId(String patId) {
        this.patId = patId;
    }


    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getLogBy() {
        return logBy;
    }

    public void setLogBy(String logBy) {
        this.logBy = logBy;
    }

    public String getCausesNo() {
        return causesNo;
    }

    public void setCausesNo(String causesNo) {
        this.causesNo = causesNo;
    }

    public String getTargetNo() {
        return targetNo;
    }

    public void setTargetNo(String targetNo) {
        this.targetNo = targetNo;
    }

    public String getMeasureNo() {
        return measureNo;
    }

    public void setMeasureNo(String measureNo) {
        this.measureNo = measureNo;
    }

    public String getPioId() {
        return pioId;
    }

    public void setPioId(String pioId) {
        this.pioId = pioId;
    }

}
