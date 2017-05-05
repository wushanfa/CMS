package com.gentlehealthcare.mobilecare.net.bean;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

public class SaveICUABean implements Serializable {

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
    private String vitalSignsValues;
    private String memo;
    private String patternId;

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
        String vitalSignsValues = "";
        String memo = "";
        String patternId = "";
        try {
            recordingTime = getRecordingTime() == null ? "" : java.net.URLEncoder.encode(getRecordingTime()
                    , "utf-8");
            patId = getPatId() == null ? "" : java.net.URLEncoder.encode(getPatId(), "utf-8");
            itemNo = getItemNo() == null ? "nothing" : getItemNo();
            vitalSignsValues = getVitalSignsValues() == null ? "nothing" : getVitalSignsValues();
            memo = getMemo() == null ? "nothing" : getMemo();
            logBy = getLogBy() == null ? "" : java.net.URLEncoder.encode(getLogBy(), "utf-8");
            patternId = getPatternId() == null ? "nothing" : getPatternId();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "?recordingTime=" + recordingTime + "&patId=" + patId + "&itemNo=" + itemNo +
                "&vitalSignsValues=" + vitalSignsValues + "&memo=" + memo + "&logBy=" + logBy + "&patternId=" +
                patternId;
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

    public String getVitalSignsValues() {
        return vitalSignsValues;
    }

    public void setVitalSignsValues(String vitalSignsValues) {
        this.vitalSignsValues = vitalSignsValues;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getPatternId() {
        return patternId;
    }

    public void setPatternId(String patternId) {
        this.patternId = patternId;
    }
}
