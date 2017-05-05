package com.gentlehealthcare.mobilecare.net.bean;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

public class SearchICUABean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * 发送请求
     */
    private String patId;
    private String recordingTime;
    /**
     * 返回请求
     */
    private String vsId;
    private String vitalSigns;
    private String vitalSignsValues;
    private String valueType;
    private String units;
    private String examMethod;
    private String memo;
    private String logBy;
    private String logTime;
    private String wardType;
    private String parmClass;
    private String patternId;

    @Override
    public String toString() {
        String patId = "";
        String recordingTime = "";
        try {
            recordingTime = getRecordingTime() == null ? "" : java.net.URLEncoder.encode
                    (getRecordingTime(), "utf-8");
            patId = getPatId() == null ? "" : getPatId();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "?recordingTime=" + recordingTime + "&patId=" + patId;
    }

    public String getPatId() {
        return patId;
    }

    public void setPatId(String patId) {
        this.patId = patId;
    }

    public String getRecordingTime() {
        return recordingTime;
    }

    public void setRecordingTime(String recordingTime) {
        this.recordingTime = recordingTime;
    }

    public String getVitalSigns() {
        return vitalSigns;
    }

    public void setVitalSigns(String vitalSigns) {
        this.vitalSigns = vitalSigns;
    }

    public String getVitalSignsValues() {
        return vitalSignsValues;
    }

    public void setVitalSignsValues(String vitalSignsValues) {
        this.vitalSignsValues = vitalSignsValues;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getExamMethod() {
        return examMethod;
    }

    public void setExamMethod(String examMethod) {
        this.examMethod = examMethod;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getLogBy() {
        return logBy;
    }

    public void setLogBy(String logBy) {
        this.logBy = logBy;
    }

    public String getLogTime() {
        return logTime;
    }

    public void setLogTime(String logTime) {
        this.logTime = logTime;
    }

    public String getWardType() {
        return wardType;
    }

    public void setWardType(String wardType) {
        this.wardType = wardType;
    }

    public String getParmClass() {
        return parmClass;
    }

    public void setParmClass(String parmClass) {
        this.parmClass = parmClass;
    }

    public String getPatternId() {
        return patternId;
    }

    public void setPatternId(String patternId) {
        this.patternId = patternId;
    }

    public String getVsId() {
        return vsId;
    }

    public void setVsId(String vsId) {
        this.vsId = vsId;
    }
}
