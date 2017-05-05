package com.gentlehealthcare.mobilecare.net.impl;

import android.content.Context;

import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.ABRequest;
import com.gentlehealthcare.mobilecare.net.HttpConnection;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.bean.SystemInfoBean;

public class SystemInfoRequest extends ABRequest<SystemInfoBean>{
	private HttpConnection connection;
	private Context context;
	public SystemInfoRequest(Context context, IRespose<SystemInfoBean> respose,
			int id, Boolean isInMainThread) {
		super(context, respose, id, isInMainThread);
		// TODO Auto-generated constructor stub
		connection=new HttpConnection();
		this.context=context;
	}

	@Override
	protected SystemInfoBean connection() throws Exception {
		// TODO Auto-generated method stub
		return (SystemInfoBean) connection.connection(UrlConstant.GetSystemInfo(), new SystemInfoBean(), context);
	}
    @Override
    protected String conn() throws Exception {
        return connection.getResposeContent();
    }
}
