package com.gentlehealthcare.mobilecare.net.bean;

import java.io.Serializable;

public class LoadICUAItemBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String itemCode;
	private String itemName;

	@Override
	public String toString() {
		return "";
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

}
