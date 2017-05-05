package com.gentlehealthcare.mobilecare.net.bean;

import com.gentlehealthcare.mobilecare.UserInfo;

public class YinLiuBean {

	private String itemCode;
	private String itemName;

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

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "?username=" + UserInfo.getUserName() + "&wardCode="
				+ UserInfo.getWardCode();

	}

}
