package com.gentlehealthcare.mobilecare.net.bean;

/**
 * Created by Zyy on 2015/12/3.
 * 类说明：静脉给药不执行上传原因
 */
public class DontExcuteResonBean {

    @Override
    public String toString() {
        return "?patId=" + getPatId() + "&username=" + getUsername() + "&planOrderNo=" + getPlanOrderNo() + "&performDesc=" + getPerformDesc() + "&varianceCause=" + getVarianceCause()+ "&varianceCauseDesc=" + getVarianceCauseDesc();
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

    public String getPlanOrderNo() {
        return planOrderNo;
    }

    public void setPlanOrderNo(String planOrderNo) {
        this.planOrderNo = planOrderNo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String patId;// 病人id
    public String username;// 当前用户名
    public String planOrderNo;// 计划id
    public String performDesc; //结束原因
    public String varianceCause;// 变异原因代码
    public String varianceCauseDesc;// 变异原因内容
}
