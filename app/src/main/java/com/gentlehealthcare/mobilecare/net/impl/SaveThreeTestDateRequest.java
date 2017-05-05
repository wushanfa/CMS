package com.gentlehealthcare.mobilecare.net.impl;

import android.content.Context;

import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.ABRequest;
import com.gentlehealthcare.mobilecare.net.HttpConnection;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.bean.SaveThreeTestBean;

public class SaveThreeTestDateRequest extends ABRequest<SaveThreeTestBean> {

	private Context context;
	private HttpConnection connection;
	private SaveThreeTestBean save;

	/**
	 * @param context
	 *            上下文，如果是在子线程中处理结果，此处可传null
	 * @param iRespose
	 *            请求结果处理实现
	 * @param id
	 *            请求的标识，需自己设置
	 * @param isInMainThread
	 */
	public SaveThreeTestDateRequest(Context context,
			IRespose<SaveThreeTestBean> respose, int id,
			Boolean isInMainThread, SaveThreeTestBean save) {
		super(context, respose, id, isInMainThread);

		this.save = save;
		this.context = context;
		this.connection = new HttpConnection();
	}

	@Override
	protected SaveThreeTestBean connection() throws Exception {
		return (SaveThreeTestBean) connection.connection(
				UrlConstant.saveThreeTest(), save, context);
	}

	@Override
	protected String conn() throws Exception {
		return connection.getResposeContent();
	}
}
