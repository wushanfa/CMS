package com.gentlehealthcare.mobilecare.db.table;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class TB_LoginRecord {
	@DatabaseField(generatedId=true)
	private int loginrecordId;
	@DatabaseField
	private String userName;//用户名
	@DatabaseField
	private long updateTime;//更新时间
	public int getLoginrecordId() {
		return loginrecordId;
	}
	public void setLoginrecordId(int loginrecordId) {
		this.loginrecordId = loginrecordId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public long getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}
	
	
	
}
