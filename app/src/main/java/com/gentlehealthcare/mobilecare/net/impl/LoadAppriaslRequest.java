package com.gentlehealthcare.mobilecare.net.impl;

import android.content.Context;

import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.ABRequest;
import com.gentlehealthcare.mobilecare.net.HttpConnection;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.bean.LoadAppraislRecordBean;

/**
 * 
 * @author dzw
 * 
 */

public class LoadAppriaslRequest extends ABRequest<LoadAppraislRecordBean> {

	private LoadAppraislRecordBean exe;
	private HttpConnection connection;
	private Context context;

	/**
	 * @param context
	 *            上下文，如果是在子线程中处理结果，此处可传null
	 * @param respose
	 *            请求结果处理实现
	 * @param id
	 *            请求的标识，需自己设置
	 * @param isInMainThread
	 */
	public LoadAppriaslRequest(Context context,
			IRespose<LoadAppraislRecordBean> respose, int id,
			Boolean isInMainThread, LoadAppraislRecordBean exe) {
		super(context, respose, id, isInMainThread);
		this.connection = new HttpConnection();
		this.context = context;
		this.exe = exe;
	}

	@Override
	protected LoadAppraislRecordBean connection() throws Exception {
		return (LoadAppraislRecordBean) connection.connection(
				UrlConstant.LoadAppriaslRecord(), exe, context);
	}

	@Override
	protected String conn() throws Exception {
		// TODO Auto-generated method stub
		return connection.getResposeContent();
	}

}
