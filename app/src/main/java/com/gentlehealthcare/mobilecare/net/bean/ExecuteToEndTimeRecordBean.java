package com.gentlehealthcare.mobilecare.net.bean;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import com.gentlehealthcare.mobilecare.UserInfo;

public class ExecuteToEndTimeRecordBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 发送时参数
	 */
	private String wardCode;
	private String nurseInOperate;
	private String performStatus;
	private String performResult;
	private String updateBy;
	private String planId;
	private String patId;
	private String eventEndTime;
	private String planOrderNo;

	/**
	 * 返回时参数
	 */
	private String result;

	/**
	 * 请求url字符串
	 */
	@Override
	public String toString() {

		String wardCode = "";
		String nurseInOperate = "";
		String performStatus = "";
		String performResult = "";
		String updateBy = "";
		String planId = "";
		String patId = "";
		String eventEndTime = "";
		String planOrderNo = "";
		try {
			eventEndTime = getEventEndTime() == null ? "" : java.net.URLEncoder
					.encode(getEventEndTime(), "utf-8");
			patId = getPatId() == null ? "" : java.net.URLEncoder.encode(
					getPatId(), "utf-8");
			planId = getPlanId() == null ? "" : java.net.URLEncoder.encode(
					getPlanId(), "utf-8");
			updateBy = UserInfo.getUserName() == null ? ""
					: java.net.URLEncoder.encode(UserInfo.getUserName(),
							"utf-8");
			performResult = getPerformResult() == null ? ""
					: java.net.URLEncoder.encode(getPerformResult(), "utf-8");
			performStatus = getPerformStatus() == null ? ""
					: java.net.URLEncoder.encode(getPerformStatus(), "utf-8");
			nurseInOperate = UserInfo.getUserName() == null ? ""
					: java.net.URLEncoder.encode(UserInfo.getUserName(),
							"utf-8");
			wardCode = UserInfo.getWardCode() == null ? ""
					: java.net.URLEncoder.encode(UserInfo.getWardCode(),
							"utf-8");
			planOrderNo = getPlanOrderNo() == null ? "" : java.net.URLEncoder
					.encode(getPlanOrderNo(), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "?wardCode=" + wardCode + "&nurseInOperate=" + nurseInOperate
				+ "&performStatus=" + performStatus + "&performResult="
				+ performResult + "&updateBy=" + updateBy + "&planId=" + planId
				+ "&patId=" + patId + "&eventEndTime=" + eventEndTime
				+ "&planOrderNo=" + planOrderNo;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getPerformStatus() {
		return performStatus;
	}

	public void setPerformStatus(String performStatus) {
		this.performStatus = performStatus;
	}

	public String getPerformResult() {
		return performResult;
	}

	public void setPerformResult(String performResult) {
		this.performResult = performResult;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getWardCode() {
		return wardCode;
	}

	public void setWardCode(String wardCode) {
		this.wardCode = wardCode;
	}

	public String getNurseInOperate() {
		return nurseInOperate;
	}

	public void setNurseInOperate(String nurseInOperate) {
		this.nurseInOperate = nurseInOperate;
	}

	public String getPatId() {
		return patId;
	}

	public void setPatId(String patId) {
		this.patId = patId;
	}

	public String getEventEndTime() {
		return eventEndTime;
	}

	public void setEventEndTime(String eventEndTime) {
		this.eventEndTime = eventEndTime;
	}

	public String getPlanOrderNo() {
		return planOrderNo;
	}

	public void setPlanOrderNo(String planOrderNo) {
		this.planOrderNo = planOrderNo;
	}

}
