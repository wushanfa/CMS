package com.gentlehealthcare.mobilecare.net.bean;

import com.gentlehealthcare.mobilecare.UserInfo;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by zhiwei on 2015/11/23.
 */
public class RecInspectBean implements Serializable {
    private String username;
    private String patId;
    private String planOrderNo;
    private String skinTestResult;
    private String dosageActual;
    private String performDesc;
    private boolean result;
    private String msg;
    private int type;

    @Override
    public String toString() {
        try {
            return "?username=" + UserInfo.getUserName() + "&patId=" + getPatId() + "&planOrderNo=" +
                    getPlanOrderNo() + "&performDesc=" + URLEncoder.encode(getPerformDesc(), "utf-8") +
                    "&dosageActual=" + getDosageActual() + "&skinTestResult=" + URLEncoder.encode
                    (getSkinTestResult(), "utf-8")+"&type="+getType();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSkinTestResult() {
        return skinTestResult;
    }

    public void setSkinTestResult(String skinTestResult) {
        this.skinTestResult = skinTestResult;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDosageActual() {
        return dosageActual;
    }

    public void setDosageActual(String dosageActual) {
        this.dosageActual = dosageActual;
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

    public String getPerformDesc() {
        return performDesc;
    }

    public void setPerformDesc(String performDesc) {
        this.performDesc = performDesc;
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
