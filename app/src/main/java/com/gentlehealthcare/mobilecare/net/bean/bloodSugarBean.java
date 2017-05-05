package com.gentlehealthcare.mobilecare.net.bean;

import java.io.Serializable;

/**
 * Created by zhiwei on 2015/11/27.
 */
public class bloodSugarBean implements Serializable {
    private String doctorInCharge;
    private String hig;
    private boolean result;
    private String desc;
    private boolean flag;
    private String patId;
    private String planOrderNo;
    private String low;
    private String msg;

    @Override
    public String toString() {
        return "";
    }

    public String getHig() {
        return hig;
    }

    public void setHig(String hig) {
        this.hig = hig;
    }

    public String getDoctorInCharge() {
        return doctorInCharge;
    }

    public void setDoctorInCharge(String doctorInCharge) {
        this.doctorInCharge = doctorInCharge;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
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

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
