package com.gentlehealthcare.mobilecare.net.bean;

import java.util.List;

/**
 * 
 * @ClassName: SystemInfoBean 
 * @Description: 系统信息
 * @author ouyang
 * @date 2015年3月6日 下午4:19:22 
 *
 */
public class SystemInfoBean {
	private String appCopyright;//版权
	private String appName;//app 名称
	private String hospitalName;//医院名称
	private String appCode;//app 编号
	private List<SystemAttentionInfo> attentions;//注意事项
	public String getAppCopyright() {
		return appCopyright;
	}
	public void setAppCopyright(String appCopyright) {
		this.appCopyright = appCopyright;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getHospitalName() {
		return hospitalName;
	}
	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}
	public String getAppCode() {
		return appCode;
	}
	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}
	
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "";
	}
	public List<SystemAttentionInfo> getAttentions() {
		return attentions;
	}
	public void setAttentions(List<SystemAttentionInfo> attentions) {
		this.attentions = attentions;
	}
	
	

}
