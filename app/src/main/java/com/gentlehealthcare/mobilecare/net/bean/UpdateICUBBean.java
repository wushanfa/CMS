package com.gentlehealthcare.mobilecare.net.bean;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by admin on 2015/11/16.
 */
public class UpdateICUBBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * 发送请求参数
     */

    private String patId;
    private String formNo;
    private String logBy;
    private String recordingTime;
    private String itemNo;
    private String valueCode;
    private String valueName;
    private String valueType;
    private String abnormalAttr;
    private String valueDesc;
    private String wardType;
    private int rowNo;
    private String columnName;
    private int flag;

    /**
     * 返回时参数
     */
    private String result;

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
        String formNo = "";
        String valueDesc = "";
        String wardType = "";
        String columnName = "";

        try {
            recordingTime = getRecordingTime() == null ? "" : URLEncoder.encode(getRecordingTime(), "utf-8");
            patId = getPatId() == null ? "" : URLEncoder.encode(getPatId(), "utf-8");
            formNo = getFormNo() == null ? "" : URLEncoder.encode(getFormNo(), "utf-8");
            itemNo = getItemNo() == null ? "nothing" : getItemNo();
            logBy = getLogBy() == null ? "" : URLEncoder.encode(getLogBy(), "utf-8");
            valueCode = getValueCode() == null ? "nothing" : getValueCode();
            valueName = getValueName() == null ? "nothing" : getValueName();
            valueType = getValueType() == null ? "nothing" : getValueType();
            abnormalAttr = getAbnormalAttr() == null ? "nothing" : getAbnormalAttr();
            valueDesc = getValueDesc() == null ? "nothing" : getValueDesc();
            wardType = getWardType() == null ? "nothing" : getWardType();
            columnName = getColumnName() == null ? "nothing" : getColumnName();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "?recordingTime=" + recordingTime + "&patId=" + patId + "&itemNo=" + itemNo +
                "&logBy=" + logBy + "&valueCode=" + valueCode + "&valueName=" + valueName + "&valueType=" +
                valueType + "&abnormalAttr=" + abnormalAttr + "&valueDesc=" + valueDesc +
                "&wardType=" + wardType + "&flag=" + getFlag() + "&columnName=" + columnName + "&rowNo=" + getRowNo();
    }


    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getWardType() {
        return wardType;
    }

    public void setWardType(String wardType) {
        this.wardType = wardType;
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

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
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

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
