package com.gentlehealthcare.mobilecare.net.bean;

import com.gentlehealthcare.mobilecare.UserInfo;

import java.io.Serializable;

/**
 * Created by zhiwei on 2015/11/23.
 */
public class CompleteInfusionBean implements Serializable {
    private String username;
    private String patId;
    private String planOrderNo;
    private int status;
    private String completeDosage;
    private String varianceCause;
    private String varianceCauseDesc;

    private boolean result;
    private String msg;

    @Override
    public String toString() {
        return "?username=" + UserInfo.getUserName() + "&patId=" + getPatId() + "&planOrderNo=" + getPlanOrderNo() +
                "&status=" + getStatus() + "&completeDosage=" + getCompleteDosage() + "&varianceCause=" +
                getVarianceCause() + "&varianceCauseDesc=" + getVarianceCauseDesc();
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCompleteDosage() {
        return completeDosage;
    }

    public void setCompleteDosage(String completeDosage) {
        this.completeDosage = completeDosage;
    }

    public String getVarianceCause() {
        return varianceCause;
    }

    public void setVarianceCause(String varianceCause) {
        this.varianceCause = varianceCause;
    }

    public String getVarianceCauseDesc() {
        return varianceCauseDesc;
    }

    public void setVarianceCauseDesc(String varianceCauseDesc) {
        this.varianceCauseDesc = varianceCauseDesc;
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
