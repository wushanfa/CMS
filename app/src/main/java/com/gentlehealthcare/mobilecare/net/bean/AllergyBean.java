package com.gentlehealthcare.mobilecare.net.bean;

import com.gentlehealthcare.mobilecare.UserInfo;

public class AllergyBean {
	private String itemName;
	private String inputCode;
	private String itemNo;

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getInputCode() {
		return inputCode;
	}

	public void setInputCode(String inputCode) {
		this.inputCode = inputCode;
	}

	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	@Override
	public String toString() {
		return "?logBy=" + UserInfo.getUserName();
	}
}
