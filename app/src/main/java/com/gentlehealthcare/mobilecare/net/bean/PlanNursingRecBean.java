package com.gentlehealthcare.mobilecare.net.bean;

/**
 * Created by ouyang on 15/7/7.
 */
public class PlanNursingRecBean {
    private String eventClass;
    private String logTime;
    private String performType;
    private String name;
    private String nurseInOperate;
    private String performDesc;

    public String getPerformDesc() {
        return performDesc;
    }

    public void setPerformDesc(String performDesc) {
        this.performDesc = performDesc;
    }

    public String getNurseInOperate() {
        return nurseInOperate;
    }

    public void setNurseInOperate(String nurseInOperate) {
        this.nurseInOperate = nurseInOperate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPerformType() {
        return performType;
    }

    public void setPerformType(String performType) {
        this.performType = performType;
    }

    public String getLogTime() {
        return logTime;
    }

    public void setLogTime(String logTime) {
        this.logTime = logTime;
    }

    public String getEventClass() {
        return eventClass;
    }

    public void setEventClass(String eventClass) {
        this.eventClass = eventClass;
    }
}
