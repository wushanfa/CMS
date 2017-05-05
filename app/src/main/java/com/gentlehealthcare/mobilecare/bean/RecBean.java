package com.gentlehealthcare.mobilecare.bean;

import com.gentlehealthcare.mobilecare.tool.UrlTool;

import java.io.Serializable;

/**
 * Created by Zyy on 2016/8/3.
 * 类说明：对应cp_pat_nursing_rec
 */

public class RecBean implements Serializable {
    String planOrderNo ;
    String logBy ;
    String nurseInOperate ;
    String wardCode ;
    String templateId ;
    String performDesc ;
    String performType ;
    @Override
    public String toString() {
        return getPatId()+"&planOrderNo="+UrlTool.transWord(planOrderNo)+"&logBy="+UrlTool.transWord(logBy)+"&nurseInOperate="+nurseInOperate+"&wardCode="+wardCode+"&performDesc="+ UrlTool.transWord(performDesc)+"&performType="+UrlTool.transWord(performType);
    }

    String patId ;

    public String getPlanOrderNo() {
        return planOrderNo;
    }

    public void setPlanOrderNo(String planOrderNo) {
        this.planOrderNo = planOrderNo;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getWardCode() {
        return wardCode;
    }

    public void setWardCode(String wardCode) {
        this.wardCode = wardCode;
    }

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
        this.performDesc = performDesc;
    }

    public String getPatId() {
        return patId;
    }

    public void setPatId(String patId) {
        this.patId = patId;
    }

    public String getNurseInOperate() {
        return nurseInOperate;
    }

    public void setNurseInOperate(String nurseInOperate) {
        this.nurseInOperate = nurseInOperate;
    }

    public String getLogBy() {
        return logBy;
    }

    public void setLogBy(String logBy) {
        this.logBy = logBy;
    }


}
