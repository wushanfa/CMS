package com.gentlehealthcare.mobilecare.net.bean;

/**
 * Created by ouyang on 15/7/9.
 */
public class PlaceStausItem {
    private String itemNo;
    private String  siteId;
    private String status;
    private String siteNo;
    private String siteDesc;
    private String siteSubNo;
    private String siteCode;
    private String posAttr;

    public String getPosAttr() {
        return posAttr;
    }

    public void setPosAttr(String posAttr) {
        this.posAttr = posAttr;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getSiteSubNo() {
        return siteSubNo;
    }

    public void setSiteSubNo(String siteSubNo) {
        this.siteSubNo = siteSubNo;
    }

    public String getSiteDesc() {
        return siteDesc;
    }

    public void setSiteDesc(String siteDesc) {
        this.siteDesc = siteDesc;
    }

    public String getSiteNo() {
        return siteNo;
    }

    public void setSiteNo(String siteNo) {
        this.siteNo = siteNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }
}

