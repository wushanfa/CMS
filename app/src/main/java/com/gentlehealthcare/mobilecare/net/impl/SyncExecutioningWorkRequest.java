package com.gentlehealthcare.mobilecare.net.impl;

import android.content.Context;

import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.ABRequest;
import com.gentlehealthcare.mobilecare.net.HttpConnection;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.bean.SyncExecutioningWorkWorkBean;
import com.gentlehealthcare.mobilecare.net.bean.SyncWillExecutionWorkBean;

/**
 * 
 * @ClassName: SyncExecutioningWorkRequest 
 * @Description: 执行中工作 同步
 * @author ouyang
 * @date 2015年3月10日 上午9:40:45 
 *
 */
public class SyncExecutioningWorkRequest extends ABRequest<SyncWillExecutionWorkBean> {
	private SyncExecutioningWorkWorkBean syncExecutioningWorkBean;
	private Context context;
	private HttpConnection connection;
	
	public SyncExecutioningWorkRequest(Context context,
			IRespose<SyncWillExecutionWorkBean> respose, int id,
			Boolean isInMainThread,SyncExecutioningWorkWorkBean syncExecutioningWorkBean) {
		super(context, respose, id, isInMainThread);
		// TODO Auto-generated constructor stub
		this.context=context;
		this.connection=new HttpConnection();
		this.syncExecutioningWorkBean=syncExecutioningWorkBean;
	}

	@Override
	protected SyncWillExecutionWorkBean connection() throws Exception {
		// TODO Auto-generated method stub
		return (SyncWillExecutionWorkBean) connection.connection(UrlConstant.GetSyncExecutingWork(), this.syncExecutioningWorkBean, this.context);
	}
    @Override
    protected String conn() throws Exception {
        return connection.getResposeContent();
    }
}
