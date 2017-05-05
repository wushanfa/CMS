package com.gentlehealthcare.mobilecare.bean;

/**
 * PIO记录实体类
 * Created by ouyang on 15/6/2.
 */
public class PIORecordsBean {
    private String problem;//问题
    private String target;//目标
    private String measures;//措施
    private String cause;//致因
    private String effect;//效果
    private String time;//时间
    private String estimatetime;//评价时间
    private String person;//评价人

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getMeasures() {
        return measures;
    }

    public void setMeasures(String measures) {
        this.measures = measures;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getEstimatetime() {
        return estimatetime;
    }

    public void setEstimatetime(String estimatetime) {
        this.estimatetime = estimatetime;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }
}
