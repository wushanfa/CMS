package com.gentlehealthcare.mobilecare.net.bean;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import com.gentlehealthcare.mobilecare.UserInfo;

public class ExecuteToOrgRecordBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 发送时参数
	 */

	private String performStatus;
	private String updateBy;
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

		String performStatus = "";
		String updateBy = "";
		String planOrderNo = "";
		try {
			updateBy = UserInfo.getUserName() == null ? ""
					: java.net.URLEncoder.encode(UserInfo.getUserName(),
							"utf-8");
			performStatus = getPerformStatus() == null ? ""
					: java.net.URLEncoder.encode(getPerformStatus(), "utf-8");
			planOrderNo = getPlanOrderNo() == null ? "" : java.net.URLEncoder
					.encode(getPlanOrderNo(), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "?performStatus=" + performStatus + "&updateBy=" + updateBy
				+ "&planOrderNo=" + planOrderNo;
	}

	public String getPerformStatus() {
		return performStatus;
	}

	public void setPerformStatus(String performStatus) {
		this.performStatus = performStatus;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public String getPlanOrderNo() {
		return planOrderNo;
	}

	public void setPlanOrderNo(String planOrderNo) {
		this.planOrderNo = planOrderNo;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

}
