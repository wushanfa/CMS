package com.gentlehealthcare.mobilecare.net.bean;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by zhiwei on 2016/4/12.
 */
public class SkinResult implements Serializable {
    private String result;
    private int skinTestResult;
    private String orderId;
    private String patId;

    @Override
    public String toString() {
        String orderIdTemp = null;
        try {
            orderIdTemp = URLEncoder.encode(getOrderId(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = "?skinTestResult=" + getSkinTestResult() + "&orderId=" + orderIdTemp + "&patId=" +
                getPatId();
        return result;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getSkinTestResult() {
        return skinTestResult;
    }

    public void setSkinTestResult(int skinTestResult) {
        this.skinTestResult = skinTestResult;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPatId() {
        return patId;
    }

    public void setPatId(String patId) {
        this.patId = patId;
    }
}
