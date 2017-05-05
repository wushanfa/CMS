package com.gentlehealthcare.mobilecare.bean;

import java.io.Serializable;

/**
 * Created by Zyy on 2015/12/3.
 * 类说明：静脉给药不执行数据字典
 */
public class IntraDontExcuteBean implements Serializable{
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

    public String itemName;
    public String itemCode;
}
