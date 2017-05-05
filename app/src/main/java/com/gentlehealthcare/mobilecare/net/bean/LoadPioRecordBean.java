package com.gentlehealthcare.mobilecare.net.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ouyang on 15/7/5.
 */
public class LoadPioRecordBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String patCode;
	private String patId;
	private String logTim1;
	private String logBy2;
	private String pioId;
	private String logTime2;
	private String problemCode;
	private String problemName;
	private String logBy;
	private List<PioItemInfo> target;
	private List<PioItemInfo> Measure;
	private List<PioItemInfo> causes;
	private List<PioItemInfo> appraisal;


	public String getPatCode() {
		return patCode;
	}

	public void setPatCode(String patCode) {
		this.patCode = patCode;
	}

	public String getPatId() {
		return patId;
	}

	public void setPatId(String patId) {
		this.patId = patId;
	}

	public String getLogBy2() {
		return logBy2;
	}

	public void setLogBy2(String logBy2) {
		this.logBy2 = logBy2;
	}

	public String getPioId() {
		return pioId;
	}

	public void setPioId(String pioId) {
		this.pioId = pioId;
	}

	public String getLogTime2() {
		return logTime2;
	}

	public void setLogTime2(String logTime2) {
		this.logTime2 = logTime2;
	}

	public String getProblemCode() {
		return problemCode;
	}

	public void setProblemCode(String problemCode) {
		this.problemCode = problemCode;
	}

	public String getProblemName() {
		return problemName;
	}

	public void setProblemName(String problemName) {
		this.problemName = problemName;
	}

	public String getLogBy() {
		return logBy;
	}

	public void setLogBy(String logBy) {
		this.logBy = logBy;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getLogTim1() {
		return logTim1;
	}

	public void setLogTim1(String logTim1) {
		this.logTim1 = logTim1;
	}

	public List<PioItemInfo> getTarget() {
		return target;
	}

	public void setTarget(List<PioItemInfo> target) {
		this.target = target;
	}

	public List<PioItemInfo> getMeasure() {
		return Measure;
	}

	public void setMeasure(List<PioItemInfo> measure) {
		Measure = measure;
	}

	public List<PioItemInfo> getCauses() {
		return causes;
	}

	public void setCauses(List<PioItemInfo> causes) {
		this.causes = causes;
	}

	public List<PioItemInfo> getAppraisal() {
		return appraisal;
	}

	public void setAppraisal(List<PioItemInfo> appraisal) {
		this.appraisal = appraisal;
	}


}
