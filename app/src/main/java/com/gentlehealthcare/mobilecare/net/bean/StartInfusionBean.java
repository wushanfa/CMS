package com.gentlehealthcare.mobilecare.net.bean;

import com.gentlehealthcare.mobilecare.UserInfo;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by zhiwei on 2015/11/23.
 */
public class StartInfusionBean implements Serializable {
    private String username;
    private String patId;
    private String planOrderNo;
    private String injectionTool;
    private String speed;

    public String getNextPatrolTime() {
        return nextPatrolTime;
    }

    public void setNextPatrolTime(String nextPatrolTime) {
        this.nextPatrolTime = nextPatrolTime;
    }

    private String nextPatrolTime;
    private boolean result;
    private String msg;


    @Override
    public String toString() {
        try {
            return "?username=" + UserInfo.getUserName() + "&patId=" + getPatId() + "&planOrderNo=" +
                    getPlanOrderNo() +
                    "&injectionTool=" + getInjectionTool() + "&speed=" + getSpeed() + "&nextPatrolTime=" +
                    URLEncoder.encode(getNextPatrolTime(), "utf-8") ;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getInjectionTool() {
        return injectionTool;
    }

    public void setInjectionTool(String injectionTool) {
        this.injectionTool = injectionTool;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
