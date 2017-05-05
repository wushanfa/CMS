package com.gentlehealthcare.mobilecare.net.bean;

import com.gentlehealthcare.mobilecare.UserInfo;

import java.io.UnsupportedEncodingException;

/**
 * 血糖检测异常
 * Created by ouyang on 15/7/9.
 */
public class CompleteGlucoseValueCheckBean {
    private String planOrderNo;
    private boolean result;
    private String patId;
    private String performDesc;
    private String type;
    private String msg;
    private String glucoseVal;

    @Override
    public String toString() {
        String performDesc="";

        try {
            performDesc=getPerformDesc()==null ?"":java.net.URLEncoder.encode(getPerformDesc(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return "?username="+ UserInfo.getUserName()+"&planOrderNo="+getPlanOrderNo()+"&performDesc="+performDesc+"&patId="+getPatId()+"&type="+getType()+"&glucoseVal="+getGlucoseVal();
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

    public String getPerformDesc() {
        return performDesc;
    }

    public void setPerformDesc(String performDesc) {
        this.performDesc = performDesc;
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

    public String getGlucoseVal() {
        return glucoseVal;
    }

    public void setGlucoseVal(String glucoseVal) {
        this.glucoseVal = glucoseVal;
    }
}
