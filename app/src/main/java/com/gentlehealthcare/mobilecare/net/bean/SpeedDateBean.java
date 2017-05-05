package com.gentlehealthcare.mobilecare.net.bean;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

public class SpeedDateBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 发送时参数
	 */
	private String username;// 用户名
	private String patId;// 病人id
	private String planOrderNo;// 医嘱执行任务id
	private String speed;// 滴速

	/**
	 * 返回时参数
	 */
	private boolean result;// 是否成功
	private String msg;// 返回信息

	@Override
	public String toString() {

		String username = "";
		String patId = "";
		String planOrderNo = "";
		String speed = "";
		try {
			username = getUsername() == null ? "" : java.net.URLEncoder.encode(
					getUsername(), "utf-8");
			patId = getPatId() == null ? "" : java.net.URLEncoder.encode(
					getPatId(), "utf-8");
			planOrderNo = getPlanOrderNo() == null ? "" : java.net.URLEncoder
					.encode(getPlanOrderNo(), "utf-8");
			speed = getSpeed() == null ? "" : java.net.URLEncoder.encode(
					getSpeed(), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "?username=" + username + "&patId=" + patId + "&planOrderNo="
				+ planOrderNo + "&speed=" + speed;
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

	public String getPlanOrderNo() {
		return planOrderNo;
	}

	public void setPlanOrderNo(String planOrderNo) {
		this.planOrderNo = planOrderNo;
	}

	public String getSpeed() {
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
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
