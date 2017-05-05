package com.gentlehealthcare.mobilecare.net.bean;

import com.gentlehealthcare.mobilecare.UserInfo;

import java.io.Serializable;

public class ExecuteNoPerformedBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String username;
    private String patId;
    private String planOrderNo;
    private String nurseInOperate;
    private String wardCode;

    /**
     * 返回时参数
     */
    private String result;

    /**
     * 请求url字符串
     */
    @Override
    public String toString() {

        return "?username=" + UserInfo.getUserName() + "&patId=" + patId + "&planOrderNo=" + planOrderNo +
                "&nurseInOperate=" + UserInfo.getUserName() + "&wardCode=" + UserInfo.getWardCode();
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

    public String getNurseInOperate() {
        return nurseInOperate;
    }

    public void setNurseInOperate(String nurseInOperate) {
        this.nurseInOperate = nurseInOperate;
    }

    public String getWardCode() {
        return wardCode;
    }

    public void setWardCode(String wardCode) {
        this.wardCode = wardCode;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
