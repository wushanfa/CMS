package com.gentlehealthcare.mobilecare.bean;

/**
 * 病人工作界面实体类
 * Created by ouyang on 15/7/6.
 */
public class PatientWorkBean {
    private String left;
    private String center;
    private String right;
    private String templateId;//模版ID
    private String planId;//计划ID
    private String state;//参考MedicineConstant

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = right;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
