package com.gentlehealthcare.mobilecare.net.impl;

import android.content.Context;

import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.ABRequest;
import com.gentlehealthcare.mobilecare.net.HttpConnection;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.bean.ModifyThreeTestBean;

public class ModifyThreeTestDateRequest extends ABRequest<ModifyThreeTestBean> {

	private Context context;
	private ModifyThreeTestBean modify;
	private HttpConnection connection;

	/**
	 * @param context
	 *            上下文，如果是在子线程中处理结果，此处可传null
	 * @param iRespose
	 *            请求结果处理实现
	 * @param id
	 *            请求的标识，需自己设置
	 * @param isInMainThread
	 */
	public ModifyThreeTestDateRequest(Context context,
			IRespose<ModifyThreeTestBean> respose, int id,
			Boolean isInMainThread, ModifyThreeTestBean modify) {
		super(context, respose, id, isInMainThread);
		this.modify = modify;
		this.context = context;
		this.connection = new HttpConnection();
	}

	@Override
	protected ModifyThreeTestBean connection() throws Exception {
		return (ModifyThreeTestBean) connection.connection(
				UrlConstant.modifyThreeTest(), modify, context);
	}

	@Override
	protected String conn() throws Exception {
		return connection.getResposeContent();
	}

}
