package com.gentlehealthcare.mobilecare.net.bean;

import java.io.Serializable;

/**
 * Created by dzw on 2015/11/13.
 */
public class LoadIcuBResultBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String itemName;
    private String dataSource;
    private String itemCodeType;
    private String abnormalAttr;
    private String memo;
    private String formId;
    private String selectAttr;
    private String wardType;
    private String itemCode;
    private String defaultAttr;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getItemCodeType() {
        return itemCodeType;
    }

    public void setItemCodeType(String itemCodeType) {
        this.itemCodeType = itemCodeType;
    }

    public String getAbnormalAttr() {
        return abnormalAttr;
    }

    public void setAbnormalAttr(String abnormalAttr) {
        this.abnormalAttr = abnormalAttr;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getSelectAttr() {
        return selectAttr;
    }

    public void setSelectAttr(String selectAttr) {
        this.selectAttr = selectAttr;
    }

    public String getWardType() {
        return wardType;
    }

    public void setWardType(String wardType) {
        this.wardType = wardType;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getDefaultAttr() {
        return defaultAttr;
    }

    public void setDefaultAttr(String defaultAttr) {
        this.defaultAttr = defaultAttr;
    }
}
