package com.gentlehealthcare.mobilecare.net.bean;

import com.gentlehealthcare.mobilecare.UserInfo;

/**
 * 修改区域块状态
 * Created by ouyang on 15/6/26.
 */
public class SetStatusForAreaBean {
    private String siteCode;//区域代码A-左上臂； B-右上臂；C-左腹；D-右腹；E-左大腿；F-右大腿；G-左外臀；H-右外臀
    private String operNo;//状态标识 -99 ( 紅色-禁打);-98(桃紅-拒打); 1-(藍色-注射過)；0-（粉紅-未打）
    private String patId;
    private String siteNo;

    public String getPatId() {
        return patId;
    }

    public void setPatId(String patId) {
        this.patId = patId;
    }

    public String getOperNo() {
        return operNo==null?"":operNo;
    }

    public void setOperNo(String operNo) {
        this.operNo = operNo;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    @Override
    public String toString() {
        return "?username="+ UserInfo.getUserName()+"&patId="+getPatId()+"&siteCode="+getSiteCode()+"&operNo="+getOperNo()+"&siteNo="+getSiteNo();
    }

    public String getSiteNo() {
        return siteNo==null?"":siteNo;
    }

    public void setSiteNo(String siteNo) {
        this.siteNo = siteNo;
    }
}
