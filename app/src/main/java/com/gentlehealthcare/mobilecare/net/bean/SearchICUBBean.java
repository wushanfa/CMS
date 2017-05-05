package com.gentlehealthcare.mobilecare.net.bean;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class SearchICUBBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * 发送请求
     */
    private String patId;
    private String formNo;
    private String recordingTime;
    private String wardType;
    /**
     * 返回请求
     */
    //private String formNo;
    private String formId;
    private String itemNo;
    private String columnCode;
    private String classCode;
    private String valueCode;
    private String valueName;
    private String valueType;
    private String abnormalAttr;
    private String logBy;
    private String logTime;
    private String memo;
    private String valueDesc;
    private String columnName;
    private int rowNo;

    @Override
    public String toString() {
        String patId = "";
        String formNo = "";
        String recordingTime = "";
        String wardType = "";
        try {
            recordingTime = getRecordingTime() == null ? "" : URLEncoder.encode(getRecordingTime(), "utf-8");
            patId = getPatId() == null ? "" : URLEncoder.encode(getPatId(), "utf-8");
            formNo = getFormNo() == null ? "" : URLEncoder.encode(getFormNo(), "utf-8");
            wardType = getWardType() == null ? "nothing" : URLEncoder.encode(getWardType(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "?recordingTime=" + recordingTime + "&patId=" + patId + "&formNo=" + formNo + "&wardType=" + wardType;
    }

    public int getRowNo() {
        return rowNo;
    }

    public void setRowNo(int rowNo) {
        this.rowNo = rowNo;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getPatId() {
        return patId;
    }

    public void setPatId(String patId) {
        this.patId = patId;
    }

    public String getFormNo() {
        return formNo;
    }

    public void setFormNo(String formNo) {
        this.formNo = formNo;
    }

    public String getRecordingTime() {
        return recordingTime;
    }

    public void setRecordingTime(String recordingTime) {
        this.recordingTime = recordingTime;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getColumnCode() {
        return columnCode;
    }

    public void setColumnCode(String columnCode) {
        this.columnCode = columnCode;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getValueCode() {
        return valueCode;
    }

    public void setValueCode(String valueCode) {
        this.valueCode = valueCode;
    }

    public String getValueName() {
        return valueName;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public String getAbnormalAttr() {
        return abnormalAttr;
    }

    public void setAbnormalAttr(String abnormalAttr) {
        this.abnormalAttr = abnormalAttr;
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

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getWardType() {
        return wardType;
    }

    public void setWardType(String wardType) {
        this.wardType = wardType;
    }

    public String getValueDesc() {
        return valueDesc;
    }

    public void setValueDesc(String valueDesc) {
        this.valueDesc = valueDesc;
    }
}
