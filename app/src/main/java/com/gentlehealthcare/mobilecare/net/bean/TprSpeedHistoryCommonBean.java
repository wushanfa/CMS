package com.gentlehealthcare.mobilecare.net.bean;

import com.gentlehealthcare.mobilecare.UserInfo;

import java.io.Serializable;

/**
 * tpr and speed common history bean
 *
 * @author diaozhiwei
 * @date 2016/02/29
 */
public class TprSpeedHistoryCommonBean implements Serializable {
    private String T;
    private String P;
    private String R;
    private String time;
    private String speed;

    private String patId;
    private String planOrderNo;

    @Override
    public String toString() {
        return "?patId=" + patId + "&planOrderNo=" + planOrderNo + "&username=" + UserInfo.getUserName();
    }

    public String getT() {
        return T;
    }

    public void setT(String t) {
        T = t;
    }

    public String getP() {
        return P;
    }

    public void setP(String p) {
        P = p;
    }

    public String getR() {
        return R;
    }

    public void setR(String r) {
        R = r;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
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
}
