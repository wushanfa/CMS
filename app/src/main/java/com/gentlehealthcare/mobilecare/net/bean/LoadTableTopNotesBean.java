package com.gentlehealthcare.mobilecare.net.bean;

import com.gentlehealthcare.mobilecare.UserInfo;

public class LoadTableTopNotesBean {
	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

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

	private String itemNo;
	private String itemName;
	private String inputCode;

	@Override
	public String toString() {

		return "?username=" + UserInfo.getUserName() + "&wardCode="
				+ UserInfo.getWardCode();
	}
}
