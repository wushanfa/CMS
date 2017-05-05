package com.gentlehealthcare.mobilecare.net.bean;

import com.gentlehealthcare.mobilecare.UserInfo;

/**
 * 病人待执行任务
 * Created by ouyang on 2015-04-07.
 */
public class SyncPatPerformWork {
    private String patId;

    @Override
    public String toString() {
        return "?userName="+ UserInfo.getName()+"&patId="+getPatId();
    }

    public String getPatId() {
        return patId;
    }

    public void setPatId(String patId) {
        this.patId = patId;
    }
}
