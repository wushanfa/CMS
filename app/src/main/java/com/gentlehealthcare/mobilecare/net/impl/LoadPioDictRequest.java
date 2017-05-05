package com.gentlehealthcare.mobilecare.net.impl;

import android.content.Context;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.ABRequest;
import com.gentlehealthcare.mobilecare.net.HttpConnection;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.bean.PioBean;

/**
 * h
 * Created by ouyang on 15/7/3.
 */
public class LoadPioDictRequest extends ABRequest<PioBean> {
    private PioBean pioBean=null;
    private HttpConnection httpConnection=null;
    /**
     * @param context        上下文，如果是在子线程中处理结果，此处可传null
     * @param respose        请求结果处理实现
     * @param id             请求的标识，需自己设置
     * @param isInMainThread
     */
    public LoadPioDictRequest(Context context, IRespose<PioBean> respose, int id, Boolean isInMainThread, PioBean pioBean) {
        super(context, respose, id, isInMainThread);
        httpConnection=new HttpConnection();
        this.pioBean=pioBean;
    }

    @Override
    protected PioBean connection() throws Exception {
        String url="";
        if (pioBean.getType()==0){
            url=UrlConstant.LoadProblemDict();
        }else if (pioBean.getType()==1){
            url=UrlConstant.LoadCausesDict();
        }else if (pioBean.getType()==2){
            url=UrlConstant.LoadTargetDict();
        }else if (pioBean.getType()==3){
            url=UrlConstant.LoadMeasure();
        }else if (pioBean.getType()==4){
            url=UrlConstant.LoadAppraisal();
        }
        return (PioBean) httpConnection.connection(url,pioBean,getContext());
    }

    @Override
    protected String conn() throws Exception {
        return httpConnection.getResposeContent();
    }
}
