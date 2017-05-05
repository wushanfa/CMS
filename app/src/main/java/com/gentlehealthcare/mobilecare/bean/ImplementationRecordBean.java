package com.gentlehealthcare.mobilecare.bean;

/**
 * Created by Zyy on 2016/5/9.
 * 类说明:执行记录javaBean
 */
public class ImplementationRecordBean {

    /**
     * name : 高耐利
     * eventStartTime : 06-06
     * performType : B1
     * performDesc : 核对血型成功
     */

    private String name;
    private String eventStartTime;
    private String performType;
    private String performDesc;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEventStartTime() {
        return eventStartTime;
    }

    public void setEventStartTime(String eventStartTime) {
        this.eventStartTime = eventStartTime;
    }

    public String getPerformType() {
        return performType;
    }

    public void setPerformType(String performType) {
        this.performType = performType;
    }

    public String getPerformDesc() {
        return performDesc;
    }

    public void setPerformDesc(String performDesc) {
        this.performDesc = performDesc;
    }
}
