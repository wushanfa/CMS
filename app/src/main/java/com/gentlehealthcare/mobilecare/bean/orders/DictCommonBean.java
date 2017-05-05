package com.gentlehealthcare.mobilecare.bean.orders;

import com.gentlehealthcare.mobilecare.UserInfo;

import java.io.Serializable;

public class DictCommonBean implements Serializable {


    private String itemName;
    private String itemCode;
    private String username;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    private boolean isSelect;

    @Override
    public String toString() {
        return "?username=" + UserInfo.getUserName();
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
