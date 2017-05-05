package com.gentlehealthcare.mobilecare.net.bean;

import java.io.UnsupportedEncodingException;

import com.gentlehealthcare.mobilecare.UserInfo;

/**
 * 记录PIO实体类 Created by ouyang on 15/7/3.
 */
public class DeleteAndModPioBean {
	private String patId;
	private String problemCode;// 问题编号
	private String problemName;// 问题
	private String itemNo;
	private String pioId;
	private String result;
	private String logBy;

	public String getProblemName() {
		return problemName;
	}

	public void setProblemName(String problemName) {
		this.problemName = problemName;
	}

	public String getProblemCode() {
		return problemCode;
	}

	public void setProblemCode(String problemCode) {
		this.problemCode = problemCode;
	}

	public String getPatId() {
		return patId;
	}

	public void setPatId(String patId) {
		this.patId = patId;
	}

	@Override
	public String toString() {
		String problemName = "";
		String problemCode = "";
		String itemNo = "";
		String pioId = "";
		String logBy = "";

		try {
			problemName = getProblemName() == null ? "thisIsNull" : java.net.URLEncoder
					.encode(getProblemName(), "utf-8");
			problemCode = getProblemCode() == null ? "thisIsNull" : java.net.URLEncoder
					.encode(getProblemCode(), "utf-8");
			itemNo = getItemNo() == null ? "" : java.net.URLEncoder.encode(
					getItemNo(), "utf-8");
			pioId = getPioId() == null ? "" : java.net.URLEncoder.encode(
					getPioId(), "utf-8");
			logBy = getLogBy() == null ? "" : java.net.URLEncoder.encode(
					getLogBy(), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "?username=" + UserInfo.getUserName() + "&patId=" + getPatId()
				+ "&problemCode=" + problemCode + "&problemName=" + problemName
				+ "&logBy=" + logBy + "&pioId=" + pioId + "&itemNo=" + itemNo;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getLogBy() {
		return logBy;
	}

	public void setLogBy(String logBy) {
		this.logBy = logBy;
	}

	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	public String getPioId() {
		return pioId;
	}

	public void setPioId(String pioId) {
		this.pioId = pioId;
	}

}
