package com.gentlehealthcare.mobilecare.net.bean;

import java.io.Serializable;


/**
 * 通知信息
 * @author zyy
 */
public class TipBean implements Serializable{
    
    /**
	 * 默认初始化序列ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * doctorInCharge : 王秋生
	 * createTime : 2016-01-26 15:19:37
	 * patCode : 0000021448
	 * itemNo : 1
	 * sex : 男
	 * bedLabel : 32
	 * bloodGroup : O/阳性
	 * patId : 40609
	 * noticeStartTime : 2016-01-26 15:34:37
	 * messageContent : 袁献华正在输血,2个单位的输A型（RH+）去白细胞红细胞,2单位，病人袁献华输血15分钟
	 * applyNo : 12345
	 * visitId : 1
	 * noticeClass : visit
	 * bloodCapacity : 2
	 * bloodId : 10203
	 * templateId : AA-5
	 * unit : 单位
	 * confirmStatus : 1
	 * createUser : 00328
	 * noticeId : 1a436d3bf6d4448fac96a65d436567f2
	 * wardCode : 0201023403
	 * age : 36
	 * patName : 袁献华
	 * relatedPlanOrderNo : 151103142244_53
	 */

	private String doctorInCharge;
	private String createTime;
	private String patCode;
	private int itemNo;
	private String sex;
	private String bedLabel;
	private String bloodGroup;
	private String patId;
	private String noticeStartTime;
	private String messageContent;
	private String applyNo;
	private int visitId;
	private String noticeClass;
	private int bloodCapacity;
	private String bloodId;
	private String templateId;
	private String unit;
	private String confirmStatus;
	private String createUser;
	private String noticeId;
	private String wardCode;
	private String age;
	private String patName;
	private String relatedPlanOrderNo;
	private String injectionTool;

	public String getInjectionTool() {
		return injectionTool;
	}

	public void setInjectionTool(String injectionTool) {
		this.injectionTool = injectionTool;
	}

	public String getSpeed() {
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	private String speed;

	public void setDoctorInCharge(String doctorInCharge) {
		this.doctorInCharge = doctorInCharge;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public void setPatCode(String patCode) {
		this.patCode = patCode;
	}

	public void setItemNo(int itemNo) {
		this.itemNo = itemNo;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public void setBedLabel(String bedLabel) {
		this.bedLabel = bedLabel;
	}

	public void setBloodGroup(String bloodGroup) {
		this.bloodGroup = bloodGroup;
	}

	public void setPatId(String patId) {
		this.patId = patId;
	}

	public void setNoticeStartTime(String noticeStartTime) {
		this.noticeStartTime = noticeStartTime;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}

	public void setApplyNo(String applyNo) {
		this.applyNo = applyNo;
	}

	public void setVisitId(int visitId) {
		this.visitId = visitId;
	}

	public void setNoticeClass(String noticeClass) {
		this.noticeClass = noticeClass;
	}

	public void setBloodCapacity(int bloodCapacity) {
		this.bloodCapacity = bloodCapacity;
	}

	public void setBloodId(String bloodId) {
		this.bloodId = bloodId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public void setConfirmStatus(String confirmStatus) {
		this.confirmStatus = confirmStatus;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}

	public void setWardCode(String wardCode) {
		this.wardCode = wardCode;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public void setPatName(String patName) {
		this.patName = patName;
	}

	public void setRelatedPlanOrderNo(String relatedPlanOrderNo) {
		this.relatedPlanOrderNo = relatedPlanOrderNo;
	}

	public String getDoctorInCharge() {
		return doctorInCharge;
	}

	public String getCreateTime() {
		return createTime;
	}

	public String getPatCode() {
		return patCode;
	}

	public int getItemNo() {
		return itemNo;
	}

	public String getSex() {
		return sex;
	}

	public String getBedLabel() {
		return bedLabel;
	}

	public String getBloodGroup() {
		return bloodGroup;
	}

	public String getPatId() {
		return patId;
	}

	public String getNoticeStartTime() {
		return noticeStartTime;
	}

	public String getMessageContent() {
		return messageContent;
	}

	public String getApplyNo() {
		return applyNo;
	}

	public int getVisitId() {
		return visitId;
	}

	public String getNoticeClass() {
		return noticeClass;
	}

	public int getBloodCapacity() {
		return bloodCapacity;
	}

	public String getBloodId() {
		return bloodId;
	}

	public String getTemplateId() {
		return templateId;
	}

	public String getUnit() {
		return unit;
	}

	public String getConfirmStatus() {
		return confirmStatus;
	}

	public String getCreateUser() {
		return createUser;
	}

	public String getNoticeId() {
		return noticeId;
	}

	public String getWardCode() {
		return wardCode;
	}

	public String getAge() {
		return age;
	}

	public String getPatName() {
		return patName;
	}

	public String getRelatedPlanOrderNo() {
		return relatedPlanOrderNo;
	}
}
