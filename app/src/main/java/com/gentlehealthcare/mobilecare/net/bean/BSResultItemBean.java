package com.gentlehealthcare.mobilecare.net.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhiwei on 2015/11/27.
 */
public class BSResultItemBean implements Serializable {
    private boolean result;
    private String msg;
    private List<BloodSugarItemBean> data;

    @Override
    public String toString() {
        return "";
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

    public List<BloodSugarItemBean> getData() {
        return data;
    }

    public void setData(List<BloodSugarItemBean> data) {
        this.data = data;
    }
}
