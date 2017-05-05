package com.gentlehealthcare.mobilecare.net.bean;

import com.gentlehealthcare.mobilecare.UserInfo;

import java.io.UnsupportedEncodingException;

/**
 * 记录
 * Created by ouyang on 2015/3/23.
 */
public class PatrolBean {
    private String performDesc;
    private String planOrderNo;
    private String patId;
    private int type=0;
    private String msg;
    private boolean result;

    @Override
    public String toString() {
        String performDesc="";
        try {
            performDesc=getPerformDesc()==null?"":java.net.URLEncoder.encode(getPerformDesc(),"utf-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "?username="+ UserInfo.getUserName()+"&performDesc="+performDesc+"&patId="+getPatId()+"&planOrderNo="+getPlanOrderNo();
    }

    public String getPerformDesc() {
        return performDesc;
    }

    public void setPerformDesc(String performDesc) {
        this.performDesc = performDesc;
    }




    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPlanOrderNo() {
        return planOrderNo;
    }

    public void setPlanOrderNo(String planOrderNo) {
        this.planOrderNo = planOrderNo;
    }

    public String getPatId() {
        return patId;
    }

    public void setPatId(String patId) {
        this.patId = patId;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
