package com.gentlehealthcare.mobilecare.net.impl;

import android.content.Context;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.ABRequest;
import com.gentlehealthcare.mobilecare.net.HttpConnection;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.bean.LoadTaskByCategoryRecordBean;

/**
 * Created by ouyang on 15/9/2.
 */
public class LoadTaskByCategoryRecordReq extends ABRequest<LoadTaskByCategoryRecordBean> {
    private LoadTaskByCategoryRecordBean loadTaskByCategoryRecordBean=null;
    private HttpConnection httpConnection=null;
    /**
     * @param context        上下文，如果是在子线程中处理结果，此处可传null
     * @param respose        请求结果处理实现
     * @param id             请求的标识，需自己设置
     * @param isInMainThread
     */
    public LoadTaskByCategoryRecordReq(Context context, IRespose<LoadTaskByCategoryRecordBean> respose, int id, Boolean isInMainThread,LoadTaskByCategoryRecordBean loadTaskByCategoryRecordBean) {
        super(context, respose, id, isInMainThread);
        this.httpConnection=new HttpConnection();
        this.loadTaskByCategoryRecordBean=loadTaskByCategoryRecordBean;
    }

    @Override
    protected LoadTaskByCategoryRecordBean connection() throws Exception {
        return (LoadTaskByCategoryRecordBean) httpConnection.connection(UrlConstant.LoadTaskByCategoryRecord(),loadTaskByCategoryRecordBean,getContext());
    }

    @Override
    protected String conn() throws Exception {
        return httpConnection.getResposeContent();
    }
}
