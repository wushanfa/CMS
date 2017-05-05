package com.gentlehealthcare.mobilecare.net.bean;

import com.gentlehealthcare.mobilecare.UserInfo;

/**
 *
 * Created by ouyang on 15/6/2.
 */
public class RecordFirstPageSignBean {
    private String recordTime;
    private String patId;
    private String temperature;
    private String  pulse;
    private String breath;
    private String pain;

    public String getPain() {
        return pain;
    }

    public void setPain(String pain) {
        this.pain = pain;
    }

    public String getBreath() {
        return breath;
    }

    public void setBreath(String breath) {
        this.breath = breath;
    }

    public String getPulse() {
        return pulse;
    }

    public void setPulse(String pulse) {
        this.pulse = pulse;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getPatId() {
        return patId;
    }

    public void setPatId(String patId) {
        this.patId = patId;
    }

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }

    @Override
    public String toString() {
        return "?username="+ UserInfo.getUserName()+"&patId="+getPatId()+"&temperature="+getTemperature()+"&pulse="+getPulse()+"&breath="+getBreath()+"&pain="+getPain()+"&recordTime="+getRecordTime();

    }
}
