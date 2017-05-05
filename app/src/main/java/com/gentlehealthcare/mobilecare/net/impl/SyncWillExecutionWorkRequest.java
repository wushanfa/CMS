package com.gentlehealthcare.mobilecare.net.impl;

import android.content.Context;

import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.ABRequest;
import com.gentlehealthcare.mobilecare.net.HttpConnection;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.bean.SyncWillExecutionWorkBean;

public class SyncWillExecutionWorkRequest extends ABRequest<SyncWillExecutionWorkBean> {
	private SyncWillExecutionWorkBean syncWillExecutionWorkBean;
	private Context context;
	private HttpConnection connection;
	
	public SyncWillExecutionWorkRequest(Context context,
			IRespose<SyncWillExecutionWorkBean> respose, int id,
			Boolean isInMainThread,SyncWillExecutionWorkBean syncWillExecutionWorkBean) {
		super(context, respose, id, isInMainThread);
		// TODO Auto-generated constructor stub
		this.context=context;
		this.connection=new HttpConnection();
		this.syncWillExecutionWorkBean=syncWillExecutionWorkBean;
	}

	@Override
	protected SyncWillExecutionWorkBean connection() throws Exception {
		// TODO Auto-generated method stub
		return (SyncWillExecutionWorkBean) connection.connection(UrlConstant.GetSyncWillExecutionWork(), this.syncWillExecutionWorkBean, this.context);
	}
    @Override
    protected String conn() throws Exception {
        return connection.getResposeContent();
    }
}
