package com.gentlehealthcare.mobilecare.net.bean;

import com.gentlehealthcare.mobilecare.UserInfo;

/**
 * 最近一次用餐时间 实体类
 * Created by ouyang on 15/8/30.
 */
public class LoadLastDinnerTimeBean {
    private String msg;
    private boolean result;
    private String patId;
    private String planOrderNo;
    private String lastDinnerTime;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getPatId() {
        return patId;
    }

    public void setPatId(String patId) {
        this.patId = patId;
    }

    public String getPlanOrderNo() {
        return planOrderNo;
    }

    public void setPlanOrderNo(String planOrderNo) {
        this.planOrderNo = planOrderNo;
    }

    public String getLastDinnerTime() {
        return lastDinnerTime;
    }

    public void setLastDinnerTime(String lastDinnerTime) {
        this.lastDinnerTime = lastDinnerTime;
    }

    @Override
    public String toString() {
        return "?username="+ UserInfo.getUserName()+"&patId="+getPatId()+"&planOrderNo="+getPlanOrderNo();
    }
}
