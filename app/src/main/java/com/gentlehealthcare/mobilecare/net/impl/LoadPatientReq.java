package com.gentlehealthcare.mobilecare.net.impl;

import android.content.Context;

import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.ABRequest;
import com.gentlehealthcare.mobilecare.net.HttpConnection;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.bean.LoadPatientBean;

/**
 * Created by ouyang on 15/9/1.
 */
public class LoadPatientReq  extends ABRequest<LoadPatientBean>{
    private HttpConnection httpConnection=null;
    private LoadPatientBean loadPatientBean=null;
    /**
     * @param context        上下文，如果是在子线程中处理结果，此处可传null
     * @param respose        请求结果处理实现
     * @param id             请求的标识，需自己设置
     * @param isInMainThread
     */
    public LoadPatientReq(Context context, IRespose<LoadPatientBean> respose, int id, Boolean isInMainThread,LoadPatientBean loadPatientBean) {
        super(context, respose, id, isInMainThread);
        this.httpConnection=new HttpConnection();
        this.loadPatientBean=loadPatientBean;
    }

    @Override
    protected LoadPatientBean connection() throws Exception {
        return (LoadPatientBean) httpConnection.connection(UrlConstant.LoadPatient(),loadPatientBean,getContext());
    }

    @Override
    protected String conn() throws Exception {
        return httpConnection.getResposeContent();
    }
}
