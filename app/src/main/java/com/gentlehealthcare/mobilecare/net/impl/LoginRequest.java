package com.gentlehealthcare.mobilecare.net.impl;

import android.content.Context;

import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.ABRequest;
import com.gentlehealthcare.mobilecare.net.HttpConnection;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.bean.LoginBean;
/**
 * 
 * @ClassName: LoginRequest 
 * @Description: 登录请求
 * @author ouyang
 * @date 2015年3月6日 上午9:44:01 
 *
 */
public class LoginRequest extends ABRequest<LoginBean>  {
	private HttpConnection connection;
	private LoginBean loginBean;
	private Context context;
	public LoginRequest(Context context, IRespose<LoginBean> respose, int id,
			Boolean isInMainThread,LoginBean loginBean) {
		super(context, respose, id, isInMainThread);
		// TODO Auto-generated constructor stub
		connection=new HttpConnection();
		this.loginBean=loginBean;
		this.context=context;
	}

	

	@Override
	protected LoginBean connection() throws Exception {
		// TODO Auto-generated method stub
		return (LoginBean) connection.connection(UrlConstant.GetLogin(), loginBean, context);
	}

    @Override
    protected String conn() throws Exception {
        return connection.getResposeContent();
    }

}
