package com.gentlehealthcare.mobilecare.net.bean;

import com.gentlehealthcare.mobilecare.UserInfo;

import java.util.List;

/**
 * 同步病人工作实体类
 * Created by ouyang on 15/7/6.
 */
public class SyncPatientWorkBean {
    private String patId;
    private boolean result;
    private String msg;
    private List<SyncPatientWorkInfo> plans;
    public String getPatId() {
        return patId;
    }

    public void setPatId(String patId) {
        this.patId = patId;
    }

    @Override
    public String toString() {
        return  "?username="+ UserInfo.getUserName()+"&patId="+patId+"&wardCode="+UserInfo.getWardCode();
    }

    public List<SyncPatientWorkInfo> getPlans() {
        return plans;
    }

    public void setPlans(List<SyncPatientWorkInfo> plans) {
        this.plans = plans;
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
