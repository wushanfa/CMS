package com.gentlehealthcare.mobilecare.net.bean;

import java.io.Serializable;

/**
 * Created by zhiwei on 2015/11/23.
 */
public class LoadExceptionBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String itemName;
    private String itemCode;

    @Override
    public String toString() {
        return "";
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
}
