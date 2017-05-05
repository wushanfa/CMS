package com.gentlehealthcare.mobilecare.net.impl;

import android.content.Context;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.ABRequest;
import com.gentlehealthcare.mobilecare.net.HttpConnection;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.bean.PatMajorInfoBean;

/**
 * Created by ouyang on 15/6/23.
 */
public class LoadPatInsulinPlansRequest extends ABRequest<PatMajorInfoBean> {
    private HttpConnection httpConnection=null;
    private PatMajorInfoBean patMajorInfoBean;
    /**
     * @param context        上下文，如果是在子线程中处理结果，此处可传null
     * @param respose        请求结果处理实现
     * @param id             请求的标识，需自己设置
     * @param isInMainThread
     */
    public LoadPatInsulinPlansRequest(Context context, IRespose<PatMajorInfoBean> respose, int id, Boolean isInMainThread,PatMajorInfoBean patMajorInfoBean) {
        super(context, respose, id, isInMainThread);
        this.httpConnection=new HttpConnection();
        this.patMajorInfoBean=patMajorInfoBean;

    }

    @Override
    protected PatMajorInfoBean connection() throws Exception {
        return (PatMajorInfoBean) httpConnection.connection(UrlConstant.LoadPatInsulinPlans(),patMajorInfoBean,getContext());
    }

    @Override
    protected String conn() throws Exception {
        return httpConnection.getResposeContent();
    }
}
