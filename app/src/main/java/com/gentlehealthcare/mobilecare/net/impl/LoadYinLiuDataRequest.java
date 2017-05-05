package com.gentlehealthcare.mobilecare.net.impl;

import android.content.Context;

import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.ABRequest;
import com.gentlehealthcare.mobilecare.net.HttpConnection;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.bean.YinLiuBean;
/**
 * @类说名 请求引流数据
 * @author HeRen_Zyy
 *
 */
public class LoadYinLiuDataRequest extends ABRequest<YinLiuBean>{
	
    private YinLiuBean mYinLiuBean=null;
    private HttpConnection mHttpConnection=null;
    private Context mContext=null;
    
    /**
     * @param context        上下文，如果是在子线程中处理结果，此处可传null
     * @param respose        请求结果处理实现
     * @param id             请求的标识，需自己设置
     * @param isInMainThread
     */

	public LoadYinLiuDataRequest(Context context, IRespose<YinLiuBean> respose,
			int id, Boolean isInMainThread,YinLiuBean yinLiuBean) {
		super(context, respose, id, isInMainThread);
		
		this.mContext=context;
		this.mYinLiuBean=yinLiuBean;
		this.mHttpConnection=new HttpConnection();
	}

	@Override
	protected YinLiuBean connection() throws Exception {

		return (YinLiuBean) mHttpConnection.connection(UrlConstant.LoadYinLiu(),mYinLiuBean,mContext);
	}

	@Override
	protected String conn() throws Exception {
		return mHttpConnection.getResposeContent();
	}

}
