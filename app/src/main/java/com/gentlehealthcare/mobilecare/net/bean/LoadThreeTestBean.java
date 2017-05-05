package com.gentlehealthcare.mobilecare.net.bean;

import java.io.Serializable;

/**
 * 
 * @author dzw
 * 
 */
public class LoadThreeTestBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String vsId;
	private String vitalSigns;
	private String vitalSignsValues;
	private String valueType;
	private String units;
	private String examMethod;
	private String nurseAttr;
	private String logBy;
	private String logTime;
	private String vitalSignsValues2;
	private String units2;
	private String itemCode;
	private String recordingTime;

	public String getVitalSigns() {
		return vitalSigns;
	}

	public void setVitalSigns(String vitalSigns) {
		this.vitalSigns = vitalSigns;
	}

	public String getVsId() {
		return vsId;
	}

	public void setVsId(String vsId) {
		this.vsId = vsId;
	}

	public String getVitalSignsValues() {
		return vitalSignsValues;
	}

	public void setVitalSignsValues(String vitalSignsValues) {
		this.vitalSignsValues = vitalSignsValues;
	}

	public String getValueType() {
		return valueType;
	}

	public void setValueType(String valueType) {
		this.valueType = valueType;
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

	public String getNurseAttr() {
		return nurseAttr;
	}

	public void setNurseAttr(String nurseAttr) {
		this.nurseAttr = nurseAttr;
	}

	public String getLogBy() {
		return logBy;
	}

	public void setLogBy(String logBy) {
		this.logBy = logBy;
	}

	public String getLogTime() {
		return logTime;
	}

	public void setLogTime(String logTime) {
		this.logTime = logTime;
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

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getRecordingTime() {
		return recordingTime;
	}

	public void setRecordingTime(String recordingTime) {
		this.recordingTime = recordingTime;
	}

}
