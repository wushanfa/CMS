package com.gentlehealthcare.mobilecare.net.impl;

import android.content.Context;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.ABRequest;
import com.gentlehealthcare.mobilecare.net.HttpConnection;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.bean.PatMajorInfoBean;
import com.gentlehealthcare.mobilecare.tool.CCLog;

/**
 * Created by ouyang on 2015/3/25.
 */
public class SyncPatMajorInfoRequest extends ABRequest<PatMajorInfoBean> {
    private HttpConnection httpConnection;
    private PatMajorInfoBean patMajorInfoBean;
    private Context context;
    /**
     * @param context        上下文，如果是在子线程中处理结果，此处可传null
     * @param respose        请求结果处理实现
     * @param id             请求的标识，需自己设置
     * @param isInMainThread
     */
    public SyncPatMajorInfoRequest(Context context, IRespose<PatMajorInfoBean> respose, int id, Boolean isInMainThread, PatMajorInfoBean patMajorInfoBean) {
        super(context, respose, id, isInMainThread);
        this.httpConnection=new HttpConnection();
        this.patMajorInfoBean=patMajorInfoBean;
        this.context=context;
    }

    @Override
    protected PatMajorInfoBean connection() throws Exception {
        String url="";
        if (patMajorInfoBean.getType()==0){
            url=UrlConstant.GetOrders(patMajorInfoBean.getType());
            CCLog.i("请求 0", "0type");
        }else{
            url=UrlConstant.LoadPatInsulinPlansByPatId();
        }
        return (PatMajorInfoBean) httpConnection.connection(url,patMajorInfoBean,context);
    }

    @Override
    protected String conn() throws Exception {
        return httpConnection.getResposeContent();
    }
}

