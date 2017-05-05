package com.gentlehealthcare.mobilecare.net.bean;

import com.gentlehealthcare.mobilecare.UserInfo;

import java.io.UnsupportedEncodingException;

/**
 * Created by ouyang on 15/7/5.
 */
public class RecordAppraisalBean {
	private String patId;
	private String pioId;
	private String appraisal;
	private String appraisalCode;

	public String getAppraisal() {
		return appraisal;
	}

	public void setAppraisal(String appraisal) {
		this.appraisal = appraisal;
	}

	public String getPioId() {
		return pioId;
	}

	public void setPioId(String pioId) {
		this.pioId = pioId;
	}

	public String getPatId() {
		return patId;
	}

	public void setPatId(String patId) {
		this.patId = patId;
	}

	@Override
	public String toString() {
		String appraisal = "";
		try {
			appraisal = getAppraisal() == null ? "" : java.net.URLEncoder
					.encode(getAppraisal(), "utf-8");
			appraisalCode = getAppraisalCode() == null ? ""
					: java.net.URLEncoder.encode(getAppraisalCode(), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "?username=" + UserInfo.getUserName() + "&patId=" + getPatId()
				+ "&pioId=" + getPioId() + "&appraisal=" + appraisal
				+ "&appraisalCode=" + appraisalCode;
	}

	public String getAppraisalCode() {
		return appraisalCode;
	}

	public void setAppraisalCode(String appraisalCode) {
		this.appraisalCode = appraisalCode;
	}
}
