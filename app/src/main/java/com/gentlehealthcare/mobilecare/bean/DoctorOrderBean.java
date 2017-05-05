package com.gentlehealthcare.mobilecare.bean;

import java.util.List;

/**
 * Created by ouyang on 2015/5/26.
 */
public class DoctorOrderBean {
    private String title;
    private boolean check;
    private List<DoctorOrderInfo> doctorOrderInfoList;



    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<DoctorOrderInfo> getDoctorOrderInfoList() {
        return doctorOrderInfoList;
    }

    public void setDoctorOrderInfoList(List<DoctorOrderInfo> doctorOrderInfoList) {
        this.doctorOrderInfoList = doctorOrderInfoList;
    }
}
