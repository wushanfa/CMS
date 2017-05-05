package com.gentlehealthcare.mobilecare.net.bean;

import com.gentlehealthcare.mobilecare.UserInfo;

/**
 * 
 * @ClassName: SyncCompleteWorkRequest 
 * @Description: 同步已完成工作实体类
 * @author ouyang
 * @date 2015年3月10日 上午9:19:45 
 *
 */
public class SyncCompleteWorkBean {

    private String patId;
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "?userName="+UserInfo.getUserName()+"&patId="+getPatId();
	
	}


    public String getPatId() {
        return patId;
    }

    public void setPatId(String patId) {
        this.patId = patId;
    }
}
