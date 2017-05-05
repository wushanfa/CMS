package com.gentlehealthcare.mobilecare.net.bean;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

public class SaveICUBBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * 发送请求参数
     */
    private String itemNo;
    private String patId;
    private String logBy;
    private String recordingTime;
    private String valueCode;
    private String valueName;
    private String valueType;
    private String abnormalAttr;
    private String valueDesc;
    private int rowNo;
    private String columnName;
    private int flag;
    private String wardType;
    /**
     * 返回时参数
     */
    private String result;
    private String formNo;


    @Override
    public String toString() {
        String itemNo = "";
        String patId = "";
        String logBy = "";
        String recordingTime = "";
        String valueCode = "";
        String valueName = "";
        String valueType = "";
        String abnormalAttr = "";
        String valueDesc = "";
        String columnName = "";
        String wardType = "";
        try {
            recordingTime = getRecordingTime() == null ? "" : java.net.URLEncoder.encode
                    (getRecordingTime(), "utf-8");
            patId = getPatId() == null ? "" : java.net.URLEncoder.encode(getPatId(), "utf-8");
            itemNo = getItemNo() == null ? "nothing" : getItemNo();
            logBy = getLogBy() == null ? "" : java.net.URLEncoder.encode(getLogBy(), "utf-8");
            valueCode = getValueCode() == null ? "nothing" : getValueCode();
            valueName = getValueName() == null ? "nothing" : getValueName();
            valueType = getValueType() == null ? "nothing" : getValueType();
            abnormalAttr = getAbnormalAttr() == null ? "nothing" : getAbnormalAttr();
            valueDesc = getValueDesc() == null ? "nothing" : getValueDesc();
            columnName = getColumnName() == null ? "nothing" : getColumnName();
            wardType = getWardType() == null ? "nothing" : getWardType();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "?recordingTime=" + recordingTime + "&patId=" + patId + "&itemNo=" + itemNo +
                "&logBy=" + logBy + "&valueCode=" + valueCode + "&valueName=" + valueName + "&valueType=" +
                valueType + "&abnormalAttr=" + abnormalAttr + "&valueDesc=" + valueDesc + "&rowNo=" + getRowNo() +
                "&flag=" + getFlag() + "&formNo=" + getFormNo() + "&columnName=" + columnName+"&wardType="+wardType;
    }

    public String getWardType() {
        return wardType;
    }

    public void setWardType(String wardType) {
        this.wardType = wardType;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
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

    public String getLogBy() {
        return logBy;
    }

    public void setLogBy(String logBy) {
        this.logBy = logBy;
    }

    public String getRecordingTime() {
        return recordingTime;
    }

    public void setRecordingTime(String recordingTime) {
        this.recordingTime = recordingTime;
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

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getValueDesc() {
        return valueDesc;
    }

    public void setValueDesc(String valueDesc) {
        this.valueDesc = valueDesc;
    }

    public int getRowNo() {
        return rowNo;
    }

    public void setRowNo(int rowNo) {
        this.rowNo = rowNo;
    }

    public String getFormNo() {
        return formNo;
    }

    public void setFormNo(String formNo) {
        this.formNo = formNo;
    }
}
