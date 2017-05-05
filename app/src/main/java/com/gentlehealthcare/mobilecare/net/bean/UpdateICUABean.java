package com.gentlehealthcare.mobilecare.net.bean;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

/**
 * Created by admin on 2015/11/16.
 */
public class UpdateICUABean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * 发送请求参数
     */
    private String itemNo;
    private String vsId;
    private String logBy;
    private String recordingTime;
    private String vitalSignsValues;
    private String memo;
    private String patternId;
    private String patId;

    /**
     * 返回时参数
     */
    private String result;

    @Override
    public String toString() {
        String itemNo = "";
        String vsId = "";
        String logBy = "";
        String recordingTime = "";
        String vitalSignsValues = "";
        String memo = "";
        String patternId = "";
        String patId = "";
        try {
            itemNo = getItemNo() == null ? "nothing" : getItemNo();
            recordingTime = getRecordingTime() == null ? "" : java.net.URLEncoder.encode
                    (getRecordingTime(), "utf-8");
            vitalSignsValues = getVitalSignsValues() == null ? "nothing" : getVitalSignsValues();
            memo = getMemo() == null ? "nothing" : getMemo();
            vsId = getVsId() == null ? "nothing" : getVsId();
            logBy = getLogBy() == null ? "" : java.net.URLEncoder.encode(getLogBy(), "utf-8");
            patternId = getPatternId() == null ? "nothing" : getPatternId();
            patId = getPatId() == null ? "nothing" : java.net.URLEncoder.encode(getPatId(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "?recordingTime=" + recordingTime + vsId + "&vitalSignsValues=" +
                vitalSignsValues + "&memo=" + memo + "&logBy=" + logBy + "&patternId=" +
                patternId + "&patId=" + patId + "&itemNo=" + itemNo;
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

    public String getVsId() {
        return vsId;
    }

    public void setVsId(String vsId) {
        this.vsId = vsId;
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
