package com.gentlehealthcare.mobilecare.net.bean;

/**
 * 加载异常字典
 * Created by ouyang on 15/8/28.
 */
public class LoadVariationDictBean {
    private  String itemName;
    private String itemCode;
    private int type;

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

    public String getItemContent(){
        return getItemCode()+" "+getItemName();
    }

    @Override
    public String toString() {
        return "";
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
