package com.gentlehealthcare.mobilecare.bean;

import java.io.Serializable;

/**
 * @author Zyy
 * @date 2015-9-17下午3:45:34
 * @category 观察Bean
 */
public class LookBean implements Serializable {
    private String itemName;// 内容
    private String itemCode;// 标识数量
    private int itemNo;// 编号
    private boolean isSelected;

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

    public int getItemNo() {
        return itemNo;
    }

    public void setItemNo(int itemNo) {
        this.itemNo = itemNo;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
