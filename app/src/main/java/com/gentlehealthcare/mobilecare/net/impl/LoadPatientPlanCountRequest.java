package com.gentlehealthcare.mobilecare.net.impl;

import android.content.Context;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.ABRequest;
import com.gentlehealthcare.mobilecare.net.HttpConnection;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.bean.LoadPatientPlanCountBean;

/**
 * Created by ouyang on 15/8/26.
 */
public class LoadPatientPlanCountRequest extends ABRequest<LoadPatientPlanCountBean> {
    private HttpConnection httpConnection=null;
    private LoadPatientPlanCountBean loadPatientPlanCountBean=null;
    /**
     * @param context        上下文，如果是在子线程中处理结果，此处可传null
     * @param respose        请求结果处理实现
     * @param id             请求的标识，需自己设置
     * @param isInMainThread
     */
    public LoadPatientPlanCountRequest(Context context, IRespose<LoadPatientPlanCountBean> respose, int id, Boolean isInMainThread,LoadPatientPlanCountBean loadPatientPlanCountBean) {
        super(context, respose, id, isInMainThread);
        this.httpConnection=new HttpConnection();
        this.loadPatientPlanCountBean=loadPatientPlanCountBean;
    }

    @Override
    protected LoadPatientPlanCountBean connection() throws Exception {
        return (LoadPatientPlanCountBean) httpConnection.connection(UrlConstant.LoadPatientPlanCount(),loadPatientPlanCountBean,getContext());
    }

    @Override
    protected String conn() throws Exception {
        return httpConnection.getResposeContent();
    }
}
