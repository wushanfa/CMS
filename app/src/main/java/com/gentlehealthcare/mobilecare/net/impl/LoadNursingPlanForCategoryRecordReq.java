package com.gentlehealthcare.mobilecare.net.impl;

import android.content.Context;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.ABRequest;
import com.gentlehealthcare.mobilecare.net.HttpConnection;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.bean.LoadNursingPlanForCategoryRecordBean;

/**
 * Created by ouyang on 15/9/2.
 */
public class LoadNursingPlanForCategoryRecordReq extends ABRequest<LoadNursingPlanForCategoryRecordBean> {
    private HttpConnection httpConnection=null;
    private LoadNursingPlanForCategoryRecordBean loadNursingPlanForCategoryRecordBean=null;
    /**
     * @param context        上下文，如果是在子线程中处理结果，此处可传null
     * @param respose        请求结果处理实现
     * @param id             请求的标识，需自己设置
     * @param isInMainThread
     */
    public LoadNursingPlanForCategoryRecordReq(Context context, IRespose<LoadNursingPlanForCategoryRecordBean> respose, int id, Boolean isInMainThread,LoadNursingPlanForCategoryRecordBean loadNursingPlanForCategoryRecordBean) {
        super(context, respose, id, isInMainThread);
        this.loadNursingPlanForCategoryRecordBean=loadNursingPlanForCategoryRecordBean;
        this.httpConnection=new HttpConnection();
    }

    @Override
    protected LoadNursingPlanForCategoryRecordBean connection() throws Exception {
        return (LoadNursingPlanForCategoryRecordBean) this.httpConnection.connection(UrlConstant.LoadNursingPlanForCategoryRecord(),loadNursingPlanForCategoryRecordBean,getContext());
    }

    @Override
    protected String conn() throws Exception {
        return httpConnection.getResposeContent();
    }
}
