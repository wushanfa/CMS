package com.gentlehealthcare.mobilecare.net.impl;

import android.content.Context;

import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.ABRequest;
import com.gentlehealthcare.mobilecare.net.HttpConnection;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.bean.SyncDeptPatientBean;

public class SyncDeptPatientRequest extends ABRequest<SyncDeptPatientBean> {
	
	private SyncDeptPatientBean syncPatientBean;
	private HttpConnection connection;
	private Context context;
	public SyncDeptPatientRequest(Context context,
			IRespose<SyncDeptPatientBean> respose, int id,
			Boolean isInMainThread,SyncDeptPatientBean syncPatientBean) {
		super(context, respose, id, isInMainThread);
		this.context=context;
		this.connection=new HttpConnection();
		this.syncPatientBean=syncPatientBean;
	}

	@Override
	protected SyncDeptPatientBean connection() throws Exception {
		// TODO Auto-generated method stub
		return (SyncDeptPatientBean) connection.connection(UrlConstant.GetSyncDeptPatient(), syncPatientBean, context);
	}
    @Override
    protected String conn() throws Exception {
        return connection.getResposeContent();
    }
}
