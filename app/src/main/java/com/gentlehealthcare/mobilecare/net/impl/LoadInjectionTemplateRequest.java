package com.gentlehealthcare.mobilecare.net.impl;

import android.content.Context;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.ABRequest;
import com.gentlehealthcare.mobilecare.net.HttpConnection;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.bean.InjectionTemplateBean;

/**
 *  获取注射模板请求
 * Created by ouyang on 15/6/24.
 */
public class LoadInjectionTemplateRequest extends ABRequest<InjectionTemplateBean>{
    private InjectionTemplateBean injectionTemplateBean;
    private HttpConnection httpConnection;

    /**
     * @param context        上下文，如果是在子线程中处理结果，此处可传null
     * @param respose        请求结果处理实现
     * @param id             请求的标识，需自己设置
     * @param isInMainThread
     */
    public LoadInjectionTemplateRequest(Context context, IRespose<InjectionTemplateBean> respose, int id, Boolean isInMainThread,InjectionTemplateBean injectionTemplateBean) {
        super(context, respose, id, isInMainThread);
        httpConnection=new HttpConnection();
        this.injectionTemplateBean=injectionTemplateBean;
    }

    @Override
    protected InjectionTemplateBean connection() throws Exception {
        String url="";
        if (injectionTemplateBean.getIsReload()==1){
            url=UrlConstant.GeneratorTemplate();
        }else if (injectionTemplateBean.getIsReload()==0){
            url=UrlConstant.LoadInjectionTemplate();
        }
        return (InjectionTemplateBean) httpConnection.connection(url,injectionTemplateBean,getContext());
    }

    @Override
    protected String conn() throws Exception {
        return httpConnection.getResposeContent();
    }
}
