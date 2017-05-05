package com.gentlehealthcare.mobilecare.net.impl;

import android.content.Context;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.ABRequest;
import com.gentlehealthcare.mobilecare.net.HttpConnection;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.bean.CompleteExecuteBean;

/**
 * Created by ouyang on 2015/3/24.
 */
public class CancelExecuteRequest extends ABRequest<CompleteExecuteBean> {
    private HttpConnection httpConnection=null;
    private Context context=null;
    private CompleteExecuteBean completeExecuteBean=null;
    /**
     * @param context        上下文，如果是在子线程中处理结果，此处可传null
     * @param respose        请求结果处理实现
     * @param id             请求的标识，需自己设置
     * @param isInMainThread
     */
    public CancelExecuteRequest(Context context, IRespose<CompleteExecuteBean> respose, int id, Boolean isInMainThread,CompleteExecuteBean completeExecuteBean) {
        super(context, respose, id, isInMainThread);
        this.httpConnection=new HttpConnection();
        this.context=context;
        this.completeExecuteBean=completeExecuteBean;

    }

    @Override
    protected CompleteExecuteBean connection() throws Exception {
        return (CompleteExecuteBean) httpConnection.connection(UrlConstant.GetCancelExecute(completeExecuteBean.getType()),this.completeExecuteBean,this.context);
    }

    @Override
    protected String conn() throws Exception {
        return httpConnection.getResposeContent();
    }
}
