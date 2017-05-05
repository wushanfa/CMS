package com.gentlehealthcare.mobilecare.net.bean;

import java.io.UnsupportedEncodingException;


public class LoadThreeTestParamBean {

	private String patId;
	private String recordingTime;

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

	@Override
	public String toString() {
		String recordingTime="";
        try {
        	recordingTime=getRecordingTime()==null?"":java.net.URLEncoder.encode(getRecordingTime(),"utf-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
		return "?recordingTime=" + recordingTime + "&patId=" + patId;
	}

}
