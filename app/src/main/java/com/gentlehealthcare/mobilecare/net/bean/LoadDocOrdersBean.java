package com.gentlehealthcare.mobilecare.net.bean;

import java.util.List;

/**
 * Created by ouyang on 2015/6/18.
 */
public class LoadDocOrdersBean {
    private String startTime;
    private List<LoadOrdersBean> orders;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public List<LoadOrdersBean> getOrders() {
        return orders;
    }

    public void setOrders(List<LoadOrdersBean> orders) {
        this.orders = orders;
    }
}
