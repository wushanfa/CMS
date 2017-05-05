package com.gentlehealthcare.mobilecare.bean.orders;

import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.net.bean.LoadOrdersInfo;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * 加载医嘱实体类 Created by ouyang on 15/6/2.
 */
public class LoadOrdersBean implements Serializable {
    private String patId;// 病人ID

    private String startTime;// 计划执行时间

    private String status;// 医嘱状态
    private String groupId;
    private String planTime;
    private String orderType;
    private List<LoadOrdersInfo> subOrders;
    private String performStatus;
    private String result;
    private String orderText;
    private String wardCode;
    private String nurseInOperate;
    private String nurseEffect;
    private String varianceCause;
    private String varianceCauseDesc;
    private String eventEndTime;
    private String eventStartTime;
    private String planOrderNo;
    private String templateId;
    private String orderClass;
    private String relatedBarcode;

    @Override
    public String toString() {
        String planTime = "";
        String orderClass = "";
        String templateId = "";
        try {
            planTime = getPlanTime() == null ? "" : URLEncoder.encode(getPlanTime(), "utf-8");
            orderClass = getOrderClass() == null ? "" : URLEncoder.encode(getOrderClass(), "utf-8");
            templateId = getTemplateId() == null ? "" : URLEncoder.encode(getTemplateId(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "?username=" + UserInfo.getUserName() + "&patId=" + getPatId() + "&planTime=" + planTime +
                "&orderType=" + getOrderType() + "&status=" + getStatus() + "&orderText=" + getOrderText() +
                "&wardCode=" + wardCode + "&nurseInOperate=" + nurseInOperate + "&orderClass=" + orderClass +
                "&templateId=" + templateId;
    }

    public String getRelatedBarcode() {
        return relatedBarcode;
    }

    public void setRelatedBarcode(String relatedBarcode) {
        this.relatedBarcode = relatedBarcode;
    }

    public String getOrderClass() {
        return orderClass;
    }

    public void setOrderClass(String orderClass) {
        this.orderClass = orderClass;
    }

    public String getPatId() {
        return patId;
    }

    public void setPatId(String patId) {
        this.patId = patId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getPlanTime() {
        return planTime;
    }

    public void setPlanTime(String planTime) {
        this.planTime = planTime;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public List<LoadOrdersInfo> getSubOrders() {
        return subOrders;
    }

    public void setSubOrders(List<LoadOrdersInfo> subOrders) {
        this.subOrders = subOrders;
    }

    public String getPerformStatus() {
        return performStatus;
    }

    public void setPerformStatus(String performStatus) {
        this.performStatus = performStatus;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getOrderText() {
        return orderText;
    }

    public void setOrderText(String orderText) {
        this.orderText = orderText;
    }

    public String getWardCode() {
        return wardCode;
    }

    public void setWardCode(String wardCode) {
        this.wardCode = wardCode;
    }

    public String getNurseInOperate() {
        return nurseInOperate;
    }

    public void setNurseInOperate(String nurseInOperate) {
        this.nurseInOperate = nurseInOperate;
    }

    public String getNurseEffect() {
        return nurseEffect;
    }

    public void setNurseEffect(String nurseEffect) {
        this.nurseEffect = nurseEffect;
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

    public String getEventEndTime() {
        return eventEndTime;
    }

    public void setEventEndTime(String eventEndTime) {
        this.eventEndTime = eventEndTime;
    }

    public String getEventStartTime() {
        return eventStartTime;
    }

    public void setEventStartTime(String eventStartTime) {
        this.eventStartTime = eventStartTime;
    }

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
}
