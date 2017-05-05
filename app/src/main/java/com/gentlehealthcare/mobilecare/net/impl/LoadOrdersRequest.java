package com.gentlehealthcare.mobilecare.net.impl;

import android.content.Context;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.ABRequest;
import com.gentlehealthcare.mobilecare.net.HttpConnection;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.bean.LoadOrdersBean;

/**
 * Created by ouyang on 15/6/2.
 */
public class LoadOrdersRequest extends ABRequest<LoadOrdersBean> {
    private LoadOrdersBean loadOrdersBean;
    private Context context;
    private HttpConnection httpConnection;
    /**
     * @param context        上下文，如果是在子线程中处理结果，此处可传null
     * @param respose        请求结果处理实现
     * @param id             请求的标识，需自己设置
     * @param isInMainThread
     */
    public LoadOrdersRequest(Context context, IRespose<LoadOrdersBean> respose, int id, Boolean isInMainThread,LoadOrdersBean loadOrdersBean) {
        super(context, respose, id, isInMainThread);
        this.context=context;
        this.httpConnection=new HttpConnection();
        this.loadOrdersBean=loadOrdersBean;
    }

    @Override
    protected LoadOrdersBean connection() throws Exception {
        return (LoadOrdersBean) httpConnection.connection(UrlConstant.LoadOrders(),loadOrdersBean,context);
    }

    @Override
    protected String conn() throws Exception {
        return httpConnection.getResposeContent();
    }
}
