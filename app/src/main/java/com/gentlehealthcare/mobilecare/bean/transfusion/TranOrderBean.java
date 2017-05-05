package com.gentlehealthcare.mobilecare.bean.transfusion;

/**
 * Created by zhiwei on 2016/5/23.
 */
public class TranOrderBean {

    /**
     * reqId : 20160317026
     * planStartTime : 2016-05-23 08:00:00
     * orderText : 备去白细胞悬浮红细胞[AB型Rh阳性(+)]
     */

    private String reqId;
    private String planStartTime;
    private String orderText;

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public String getPlanStartTime() {
        return planStartTime;
    }

    public void setPlanStartTime(String planStartTime) {
        this.planStartTime = planStartTime;
    }

    public String getOrderText() {
        return orderText;
    }

    public void setOrderText(String orderText) {
        this.orderText = orderText;
    }
}
