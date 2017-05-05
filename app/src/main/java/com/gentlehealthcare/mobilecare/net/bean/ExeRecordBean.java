package com.gentlehealthcare.mobilecare.net.bean;

import java.io.Serializable;

import com.gentlehealthcare.mobilecare.UserInfo;

public class ExeRecordBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String username;

	/**
	 * 返回时参数
	 */
	private String itemCode;
	private String itemName;

	/**
	 * 请求url字符串
	 */
	@Override
	public String toString() {
		return "?username="+UserInfo.getUserName();
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
}
