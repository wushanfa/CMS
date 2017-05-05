package com.gentlehealthcare.mobilecare.net.bean;

import java.io.Serializable;

public class TPRRecordBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 发送时参数
	 */
	private String username;//用户名
	private String patId;//病人id
	private String planOrderNo;//医嘱执行任务id
	private String temperature;//体温
	private String pulse;//脉搏
	private String respire;//呼吸

	/**
	 * 返回时参数
	 */
	private boolean result;//是否成功
	private String msg;//返回信息

	/**
	 * 请求url字符串
	 */
	@Override
	public String toString() {
		return "?username=" + username + "&patId=" + patId + "&planOrderNo="
				+ planOrderNo + "&temperature=" + temperature + "&pulse="
				+ pulse + "&respire=" + respire;
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

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	public String getPulse() {
		return pulse;
	}

	public void setPulse(String pulse) {
		this.pulse = pulse;
	}

	public String getRespire() {
		return respire;
	}

	public void setRespire(String respire) {
		this.respire = respire;
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
