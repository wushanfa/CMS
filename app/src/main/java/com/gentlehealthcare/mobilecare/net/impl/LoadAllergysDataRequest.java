package com.gentlehealthcare.mobilecare.net.impl;

import android.content.Context;

import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.ABRequest;
import com.gentlehealthcare.mobilecare.net.HttpConnection;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.bean.AllergyBean;

public class LoadAllergysDataRequest extends ABRequest<AllergyBean> {

	private Context context = null;
	private AllergyBean allergyBean = null;
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

	public LoadAllergysDataRequest(Context context,
			IRespose<AllergyBean> respose, int id, Boolean isInMainThread,
			AllergyBean allergyBean) {
		super(context, respose, id, isInMainThread);

		this.context = context;
		this.allergyBean = allergyBean;
		this.connection = new HttpConnection();
	}

	@Override
	protected AllergyBean connection() throws Exception {
		return (AllergyBean) connection.connection(
				UrlConstant.LoadDrugsAllergy(), allergyBean, context);
	}

	@Override
	protected String conn() throws Exception {
		return connection.getResposeContent();
	}

}
