package com.gentlehealthcare.mobilecare.net.bean;

import java.util.List;

import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.db.table.TB_ExecutionWork;
/**
 * 
 * @ClassName: SyncWillExecutionWorkBean 
 * @Description: 同步待执行工作 实体类
 * @author ouyang
 * @date 2015年3月10日 上午9:14:20 
 *
 */
public class SyncExecutioningWorkWorkBean {
	private List<TB_ExecutionWork> executingWorkList;

	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "?username="+UserInfo.getUserName();
	
	}


	public List<TB_ExecutionWork> getExecutingWorkList() {
		return executingWorkList;
	}


	public void setExecutingWorkList(List<TB_ExecutionWork> executingWorkList) {
		this.executingWorkList = executingWorkList;
	}
}
