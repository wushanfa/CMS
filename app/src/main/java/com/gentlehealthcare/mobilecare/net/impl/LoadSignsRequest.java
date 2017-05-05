package com.gentlehealthcare.mobilecare.net.impl;

import android.content.Context;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.ABRequest;
import com.gentlehealthcare.mobilecare.net.HttpConnection;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.bean.RecordSignsBean;

/**
 * 加载整个体征
 * Created by ouyang on 15/7/15.
 */
public class LoadSignsRequest extends ABRequest<RecordSignsBean> {
    private HttpConnection httpConnection=null;
    private RecordSignsBean recordSignsBean=null;
    /**
     * @param context        上下文，如果是在子线程中处理结果，此处可传null
     * @param respose        请求结果处理实现
     * @param id             请求的标识，需自己设置
     * @param isInMainThread
     */
    public LoadSignsRequest(Context context, IRespose<RecordSignsBean> respose, int id, Boolean isInMainThread,RecordSignsBean recordSignsBean) {
        super(context, respose, id, isInMainThread);
        this.recordSignsBean=new RecordSignsBean();
        this.httpConnection=new HttpConnection();
    }

    @Override
    protected RecordSignsBean connection() throws Exception {
        return (RecordSignsBean) httpConnection.connection(UrlConstant.LoadSigns(),recordSignsBean,getContext());
    }

    @Override
    protected String conn() throws Exception {
        return httpConnection.getResposeContent();
    }
}
