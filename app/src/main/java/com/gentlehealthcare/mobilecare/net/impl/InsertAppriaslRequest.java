package com.gentlehealthcare.mobilecare.net.impl;

import android.content.Context;

import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.ABRequest;
import com.gentlehealthcare.mobilecare.net.HttpConnection;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.bean.insertAppraislRecordBean;

/**
 * 
 * @author dzw
 * 
 */

public class InsertAppriaslRequest extends ABRequest<insertAppraislRecordBean> {

	private insertAppraislRecordBean exe;
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
	public InsertAppriaslRequest(Context context,
			IRespose<insertAppraislRecordBean> respose, int id,
			Boolean isInMainThread, insertAppraislRecordBean exe) {
		super(context, respose, id, isInMainThread);
		this.connection = new HttpConnection();
		this.context = context;
		this.exe = exe;
	}

	@Override
	protected insertAppraislRecordBean connection() throws Exception {
		return (insertAppraislRecordBean) connection.connection(
				UrlConstant.insertAppriasl(), exe, context);
	}

	@Override
	protected String conn() throws Exception {
		// TODO Auto-generated method stub
		return connection.getResposeContent();
	}

}
