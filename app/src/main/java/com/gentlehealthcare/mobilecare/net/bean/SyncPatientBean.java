package com.gentlehealthcare.mobilecare.net.bean;

import com.gentlehealthcare.mobilecare.UserInfo;

import java.io.Serializable;

/**
 * @author ouyang
 * @ClassName: SyncPatientBean
 * @Description: 同步 病人数据
 * @date 2015年3月6日 下午5:34:16
 */
public class SyncPatientBean implements Serializable {
    private String patId;//病人ID
    private String patCode;//病人编号
    private String visitId;//就诊次数
    private String name;//病人名称
    private String sex;//性别
    private String age;//年龄
    private String wardCode;//单元编号
    private String wardName;//单元名称
    private String nursingGrade;//护理等级
   // private List<TB_PatientMatters> nursingKeywords;//关键字
    private String bedLabel;//病床Lebel
    private String admissionDate;//入院时间
    private int performTask;//待执行工作数量
    private int executingTask;//执行中工作数量
    private int complitedTask;//已执行工作数量
    private String admissionDiagName;//入院诊断
    private String doctorInCharge;

    public String getPatBarcode() {
        return patBarcode;
    }

    public void setPatBarcode(String patBarcode) {
        this.patBarcode = patBarcode;
    }

    private String patBarcode;

    public String getDoctorInCharge() {
        return doctorInCharge;
    }

    public void setDoctorInCharge(String doctorInCharge) {
        this.doctorInCharge = doctorInCharge;
    }

    public SyncPatientBean() {
    }

//    public List<TB_PatientMatters> getNursingKeywords() {
//        return nursingKeywords;
//    }
//
//    public void setNursingKeywords(List<TB_PatientMatters> nursingKeywords) {
//        this.nursingKeywords = nursingKeywords;
//    }

    public String getWardCode() {
        return wardCode;
    }

    public void setWardCode(String wardCode) {
        this.wardCode = wardCode;
    }

    public String getBedLabel() {
        return bedLabel;
    }

    public void setBedLabel(String bedLabel) {
        this.bedLabel = bedLabel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getVisitId() {
        return visitId;
    }

    public void setVisitId(String visitId) {
        this.visitId = visitId;
    }

    public String getPatCode() {
        return patCode;
    }

    public void setPatCode(String patCode) {
        this.patCode = patCode;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }


    public String getNursingGrade() {
        return nursingGrade;
    }

    public void setNursingGrade(String nursingGrade) {
        this.nursingGrade = nursingGrade;
    }

    public String getPatId() {
        return patId;
    }

    public void setPatId(String patId) {
        this.patId = patId;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "?username=" + UserInfo.getUserName() + "&wardCode=" + UserInfo.getWardCode();

    }


    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(String admissionDate) {
        this.admissionDate = admissionDate;
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

    public int getComplitedTask() {
        return complitedTask;
    }

    public void setComplitedTask(int complitedTask) {
        this.complitedTask = complitedTask;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAdmissionDiagName() {
        return admissionDiagName;
    }

    public void setAdmissionDiagName(String admissionDiagName) {
        this.admissionDiagName = admissionDiagName;
    }
}
