package com.gentlehealthcare.mobilecare.net.bean;

import com.gentlehealthcare.mobilecare.UserInfo;

import java.io.Serializable;

/**
 * create by zhiwei on 2015-12-04
 */
public class LoadOrderClassBean implements Serializable {
    private String username;
    private String patId;// 病人ID
    private String itemName;
    private String itemCode;

    @Override
    public String toString() {
        return "?username=" + UserInfo.getUserName() + "&patId=" + getPatId();
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getPatId() {
        return patId;
    }

    public void setPatId(String patId) {
        this.patId = patId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
