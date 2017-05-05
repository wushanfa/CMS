package com.gentlehealthcare.mobilecare.net.bean;

import com.gentlehealthcare.mobilecare.UserInfo;

import java.io.UnsupportedEncodingException;

/**
 * 记录巡视时间
 * Created by ouyang on 15/7/16.
 */
public class RecordInspectionTimeBean {
    private int type;//0 静脉给药  1胰岛素
    private String patId;
    private String inspectionTime;
    private  String planOrderNo;
    private String performDesc;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private String msg;
    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    private boolean result;


    public String getInspectionTime() {
        return inspectionTime;
    }

    public void setInspectionTime(String inspectionTime) {
        this.inspectionTime = inspectionTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        String performDesc="";
        try {
            performDesc=getPerformDesc()==null?"":java.net.URLEncoder.encode(getPerformDesc(),"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "?username="+ UserInfo.getUserName()+"&patId="+getPatId()+"&inspectionTime="+getInspectionTime()+"&planOrderNo="+getPlanOrderNo()+"&performDesc="+performDesc;

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

    public String getPerformDesc() {
        return performDesc;
    }

    public void setPerformDesc(String performDesc) {
        this.performDesc = performDesc;
    }
}
