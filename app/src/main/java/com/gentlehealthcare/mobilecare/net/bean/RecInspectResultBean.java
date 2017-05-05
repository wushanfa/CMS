package com.gentlehealthcare.mobilecare.net.bean;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

public class RecInspectResultBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 发送时参数
	 */
	private String username;
	private String patId;
	private String planOrderNO;
	private String itemCode;
	private String otherDesc;
	private String status;
	private String handleText;
	private String dosage;

	public String getBloodId() {
		return bloodId;
	}

	public void setBloodId(String bloodId) {
		this.bloodId = bloodId;
	}

	private String bloodId;


	/**
	 * 返回时参数
	 */
	private boolean result;// 是否成功
	private String msg;// 返回信息

	@Override
	public String toString() {
		String patId = "";
		String username = "";
		String planOrderNO = "";
		String itemCode = "";
		String otherDesc = "";
		String status = "";
		String handleText = "";
		String dosage = "";
		try {
			patId = getPatId() == null ? "" : java.net.URLEncoder.encode(
					getPatId(), "utf-8");
			username = getUsername() == null ? "" : java.net.URLEncoder.encode(
					getUsername(), "utf-8");
			planOrderNO = getPlanOrderNO() == null ? "" : java.net.URLEncoder
					.encode(getPlanOrderNO(), "utf-8");
			itemCode = getItemCode() == null ? "" : java.net.URLEncoder.encode(
					getItemCode(), "utf-8");
			otherDesc = getOtherDesc() == null ? "" : java.net.URLEncoder
					.encode(getOtherDesc(), "utf-8");
			status = getStatus() == null ? "" : java.net.URLEncoder.encode(
					getStatus(), "utf-8");
			handleText = getHandleText() == null ? "" : java.net.URLEncoder
					.encode(getHandleText(), "utf-8");
			dosage = getDosage() == null ? "" : java.net.URLEncoder.encode(
					getDosage(), "utf-8");

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "?patId=" + patId + "&username=" + username + "&planOrderNo="
				+ planOrderNO + "&itemCode=" + itemCode + "&otherDesc="
				+ otherDesc + "&status=" + status + "&handleText=" + handleText
				+ "&dosage=" + dosage+"&bloodId="+getBloodId();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPatId() {
		return patId;
	}

	public void setPatId(String patId) {
		this.patId = patId;
	}

	public String getPlanOrderNO() {
		return planOrderNO;
	}

	public void setPlanOrderNO(String planOrderNO) {
		this.planOrderNO = planOrderNO;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getOtherDesc() {
		return otherDesc;
	}

	public void setOtherDesc(String otherDesc) {
		this.otherDesc = otherDesc;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getHandleText() {
		return handleText;
	}

	public void setHandleText(String handleText) {
		this.handleText = handleText;
	}

	public String getDosage() {
		return dosage;
	}

	public void setDosage(String dosage) {
		this.dosage = dosage;
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

}
