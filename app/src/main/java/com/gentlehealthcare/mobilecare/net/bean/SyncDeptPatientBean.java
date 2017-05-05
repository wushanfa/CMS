package com.gentlehealthcare.mobilecare.net.bean;

import java.util.List;

import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.db.table.TB_Patient;

public class SyncDeptPatientBean {
	private List<TB_Patient> deptPatList;
    private String wardCode;

	@Override
	public String toString() {
		return "?username="+UserInfo.getUserName()+"&wardCode="+UserInfo.getWardCode();
	
	}

	public List<TB_Patient> getDeptPatList() {
		return deptPatList;
	}

	public void setDeptPatList(List<TB_Patient> deptPatList) {
		this.deptPatList = deptPatList;
	}

    public String getWardCode() {
        return wardCode;
    }

    public void setWardCode(String wardCode) {
        this.wardCode = wardCode;
    }
}
