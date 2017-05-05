package com.gentlehealthcare.mobilecare.net.impl;

import android.content.Context;

import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.ABRequest;
import com.gentlehealthcare.mobilecare.net.HttpConnection;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.bean.OtherProjectBean;

public class LoadOtherProjectDataRequest extends ABRequest<OtherProjectBean> {

	private Context context = null;
	private OtherProjectBean otherProject = null;
	private HttpConnection connection = null;

	/**
	 * @param context
	 *            上下文，如果是在子线程中处理结果，此处可传null
	 * @param respose
	 *            请求结果处理实现
	 * @param id
	 *            请求的标识，需自己设置
	 * @param isInMainThread
	 */

	public LoadOtherProjectDataRequest(Context context,
			IRespose<OtherProjectBean> respose, int id, Boolean isInMainThread,
			OtherProjectBean otherProject) {
		super(context, respose, id, isInMainThread);

		this.context = context;
		this.otherProject = otherProject;
		this.connection = new HttpConnection();
	}

	@Override
	protected OtherProjectBean connection() throws Exception {
		return (OtherProjectBean) connection.connection(
				UrlConstant.LoadOtherProject(),otherProject, context);
	}

	@Override
	protected String conn() throws Exception {
		return connection.getResposeContent();
	}

}
