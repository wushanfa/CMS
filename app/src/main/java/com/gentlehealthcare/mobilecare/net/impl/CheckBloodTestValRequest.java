package com.gentlehealthcare.mobilecare.net.impl;

import android.content.Context;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.ABRequest;
import com.gentlehealthcare.mobilecare.net.HttpConnection;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.bean.BloodTestValCheckBean;

/**
 * 血糖值核对请求
 * Created by ouyang on 15/6/24.
 */
public class CheckBloodTestValRequest extends ABRequest<BloodTestValCheckBean> {
    private HttpConnection httpConnection;
    private BloodTestValCheckBean bloodTestValCheckBean;

    /**
     * @param context        上下文，如果是在子线程中处理结果，此处可传null
     * @param respose        请求结果处理实现
     * @param id             请求的标识，需自己设置
     * @param isInMainThread
     */
    public CheckBloodTestValRequest(Context context, IRespose<BloodTestValCheckBean> respose, int id, Boolean isInMainThread,BloodTestValCheckBean bloodTestValCheckBean) {
        super(context, respose, id, isInMainThread);
        this.bloodTestValCheckBean=bloodTestValCheckBean;
        this.httpConnection=new HttpConnection();
    }

    @Override
    protected BloodTestValCheckBean connection() throws Exception {
        return (BloodTestValCheckBean) httpConnection.connection(UrlConstant.CheckBloodTestVal(),bloodTestValCheckBean,getContext());
    }

    @Override
    protected String conn() throws Exception {
        return httpConnection.getResposeContent();
    }
}
