package com.gentlehealthcare.mobilecare.net.impl;

import android.content.Context;

import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.ABRequest;
import com.gentlehealthcare.mobilecare.net.HttpConnection;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.bean.LoadThreeTestParamBean;

public class LoadThreeTestDataRequest extends ABRequest<LoadThreeTestParamBean> {

	private Context context;
	private LoadThreeTestParamBean threeTest;
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

	public LoadThreeTestDataRequest(Context context,
			IRespose<LoadThreeTestParamBean> iRespose, int id,
			Boolean isInMainThread, LoadThreeTestParamBean threeTest) {
		super(context, iRespose, id, isInMainThread);

		this.context = context;
		this.threeTest = threeTest;
		this.connection = new HttpConnection();
	}

	@Override
	protected LoadThreeTestParamBean connection() throws Exception {
		return (LoadThreeTestParamBean) connection.connection(
				UrlConstant.loadThreeTest(), threeTest, context);
	}

	@Override
	protected String conn() throws Exception {
		return connection.getResposeContent();
	}

}
