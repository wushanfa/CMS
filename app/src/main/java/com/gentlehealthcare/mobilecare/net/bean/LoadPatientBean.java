package com.gentlehealthcare.mobilecare.net.bean;

import com.gentlehealthcare.mobilecare.UserInfo;

/**
 * Created by ouyang on 15/9/1.
 */
public class LoadPatientBean {
    private String patId;
    private String patCode;
    private String visitId;
    private String name;//年纪
    private String sex;//性别
    private String age;//年龄
    private String wardCode;
    private String wardName;
    private String doctorInCharge;//主治医生
    private String nursingGrade;//护理等级
    private String nursingKeyWords;//关键字
    private String bedLabel;//床编号
    private String admissionDate;//入院时间
    private String performTask;//待执行数量
    private String executingTask;//执行中数量
    private String complitedTask;//已执行数量

    public String getPatId() {
        return patId;
    }

    public void setPatId(String patId) {
        this.patId = patId;
    }

    public String getPatCode() {
        return patCode;
    }

    public void setPatCode(String patCode) {
        this.patCode = patCode;
    }

    public String getVisitId() {
        return visitId;
    }

    public void setVisitId(String visitId) {
        this.visitId = visitId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getWardCode() {
        return wardCode;
    }

    public void setWardCode(String wardCode) {
        this.wardCode = wardCode;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

    public String getDoctorInCharge() {
        return doctorInCharge;
    }

    public void setDoctorInCharge(String doctorInCharge) {
        this.doctorInCharge = doctorInCharge;
    }

    public String getNursingGrade() {
        return nursingGrade;
    }

    public void setNursingGrade(String nursingGrade) {
        this.nursingGrade = nursingGrade;
    }

    public String getNursingKeyWords() {
        return nursingKeyWords;
    }

    public void setNursingKeyWords(String nursingKeyWords) {
        this.nursingKeyWords = nursingKeyWords;
    }

    public String getBedLabel() {
        return bedLabel;
    }

    public void setBedLabel(String bedLabel) {
        this.bedLabel = bedLabel;
    }

    public String getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(String admissionDate) {
        this.admissionDate = admissionDate;
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

    public String getComplitedTask() {
        return complitedTask;
    }

    public void setComplitedTask(String complitedTask) {
        this.complitedTask = complitedTask;
    }

    @Override
    public String toString() {
        return "?username="+ UserInfo.getUserName()+"&patId="+getPatId();
    }
}
