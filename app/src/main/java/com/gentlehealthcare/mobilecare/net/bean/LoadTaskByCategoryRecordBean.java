package com.gentlehealthcare.mobilecare.net.bean;

import com.gentlehealthcare.mobilecare.UserInfo;

/**
 * Created by ouyang on 15/9/2.
 */
public class LoadTaskByCategoryRecordBean {
    private String templateId;
    private boolean result;
    private String msg;
    private String performTask;
    private String executingTask;
    private String completedTask;

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
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

    public String getPerformTask() {
        return performTask;
    }

    public void setPerformTask(String performTask) {
        this.performTask = performTask;
    }

    public String getExecutingTask() {
        return executingTask;
    }

    public void setExecutingTask(String executingTask) {
        this.executingTask = executingTask;
    }

    public String getCompletedTask() {
        return completedTask;
    }

    public void setCompletedTask(String completedTask) {
        this.completedTask = completedTask;
    }

    @Override
    public String toString() {
        return "?username="+ UserInfo.getUserName()+"&templateId="+getTemplateId();
    }
}
