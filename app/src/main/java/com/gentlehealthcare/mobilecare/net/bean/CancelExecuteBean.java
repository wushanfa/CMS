package com.gentlehealthcare.mobilecare.net.bean;

import com.gentlehealthcare.mobilecare.UserInfo;

import java.io.UnsupportedEncodingException;

/**
 * 取消执行实体类
 * Created by ouyang on 2015/3/24.
 */
public class CancelExecuteBean {
    private String planId;
    private String performDesc;
    private String result;



    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getPerformDesc() {
        return performDesc;
    }

    public void setPerformDesc(String performDesc) {
        this.performDesc = performDesc;
    }

    @Override
    public String toString() {
        String performDesc="";
        try {
            performDesc=getPerformDesc()==null ?"":java.net.URLEncoder.encode(getPerformDesc(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return "?username="+ UserInfo.getUserName()+"&planId="+getPlanId()+"&performDesc="+performDesc;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
