package com.gentlehealthcare.mobilecare.net.bean;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author dzw
 */
public class LoadIcuOrderBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * 发送时参数
     */
    private String patId;
    private String startDateTime;
    private String repeatIndicator;


    /**
     * 返回时参数
     */
    private String orderNo;
    private String orderSubNo;
    private String orderClass;
    private String orderText;
    private String dosage;
    private String dosageUnits;
    private String administration;
    private String frequency;
    private String orderedDoctor;
    private String orderSubClass;

    /**
     * 请求url字符串
     */
    @Override
    public String toString() {
        String startDateTime = "";
        try {
            startDateTime = getStartDateTime() == null ? "" : URLEncoder.encode(getStartDateTime(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "?patId=" + getPatId() + "&startDateTime=" + startDateTime + "&repeatIndicator=" +
                getRepeatIndicator();
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getPatId() {
        return patId;
    }

    public void setPatId(String patId) {
        this.patId = patId;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getRepeatIndicator() {
        return repeatIndicator;
    }

    public void setRepeatIndicator(String repeatIndicator) {
        this.repeatIndicator = repeatIndicator;
    }

    public String getOrderClass() {
        return orderClass;
    }

    public void setOrderClass(String orderClass) {
        this.orderClass = orderClass;
    }

    public String getOrderText() {
        return orderText;
    }

    public void setOrderText(String orderText) {
        this.orderText = orderText;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getDosageUnits() {
        return dosageUnits;
    }

    public void setDosageUnits(String dosageUnits) {
        this.dosageUnits = dosageUnits;
    }

    public String getAdministration() {
        return administration;
    }

    public void setAdministration(String administration) {
        this.administration = administration;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getOrderedDoctor() {
        return orderedDoctor;
    }

    public void setOrderedDoctor(String orderedDoctor) {
        this.orderedDoctor = orderedDoctor;
    }

    public String getOrderSubClass() {
        return orderSubClass;
    }

    public void setOrderSubClass(String orderSubClass) {
        this.orderSubClass = orderSubClass;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderSubNo() {
        return orderSubNo;
    }

    public void setOrderSubNo(String orderSubNo) {
        this.orderSubNo = orderSubNo;
    }
}
