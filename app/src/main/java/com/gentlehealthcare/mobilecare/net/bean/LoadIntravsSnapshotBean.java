package com.gentlehealthcare.mobilecare.net.bean;

import com.gentlehealthcare.mobilecare.UserInfo;

/**
 * Created by ouyang on 15/8/27.
 */
public class LoadIntravsSnapshotBean extends LoadInspectionTimeBean {
    private String patId;
    private int performTask;
    private int executingTask;

    public String getPatId() {
        return patId;
    }

    public void setPatId(String patId) {
        this.patId = patId;
    }

    @Override
    public String toString() {
        return "?username="+ UserInfo.getUserName()+"&patId="+getPatId();
    }

    public int getPerformTask() {
        return performTask;
    }

    public void setPerformTask(int performTask) {
        this.performTask = performTask;
    }

    public int getExecutingTask() {
        return executingTask;
    }

    public void setExecutingTask(int executingTask) {
        this.executingTask = executingTask;
    }
}
