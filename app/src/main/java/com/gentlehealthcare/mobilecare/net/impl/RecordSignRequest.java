package com.gentlehealthcare.mobilecare.net.impl;

import android.content.Context;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.ABRequest;
import com.gentlehealthcare.mobilecare.net.HttpConnection;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.bean.RecordSignsBean;

/**
 * Created by ouyang on 15/7/15.
 */
public class RecordSignRequest  extends ABRequest<RecordSignsBean>{
    private RecordSignsBean recordSignsBean=null;
    private HttpConnection httpConnection=null;
    /**
     * @param context        上下文，如果是在子线程中处理结果，此处可传null
     * @param respose        请求结果处理实现
     * @param id             请求的标识，需自己设置
     * @param isInMainThread
     */
    public RecordSignRequest(Context context, IRespose<RecordSignsBean> respose, int id, Boolean isInMainThread,RecordSignsBean recordSignsBean) {
        super(context, respose, id, isInMainThread);
        this.recordSignsBean=recordSignsBean;
        this.httpConnection=new HttpConnection();
    }

    @Override
    protected RecordSignsBean connection() throws Exception {
        return (RecordSignsBean) httpConnection.connection(UrlConstant.RecordSigns(),recordSignsBean,getContext());
    }

    @Override
    protected String conn() throws Exception {
        return httpConnection.getResposeContent();
    }
}
