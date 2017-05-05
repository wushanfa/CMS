package com.gentlehealthcare.mobilecare.net.bean;

import java.io.Serializable;

/**
 * Created by zhiwei on 2015/11/23.
 */
public class IntraLoadInspectionBean implements Serializable {
    private String patId;
    private String planOrderNo;

    private String inspectionTime;
    private String eventStartTime;
    private boolean result;
    private String msg;

    @Override
    public String toString() {
        String patId = "";
        String planOrderNo = "";
        patId = getPatId() == null ? "" : getPatId();
        planOrderNo = getPlanOrderNo() == null ? "" : getPlanOrderNo();
        return "?patId=" + patId + "&planOrderNo=" + planOrderNo;
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

    public String getInspectionTime() {
        return inspectionTime;
    }

    public void setInspectionTime(String inspectionTime) {
        this.inspectionTime = inspectionTime;
    }

    public String getEventStartTime() {
        return eventStartTime;
    }

    public void setEventStartTime(String eventStartTime) {
        this.eventStartTime = eventStartTime;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
