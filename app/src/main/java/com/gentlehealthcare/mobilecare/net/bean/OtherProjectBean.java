package com.gentlehealthcare.mobilecare.net.bean;

import com.gentlehealthcare.mobilecare.UserInfo;

public class OtherProjectBean {
	private String itemName;
	private String itemCode;
	private String wardType;
	private String itemType;
	private String itemUnit;
	private String itemUnit2;

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

	public String getWardType() {
		return wardType;
	}

	public void setWardType(String wardType) {
		this.wardType = wardType;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getItemUnit() {
		return itemUnit;
	}

	public void setItemUnit(String itemUnit) {
		this.itemUnit = itemUnit;
	}

	public String getItemUnit2() {
		return itemUnit2;
	}

	public void setItemUnit2(String itemUnit2) {
		this.itemUnit2 = itemUnit2;
	}

	@Override
	public String toString() {
		return "?logBy=" + UserInfo.getUserName();
	}
}
