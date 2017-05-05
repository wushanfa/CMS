package com.gentlehealthcare.mobilecare.net.impl;

import android.content.Context;

import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.ABRequest;
import com.gentlehealthcare.mobilecare.net.HttpConnection;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.bean.SpeedDateBean;

public class SpeedRecordDateRequest extends ABRequest<SpeedDateBean> {

	private SpeedDateBean speed;
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
	public SpeedRecordDateRequest(Context context,
			IRespose<SpeedDateBean> respose, int id, Boolean isInMainThread,
			SpeedDateBean speed) {
		super(context, respose, id, isInMainThread);
		this.context = context;
		this.connection = new HttpConnection();
		this.speed = speed;
	}

	@Override
	protected SpeedDateBean connection() throws Exception {
		return (SpeedDateBean) connection.connection(
				UrlConstant.saveSpeedDate(), speed, context);
	}

	@Override
	protected String conn() throws Exception {
		return connection.getResposeContent();
	}

}
