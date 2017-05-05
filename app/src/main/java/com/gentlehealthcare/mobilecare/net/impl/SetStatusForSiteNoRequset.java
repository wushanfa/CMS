package com.gentlehealthcare.mobilecare.net.impl;

import android.content.Context;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.ABRequest;
import com.gentlehealthcare.mobilecare.net.HttpConnection;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.bean.SetStatusForAreaBean;

/**
 * Created by ouyang on 15/6/26.
 */
public class SetStatusForSiteNoRequset extends ABRequest<SetStatusForAreaBean>{
    private SetStatusForAreaBean statusForAreaBean;
    private HttpConnection httpConnection=null;
    /**
     * @param context        上下文，如果是在子线程中处理结果，此处可传null
     * @param respose        请求结果处理实现
     * @param id             请求的标识，需自己设置
     * @param isInMainThread
     */
    public SetStatusForSiteNoRequset(Context context, IRespose<SetStatusForAreaBean> respose, int id, Boolean isInMainThread,SetStatusForAreaBean statusForAreaBean) {
        super(context, respose, id, isInMainThread);
        this.statusForAreaBean=statusForAreaBean;
        this.httpConnection=new HttpConnection();
    }

    @Override
    protected SetStatusForAreaBean connection() throws Exception {
        return (SetStatusForAreaBean) httpConnection.connection(UrlConstant.SetStatusForSiteNo(),statusForAreaBean,getContext());
    }

    @Override
    protected String conn() throws Exception {
        return httpConnection.getResposeContent();
    }
}