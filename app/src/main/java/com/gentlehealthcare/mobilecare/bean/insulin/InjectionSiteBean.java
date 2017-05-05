package com.gentlehealthcare.mobilecare.bean.insulin;

import java.io.Serializable;

/**
 * Created by zhiwei on 2016/5/16.
 */
public class InjectionSiteBean implements Serializable {
    private String siteId;
    private String itemNo;
    private String siteCode;
    private String siteNo;
    private String siteDesc;
    private int status;
    private int posAttr;

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getSiteNo() {
        return siteNo;
    }

    public void setSiteNo(String siteNo) {
        this.siteNo = siteNo;
    }

    public String getSiteDesc() {
        return siteDesc;
    }

    public void setSiteDesc(String siteDesc) {
        this.siteDesc = siteDesc;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPosAttr() {
        return posAttr;
    }

    public void setPosAttr(int posAttr) {
        this.posAttr = posAttr;
    }
}
