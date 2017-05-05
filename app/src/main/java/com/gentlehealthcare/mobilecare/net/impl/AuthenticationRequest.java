package com.gentlehealthcare.mobilecare.net.impl;

import android.content.Context;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.ABRequest;
import com.gentlehealthcare.mobilecare.net.HttpConnection;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.bean.AuthenticationBean;

/**
 * Created by ouyang on 2015/3/20.
 */
public class AuthenticationRequest extends ABRequest<AuthenticationBean> {

    private Context context;
    private HttpConnection httpConnection;
    private AuthenticationBean authenticationBean;
    private int id;//类型: 0 给药  1胰岛素
    /**
     * @param context        上下文，如果是在子线程中处理结果，此处可传null
     * @param respose        请求结果处理实现
     * @param id             请求的标识，需自己设置
     * @param isInMainThread
     */
    public AuthenticationRequest(Context context, IRespose<AuthenticationBean> respose, int id, Boolean isInMainThread,AuthenticationBean authenticationBean) {
        super(context, respose,id , isInMainThread);
        this.context=context;
        this.httpConnection=new HttpConnection();
        this.authenticationBean=authenticationBean;
        this.id=id;
    }

    @Override
    protected AuthenticationBean connection() throws Exception {
        return (AuthenticationBean) httpConnection.connection(UrlConstant.GetCheckAuthentication(id),this.authenticationBean,context);

    }

    @Override
    protected String conn() throws Exception {
        return httpConnection.getResposeContent();
    }
}
