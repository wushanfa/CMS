package com.gentlehealthcare.mobilecare.net.bean;

/**
 * Created by Zyy on 2015/11/28.
 * 类说明：已完成血糖测试
 */
public class FinishedShugar {

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getLogBy() {
        return logBy;
    }

    public void setLogBy(String logBy) {
        this.logBy = logBy;
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



    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String val;// "14.0",
    public String itemName;// "空腹血糖",

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }

    public String reference;// "超低<2.8<低<3.9~6.1<高<7.0mmol/L",
    public String res;// "高",
    public String patId;// "40609",
    public String recordingTime;// 1448719582000,
    public String logBy;// "水根会",
    public String itemCode;// "BLOOD_GLUCOSE01"
}
