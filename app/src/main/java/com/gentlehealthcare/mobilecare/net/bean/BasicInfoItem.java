package com.gentlehealthcare.mobilecare.net.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BasicInfoItem {
    private String itemName;//部门名称
    @SerializedName("NAME")
    private String name;//用户名称
    private List<WardInfoItem> wardList;//病房列表
    @SerializedName("DEPTCODE")
    private String deptCode;//部门编号
    @SerializedName("DEPTNAME")
    private String deptName;//部门名称
    @SerializedName("CAPABILITY")
    private String capability;//用户等级

    public String getCapability() {
        return capability;
    }

    public void setCapability(String capability) {
        this.capability = capability;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<WardInfoItem> getWardList() {
        return wardList;
    }

    public void setWardList(List<WardInfoItem> wardList) {
        this.wardList = wardList;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }


    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
}
