package com.gentlehealthcare.mobilecare.net.impl;

import android.content.Context;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.ABRequest;
import com.gentlehealthcare.mobilecare.net.HttpConnection;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.bean.CompleteGlucoseValueCheckBean;

/**
 * 血糖检测结果测试异常
 * Created by ouyang on 15/7/9.
 */
public class CompleteGlucoseValueCheckReq extends ABRequest<CompleteGlucoseValueCheckBean>{
    private CompleteGlucoseValueCheckBean completeGlucoseValueCheckBean;
    private HttpConnection httpConnection=null;
    /**
     * @param context        上下文，如果是在子线程中处理结果，此处可传null
     * @param respose        请求结果处理实现
     * @param id             请求的标识，需自己设置
     * @param isInMainThread
     */
    public CompleteGlucoseValueCheckReq(Context context, IRespose<CompleteGlucoseValueCheckBean> respose, int id, Boolean isInMainThread,CompleteGlucoseValueCheckBean completeGlucoseValueCheckBean) {
        super(context, respose, id, isInMainThread);
        this.httpConnection=new HttpConnection();
        this.completeGlucoseValueCheckBean=completeGlucoseValueCheckBean;

    }

    @Override
    protected CompleteGlucoseValueCheckBean connection() throws Exception {
        return (CompleteGlucoseValueCheckBean) httpConnection.connection(UrlConstant.CompleteGlucoseValCheck(),completeGlucoseValueCheckBean,getContext());
    }

    @Override
    protected String conn() throws Exception {
        return httpConnection.getResposeContent();
    }
}
