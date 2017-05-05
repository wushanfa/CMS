package com.gentlehealthcare.mobilecare.net.impl;

import android.content.Context;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.ABRequest;
import com.gentlehealthcare.mobilecare.net.HttpConnection;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.bean.ExecutionOrderBean;

/**
 * Created by ouyang on 15/6/2.
 */
public class ExecutionOrderRequest extends ABRequest<ExecutionOrderBean> {
    private HttpConnection httpConnection=null;
    private Context context;
    private ExecutionOrderBean executionOrderBean;
    /**
     * @param context        上下文，如果是在子线程中处理结果，此处可传null
     * @param respose        请求结果处理实现
     * @param id             请求的标识，需自己设置
     * @param isInMainThread
     */
    public ExecutionOrderRequest(Context context, IRespose<ExecutionOrderBean> respose, int id, Boolean isInMainThread,ExecutionOrderBean executionOrderBean) {
        super(context, respose, id, isInMainThread);
        this.executionOrderBean=executionOrderBean;
        this.context=context;
        this.httpConnection=new HttpConnection();
    }

    @Override
    protected ExecutionOrderBean connection() throws Exception {
        return (ExecutionOrderBean) httpConnection.connection(UrlConstant.ExecutionOrders(),executionOrderBean,context);
    }

    @Override
    protected String conn() throws Exception {
        return httpConnection.getResposeContent();
    }
}
