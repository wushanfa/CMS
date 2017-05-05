package com.gentlehealthcare.mobilecare.bean.sys;

import java.io.Serializable;

/**
 * Created by Zyy on 2016/5/23.
 * 类说明：
 */
public class BarcodeDict implements Serializable{
	
	public String getRegularExp() {
		return regularExp;
	}
	public void setRegularExp(String regularExp) {
		this.regularExp = regularExp;
	}
	public String getBarcodeName() {
		return barcodeName;
	}
	public void setBarcodeName(String barcodeName) {
		this.barcodeName = barcodeName;
	}
	private String regularExp;
	private String barcodeName;
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	private String serialNo;
	private String memo;

    /**
     * bloodProductCode : ^[=][<].{8}$
     * patCode : .[*].{1,}[*].{1,}
     * labBarcode : ^.{15}$
     * bloodInvalCode : ^\d{12}$
     * planBarcode : .[*].{1,}[*].{1,}[*][1][*]\d{4}(\-)\d{2}\1\d{2}[ ]\d{2}[#]\d{2}[#]\d{2}$
     * bloodDonorCode : ^[=].{15}$
     * bloodGroupCode : ^[=][%].{4}$
     */
}
