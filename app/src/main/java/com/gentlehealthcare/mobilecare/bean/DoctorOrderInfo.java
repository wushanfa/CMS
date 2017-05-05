package com.gentlehealthcare.mobilecare.bean;


/**
 * 医嘱列表的Item
 * Created by ouyang on 2015/5/27.
 */
public class DoctorOrderInfo {
    private String  leftName;
    private String rightName;

    public DoctorOrderInfo(String leftName, String rightName) {
        this.leftName = leftName;
        this.rightName = rightName;
    }

    public String getRightName() {
        return rightName;
    }

    public void setRightName(String rightName) {
        this.rightName = rightName;
    }

    public String getLeftName() {
        return leftName;
    }

    public void setLeftName(String leftName) {
        this.leftName = leftName;
    }
}
