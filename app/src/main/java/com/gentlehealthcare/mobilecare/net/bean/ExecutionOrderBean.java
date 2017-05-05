package com.gentlehealthcare.mobilecare.net.bean;

import java.io.UnsupportedEncodingException;

import com.gentlehealthcare.mobilecare.UserInfo;

/**
 * 执行医嘱 Created by ouyang on 15/6/2.
 */
public class ExecutionOrderBean {
	private String patId;
	private String planId;
	private String wardCode;
	private String nurseInOperate;
	private String planOrderNo;
	
	private String result;

	public String getPatId() {
		return patId;
	}

	public void setPatId(String patId) {
		this.patId = patId;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	@Override
	public String toString() {
		String wardCode = "";
		String nurseInOperate = "";
		String updateBy = "";
		String planId = "";
		String patId = "";
		String planOrderNo = "";
		try {
			patId = getPatId() == null ? "" : java.net.URLEncoder.encode(
					getPatId(), "utf-8");
			planId = getPlanId() == null ? "" : java.net.URLEncoder.encode(
					getPlanId(), "utf-8");
			updateBy = UserInfo.getUserName() == null ? ""
					: java.net.URLEncoder.encode(UserInfo.getUserName(),
							"utf-8");
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
		return "?username=" + updateBy + "&patId=" + patId + "&planId="
				+ planId + "&wardCode=" + wardCode + "&nurseInOperate="
				+ nurseInOperate + "&planOrderNo=" + planOrderNo;

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
