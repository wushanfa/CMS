package com.gentlehealthcare.mobilecare.net.bean;

import java.io.Serializable;

/**
 * Created by zhiwei on 2015/11/27.
 */
public class BloodSugarItemBean implements Serializable {
    private String itemCode;
    private String itemName;

    @Override
    public String toString() {
        return "";
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
}
