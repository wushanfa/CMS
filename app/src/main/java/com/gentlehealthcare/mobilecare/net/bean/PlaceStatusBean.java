package com.gentlehealthcare.mobilecare.net.bean;

/**
 * Created by ouyang on 15/6/24.
 */
public class PlaceStatusBean {

    private String status;//-99 ( 紅色-禁打);-98(桃紅-拒打); 1-(藍色-注射過)；0-（粉紅-未打）
    private String siteNo;
    private String itemNo;

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

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }
}
