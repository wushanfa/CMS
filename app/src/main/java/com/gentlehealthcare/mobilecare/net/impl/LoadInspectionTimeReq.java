package com.gentlehealthcare.mobilecare.net.impl;

import android.content.Context;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.ABRequest;
import com.gentlehealthcare.mobilecare.net.HttpConnection;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.bean.LoadInspectionTimeBean;

/**
 * 巡视界面 获取巡视时间和注射时间
 * Created by ouyang on 15/8/28.
 */
public class LoadInspectionTimeReq extends ABRequest<LoadInspectionTimeBean> {
    private HttpConnection httpConnection=null;
    private LoadInspectionTimeBean loadInspectionTimeBean=null;
    /**
     * @param context        上下文，如果是在子线程中处理结果，此处可传null
     * @param respose        请求结果处理实现
     * @param id             请求的标识，需自己设置
     * @param isInMainThread
     */
    public LoadInspectionTimeReq(Context context, IRespose<LoadInspectionTimeBean> respose, int id, Boolean isInMainThread,LoadInspectionTimeBean loadInspectionTimeBean) {
        super(context, respose, id, isInMainThread);
        this.httpConnection=new HttpConnection();
        this.loadInspectionTimeBean=loadInspectionTimeBean;
    }

    @Override
    protected LoadInspectionTimeBean connection() throws Exception {
        return (LoadInspectionTimeBean) httpConnection.connection(UrlConstant.GetInspectionTime(loadInspectionTimeBean.getType()),loadInspectionTimeBean,getContext());
    }

    @Override
    protected String conn() throws Exception {
        return httpConnection.getResposeContent();
    }
}
