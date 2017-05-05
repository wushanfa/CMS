package com.gentlehealthcare.mobilecare.net.impl;

import android.content.Context;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.ABRequest;
import com.gentlehealthcare.mobilecare.net.HttpConnection;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.bean.RecordInspectionTimeBean;

/**
 * Created by ouyang on 15/7/16.
 */
public class RecordInspectionTimeReq extends ABRequest<RecordInspectionTimeBean> {
    private HttpConnection httpConnection=null;
    private RecordInspectionTimeBean recordInspectionTimeBean=null;
    /**
     * @param context        上下文，如果是在子线程中处理结果，此处可传null
     * @param respose        请求结果处理实现
     * @param id             请求的标识，需自己设置
     * @param isInMainThread
     */
    public RecordInspectionTimeReq(Context context, IRespose<RecordInspectionTimeBean> respose, int id, Boolean isInMainThread,RecordInspectionTimeBean recordInspectionTimeBean) {
        super(context, respose, id, isInMainThread);
        this.recordInspectionTimeBean=recordInspectionTimeBean;
        this.httpConnection=new HttpConnection();
    }

    @Override
    protected RecordInspectionTimeBean connection() throws Exception {
        return (RecordInspectionTimeBean) httpConnection.connection(UrlConstant.RecordInspectionTime(recordInspectionTimeBean.getType()),recordInspectionTimeBean,getContext());
    }

    @Override
    protected String conn() throws Exception {
        return httpConnection.getResposeContent();
    }
}
