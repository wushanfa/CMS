package com.gentlehealthcare.mobilecare.bean;

/**
 * Created by Administrator on 2017/6/2.
 */

public class GradedCareEvaluationBean {
    public String time;
    public String type;
    public String name;
    public String measure;

    public GradedCareEvaluationBean(String time, String type, String name, String measure) {
        this.time = time;
        this.type = type;
        this.name = name;
        this.measure = measure;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }
}
