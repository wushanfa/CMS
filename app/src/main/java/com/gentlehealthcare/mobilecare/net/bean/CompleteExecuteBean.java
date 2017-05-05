package com.gentlehealthcare.mobilecare.net.bean;

import com.gentlehealthcare.mobilecare.UserInfo;

import java.io.UnsupportedEncodingException;

/**
 * 完成执行工作实体类
 * Created by ouyang on 2015/3/24.
 */
public class CompleteExecuteBean {
    private String patId;
    private String planOrderNo;//
    private String status;//status :1 表示正常  status :-1 表示异常
    private String completeDosage;//已执行剂量
    private String varianceCause;//结束原因代码
    private String varianceCauseDesc;//结束原因内容
    private String performDesc;
    private int performTask;
    private boolean result;
    private String msg;
    private int type=0;


    public String getCompleteDosage() {
        return completeDosage;
    }

    public void setCompleteDosage(String completeDosage) {
        this.completeDosage = completeDosage;
    }

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    @Override
    public String toString() {
        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append("?username="+ UserInfo.getUserName());
        stringBuffer.append("&patId="+getPatId());
        stringBuffer.append("&planOrderNo="+getPlanOrderNo());
       stringBuffer.append("&status="+getStatus());
        String performDesc="";
        try {
            performDesc=getPerformDesc()==null?"":java.net.URLEncoder.encode(getPerformDesc(),"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        stringBuffer.append("&performDesc="+performDesc);
        if (getStatus()!=null&&getStatus().equals("-1")){
            String varianceCause="";
            String varianceCauseDesc="";
            try {
                varianceCause=getVarianceCause()==null?"":java.net.URLEncoder.encode(getVarianceCause(),"utf-8");
                varianceCauseDesc=getVarianceCauseDesc()==null?"":java.net.URLEncoder.encode(getVarianceCauseDesc(),"utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            stringBuffer.append("&varianceCauseDesc="+varianceCauseDesc);
            stringBuffer.append("&varianceCause="+varianceCause);
            stringBuffer.append("&completeDosage="+getCompleteDosage());

        }
        return stringBuffer.toString();
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVarianceCause() {
        return varianceCause;
    }

    public void setVarianceCause(String varianceCause) {
        this.varianceCause = varianceCause;
    }

    public String getVarianceCauseDesc() {
        return varianceCauseDesc;
    }

    public void setVarianceCauseDesc(String varianceCauseDesc) {
        this.varianceCauseDesc = varianceCauseDesc;
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

    public int getPerformTask() {
        return performTask;
    }

    public void setPerformTask(int performTask) {
        this.performTask = performTask;
    }

    public String getPerformDesc() {
        return performDesc;
    }

    public void setPerformDesc(String performDesc) {
        this.performDesc = performDesc;
    }
}
