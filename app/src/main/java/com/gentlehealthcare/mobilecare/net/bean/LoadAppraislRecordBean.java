package com.gentlehealthcare.mobilecare.net.bean;

import com.gentlehealthcare.mobilecare.UserInfo;

import java.io.Serializable;

public class LoadAppraislRecordBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 返回时参数
	 */
	private String itemName;
	private String itemCode;

	/**
	 * 请求url字符串
	 */
	@Override
	public String toString() {
		return "?username="+ UserInfo.getUserName();
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	

}
