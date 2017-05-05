package com.gentlehealthcare.mobilecare.net.bean;

import com.gentlehealthcare.mobilecare.UserInfo;

/**
 * 血糖测试请求
 * Created by ouyang on 15/6/23.
 */
public class BloodTestBean {
    private String patId;
    private String lastDinnerTime;//最后一次用餐时间
    private boolean result;
    private String planOrderNo;
    private String planTimeAttr;//0 临时 1 饭前 2饭后
    private String msg;


    @Override
    public String toString() {
        return "?username="+ UserInfo.getUserName()+"&patId="+getPatId()+"&lastDinnerTime="+getLastDinnerTime()+"&planOrderNo="+getPlanOrderNo()+"&planTimeAttr="+getPlanTimeAttr();
    }

    public String getPlanTimeAttr() {
        return planTimeAttr;
    }

    public void setPlanTimeAttr(String planTimeAttr) {
        this.planTimeAttr = planTimeAttr;
    }

    public String getPlanOrderNo() {
        return planOrderNo;
    }

    public void setPlanOrderNo(String planOrderNo) {
        this.planOrderNo = planOrderNo;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getLastDinnerTime() {
        return lastDinnerTime;
    }

    public void setLastDinnerTime(String lastDinnerTime) {
        this.lastDinnerTime = lastDinnerTime;
    }

    public String getPatId() {
        return patId;
    }

    public void setPatId(String patId) {
        this.patId = patId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
