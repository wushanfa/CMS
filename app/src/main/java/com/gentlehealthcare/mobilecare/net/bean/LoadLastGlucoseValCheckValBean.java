package com.gentlehealthcare.mobilecare.net.bean;

import com.gentlehealthcare.mobilecare.UserInfo;

/**
 * Created by ouyang on 15/7/14.
 */
public class LoadLastGlucoseValCheckValBean {
    private boolean result;
    private String planStartTime;
    private String eventStartTime;
    private String planTimeAttr;//0 临时 1饭前 2饭后
    private String glucoseVal;
    private String patId;
    private String msg;
    private String planOrderNo;
    private String itemName;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getGlucoseVal() {
        return glucoseVal;
    }

    public void setGlucoseVal(String glucoseVal) {
        this.glucoseVal = glucoseVal;
    }

    public String getPlanStartTime() {
        return planStartTime;
    }

    public void setPlanStartTime(String planStartTime) {
        this.planStartTime = planStartTime;
    }

    public String getEventStartTime() {
        return eventStartTime;
    }

    public void setEventStartTime(String eventStartTime) {
        this.eventStartTime = eventStartTime;
    }

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "?username=" + UserInfo.getUserName() + "&patId=" + getPatId() + "&planOrderNo=" + getPlanOrderNo();
    }

    public String getPatId() {
        return patId;
    }

    public void setPatId(String patId) {
        this.patId = patId;
    }

    public String getPlanTimeAttr() {
        return planTimeAttr;
    }

    public void setPlanTimeAttr(String planTimeAttr) {
        this.planTimeAttr = planTimeAttr;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getPlanOrderNo() {
        return planOrderNo;
    }

    public void setPlanOrderNo(String planOrderNo) {
        this.planOrderNo = planOrderNo;
    }
}
