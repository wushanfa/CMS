package com.gentlehealthcare.mobilecare.net.bean;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

/**
 * 
 * @author dzw
 * 
 */
public class SaveThreeTestBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String patId;
	private String recordingTime;
	private String vitalSigns;
	private String vitalSignsValues;
	private String units;
	private String examMethod;
	private String logBy;
	private String vitalSignsValues2;
	private String units2;
	private String result;

	@Override
	public String toString() {
		String patId = "";
		String recordingTime = "";
		String vitalSigns = "";
		String vitalSignsValues = "";
		String units = "";
		String examMethod = "";
		String logBy = "";
		String vitalSignsValues2 = "";
		String units2 = "";
		try {
			recordingTime = getRecordingTime() == null ? "thisIsNull"
					: java.net.URLEncoder.encode(getRecordingTime(), "utf-8");
			patId = getPatId() == null ? "thisIsNull" : java.net.URLEncoder
					.encode(getPatId(), "utf-8");
			vitalSigns = getVitalSigns() == null ? "thisIsNull"
					: java.net.URLEncoder.encode(getVitalSigns(), "utf-8");
			vitalSignsValues = getVitalSignsValues() == null ? "thisIsNull"
					: java.net.URLEncoder
							.encode(getVitalSignsValues(), "utf-8");
			units = getUnits() == null ? "thisIsNull" : java.net.URLEncoder
					.encode(getUnits(), "utf-8");
			examMethod = getExamMethod() == null ? "thisIsNull"
					: java.net.URLEncoder.encode(getExamMethod(), "utf-8");
			logBy = getLogBy() == null ? "thisIsNull" : java.net.URLEncoder
					.encode(getLogBy(), "utf-8");
			vitalSignsValues2 = getVitalSignsValues2() == null ? "thisIsNull"
					: java.net.URLEncoder.encode(getVitalSignsValues2(),
							"utf-8");
			units2 = getUnits2() == null ? "thisIsNull" : java.net.URLEncoder
					.encode(getUnits2(), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "?recordingTime=" + recordingTime + "&patId=" + patId
				+ "&vitalSigns=" + vitalSigns + "&vitalSignsValues="
				+ vitalSignsValues + "&units=" + units + "&examMethod="
				+ examMethod + "&logBy=" + logBy + "&vitalSignsValues2="
				+ vitalSignsValues2 + "&units2=" + units2;
	}

	public String getPatId() {
		return patId;
	}

	public void setPatId(String patId) {
		this.patId = patId;
	}

	public String getRecordingTime() {
		return recordingTime;
	}

	public void setRecordingTime(String recordingTime) {
		this.recordingTime = recordingTime;
	}

	public String getVitalSigns() {
		return vitalSigns;
	}

	public void setVitalSigns(String vitalSigns) {
		this.vitalSigns = vitalSigns;
	}

	public String getVitalSignsValues() {
		return vitalSignsValues;
	}

	public void setVitalSignsValues(String vitalSignsValues) {
		this.vitalSignsValues = vitalSignsValues;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public String getExamMethod() {
		return examMethod;
	}

	public void setExamMethod(String examMethod) {
		this.examMethod = examMethod;
	}

	public String getLogBy() {
		return logBy;
	}

	public void setLogBy(String logBy) {
		this.logBy = logBy;
	}

	public String getVitalSignsValues2() {
		return vitalSignsValues2;
	}

	public void setVitalSignsValues2(String vitalSignsValues2) {
		this.vitalSignsValues2 = vitalSignsValues2;
	}

	public String getUnits2() {
		return units2;
	}

	public void setUnits2(String units2) {
		this.units2 = units2;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

}
