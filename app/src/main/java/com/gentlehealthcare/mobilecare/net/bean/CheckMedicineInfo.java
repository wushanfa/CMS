package com.gentlehealthcare.mobilecare.net.bean;

/**
 * Created by ouyang on 2015/3/23.
 */
public class CheckMedicineInfo {
    private String performType;
    private String performDesc;
    private String  planId;
    private boolean performState;

    public String getPerformType() {
        return performType;
    }

    public void setPerformType(String performType) {
        this.performType = performType;
    }

    public String getPerformDesc() {
        return performDesc;
    }

    public void setPerformDesc(String performDesc) {
        if (performDesc.equals("药品核对成功"))
            setPerformState(true);
        else
        setPerformState(false);
        this.performDesc = performDesc;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public boolean isPerformState() {
        if (getPerformDesc().equals("药品核对成功"))
        {
            this.setPerformState(true);
            return true;
        }
        this.setPerformState(false);
        return false;
    }

    public void setPerformState(boolean performState) {
        this.performState = performState;
    }
}
