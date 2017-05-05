package com.gentlehealthcare.mobilecare.net.bean;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import com.gentlehealthcare.mobilecare.UserInfo;

/**
 * Created by ouyang on 2015/3/25.
 */
public class PatMajorInfoBean {
    private String patId;
    private String templateId;
    private List<OrderItemBean> orders;
    private String performStatus;
    private int type;
    private String searchTime;

    @Override
    public String toString() {

        String searchTime = "";

        try {
            searchTime = getSearchTime() == null ? "" : URLEncoder.encode(getSearchTime(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return "?username=" + UserInfo.getUserName() + "&patId=" + getPatId()
                + "&searchTime=" + searchTime + "&performStatus="
                + getPerformStatus() + "&templateId=" + getTemplateId();
    }

    public String getPatId() {
        return patId;
    }

    public void setPatId(String patId) {
        this.patId = patId;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public List<OrderItemBean> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderItemBean> orders) {
        this.orders = orders;
    }

    public String getPerformStatus() {
        return performStatus;
    }

    public void setPerformStatus(String performStatus) {
        this.performStatus = performStatus;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSearchTime() {
        return searchTime;
    }

    public void setSearchTime(String searchTime) {
        this.searchTime = searchTime;
    }
}
