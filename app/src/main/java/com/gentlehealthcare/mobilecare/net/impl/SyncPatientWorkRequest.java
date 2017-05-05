package com.gentlehealthcare.mobilecare.net.impl;

import android.content.Context;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.ABRequest;
import com.gentlehealthcare.mobilecare.net.HttpConnection;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientWorkBean;

/**
 * 获取病人工作请求
 * Created by ouyang on 15/7/6.
 */
public class SyncPatientWorkRequest extends ABRequest<SyncPatientWorkBean> {
    private SyncPatientWorkBean syncPatientWorkBean=null;
    private HttpConnection httpConnection=null;
    /**
     * @param context        上下文，如果是在子线程中处理结果，此处可传null
     * @param respose        请求结果处理实现
     * @param id             请求的标识，需自己设置
     * @param isInMainThread
     */
    public SyncPatientWorkRequest(Context context, IRespose<SyncPatientWorkBean> respose, int id, Boolean isInMainThread,SyncPatientWorkBean syncPatientWorkBean) {
        super(context, respose, id, isInMainThread);
        this.syncPatientWorkBean=syncPatientWorkBean;
        this.httpConnection=new HttpConnection();
    }

    @Override
    protected SyncPatientWorkBean connection() throws Exception {
        return (SyncPatientWorkBean) httpConnection.connection(UrlConstant.LoadPatientById(),syncPatientWorkBean,getContext());
    }

    @Override
    protected String conn() throws Exception {
        return httpConnection.getResposeContent();
    }
}
