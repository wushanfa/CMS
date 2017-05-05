package com.gentlehealthcare.mobilecare.net.impl;

import android.content.Context;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.ABRequest;
import com.gentlehealthcare.mobilecare.net.HttpConnection;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.bean.BloodTestBean;

/**
 * 血糖测试请求
 * Created by ouyang on 15/6/23.
 */
public class BloodTestRequest extends ABRequest<BloodTestBean> {
    private BloodTestBean bloodTestBean;
    private HttpConnection httpConnection;
    /**
     * @param context        上下文，如果是在子线程中处理结果，此处可传null
     * @param respose        请求结果处理实现
     * @param id             请求的标识，需自己设置
     * @param isInMainThread
     */
    public BloodTestRequest(Context context, IRespose<BloodTestBean> respose, int id, Boolean isInMainThread,BloodTestBean bloodTestBean) {
        super(context, respose, id, isInMainThread);
        this.bloodTestBean=bloodTestBean;
        httpConnection=new HttpConnection();
    }

    @Override
    protected BloodTestBean connection() throws Exception {
        return (BloodTestBean) httpConnection.connection(UrlConstant.GetStartBloodSugarTest(),bloodTestBean,getContext());
    }

    @Override
    protected String conn() throws Exception {
        return httpConnection.getResposeContent();
    }
}
