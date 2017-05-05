package com.gentlehealthcare.mobilecare.net.impl;

import android.content.Context;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.ABRequest;
import com.gentlehealthcare.mobilecare.net.HttpConnection;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.bean.LoadLastGlucoseValCheckValBean;

/**
 * Created by ouyang on 15/7/14.
 */
public class LoadLastGlucoseValCheckValReq extends ABRequest<LoadLastGlucoseValCheckValBean> {
    private HttpConnection httpConnection=null;
    private LoadLastGlucoseValCheckValBean loadLastGlucoseValCheckValBean=null;
    /**
     * @param context        上下文，如果是在子线程中处理结果，此处可传null
     * @param respose        请求结果处理实现
     * @param id             请求的标识，需自己设置
     * @param isInMainThread
     */
    public LoadLastGlucoseValCheckValReq(Context context, IRespose<LoadLastGlucoseValCheckValBean> respose, int id, Boolean isInMainThread, LoadLastGlucoseValCheckValBean loadLastGlucoseValCheckValBean) {
        super(context, respose, id, isInMainThread);
        this.loadLastGlucoseValCheckValBean=loadLastGlucoseValCheckValBean;
        this.httpConnection=new HttpConnection();
    }

    @Override
    protected LoadLastGlucoseValCheckValBean connection() throws Exception {
        return (LoadLastGlucoseValCheckValBean) httpConnection.connection(UrlConstant.LoadLastGlucoseValCheckVal(),loadLastGlucoseValCheckValBean,getContext());
    }

    @Override
    protected String conn() throws Exception {
        return httpConnection.getResposeContent();
    }
}
