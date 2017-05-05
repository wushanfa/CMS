package com.gentlehealthcare.mobilecare.net.bean;

import com.gentlehealthcare.mobilecare.UserInfo;

/**
 * Created by ouyang on 15/6/24.
 */
public class BloodTestValCheckBean {
    private String patId;
    private String glucoseVal;
    private String planOrderNo;
    private boolean result;
    private String type;
    private String msg;
    private boolean checkResult;
    private String doctorInCharge;
    public String getDoctorInCharge() {
        return doctorInCharge;
    }

    public void setDoctorInCharge(String doctorInCharge) {
        this.doctorInCharge = doctorInCharge;
    }



    @Override
    public String toString() {
        return "?username="+ UserInfo.getUserName()+"&patId="+getPatId()+"&glucoseVal="+getGlucoseVal()+"&type="+getType()+"&planOrderNo="+getPlanOrderNo();
    }

    public String getGlucoseVal() {
        return glucoseVal;
    }

    public void setGlucoseVal(String glucoseVal) {
        this.glucoseVal = glucoseVal;
    }



    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPlanOrderNo() {
        return planOrderNo;
    }

    public void setPlanOrderNo(String planOrderNo) {
        this.planOrderNo = planOrderNo;
    }

    public String getPatId() {
        return patId;
    }

    public void setPatId(String patId) {
        this.patId = patId;
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

    public boolean isCheckResult() {
        return checkResult;
    }

    public void setCheckResult(boolean checkResult) {
        this.checkResult = checkResult;
    }
}
