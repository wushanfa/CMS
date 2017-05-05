package com.gentlehealthcare.mobilecare.net.impl;

import android.content.Context;

import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.ABRequest;
import com.gentlehealthcare.mobilecare.net.HttpConnection;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.bean.SyncCompleteWorkBean;
/**
 * 
 * @ClassName: SyncCompleteWorkRequest 
 * @Description: 同步 已完成工作
 * @author ouyang
 * @date 2015年3月10日 上午9:48:07 
 *
 */
public class SyncCompleteWorkRequest extends ABRequest<SyncCompleteWorkBean> {
	private Context context;
	private HttpConnection connection;
	private SyncCompleteWorkBean completeworkBean;
	public SyncCompleteWorkRequest(Context context,
			IRespose<SyncCompleteWorkBean> respose, int id,
			Boolean isInMainThread, SyncCompleteWorkBean completeworkBean) {
		super(context, respose, id, isInMainThread);
		// TODO Auto-generated constructor stub
		this.context=context;
		this.connection=new HttpConnection();
		this.completeworkBean=completeworkBean;
	}

	@Override
	protected SyncCompleteWorkBean connection() throws Exception {
		// TODO Auto-generated method stub
		return (SyncCompleteWorkBean) connection.connection(UrlConstant.GetSyncComlitedWorkList(), this.completeworkBean, this.context);
	}

    @Override
    protected String conn() throws Exception {
        return connection.getResposeContent();
    }

}
