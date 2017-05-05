package com.gentlehealthcare.mobilecare.net.impl;

import android.content.Context;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.ABRequest;
import com.gentlehealthcare.mobilecare.net.HttpConnection;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.bean.RecordAppraisalBean;

/**
 * Created by ouyang on 15/7/5.
 */
public class RecordAppraisalRequest extends ABRequest<RecordAppraisalBean> {
    private HttpConnection httpConnection=null;
    private RecordAppraisalBean recordAppraisalBean;
    /**
     * @param context        上下文，如果是在子线程中处理结果，此处可传null
     * @param respose        请求结果处理实现
     * @param id             请求的标识，需自己设置
     * @param isInMainThread
     */
    public RecordAppraisalRequest(Context context, IRespose<RecordAppraisalBean> respose, int id, Boolean isInMainThread,RecordAppraisalBean recordAppraisalBean) {
        super(context, respose, id, isInMainThread);
        this.recordAppraisalBean=recordAppraisalBean;
        this.httpConnection=new HttpConnection();
    }

    @Override
    protected RecordAppraisalBean connection() throws Exception {
        return (RecordAppraisalBean) httpConnection.connection(UrlConstant.RecordAppraisal(),recordAppraisalBean,getContext());
    }

    @Override
    protected String conn() throws Exception {
        return httpConnection.getResposeContent();
    }
}
