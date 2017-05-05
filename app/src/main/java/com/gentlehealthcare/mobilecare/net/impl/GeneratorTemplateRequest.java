package com.gentlehealthcare.mobilecare.net.impl;

import android.content.Context;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.ABRequest;
import com.gentlehealthcare.mobilecare.net.HttpConnection;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.bean.InjectionTemplateBean;

/**
 * 重新生成模版数据
 * Created by ouyang on 15/6/25.
 */
public class GeneratorTemplateRequest extends ABRequest<InjectionTemplateBean> {
    private InjectionTemplateBean injectionTemplateBean;
    private HttpConnection httpConnection;
    /**
     * @param context        上下文，如果是在子线程中处理结果，此处可传null
     * @param respose        请求结果处理实现
     * @param id             请求的标识，需自己设置
     * @param isInMainThread
     */
    public GeneratorTemplateRequest(Context context, IRespose<InjectionTemplateBean> respose, int id, Boolean isInMainThread,InjectionTemplateBean injectionTemplateBean) {
        super(context, respose, id, isInMainThread);
        this.injectionTemplateBean=injectionTemplateBean;
        this.httpConnection=new HttpConnection();
    }

    @Override
    protected InjectionTemplateBean connection() throws Exception {
        return (InjectionTemplateBean) httpConnection.connection(UrlConstant.GeneratorTemplate(),injectionTemplateBean,getContext());
    }

    @Override
    protected String conn() throws Exception {
        return httpConnection.getResposeContent();
    }
}
