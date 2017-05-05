package com.gentlehealthcare.mobilecare.net.impl;

import android.content.Context;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.ABRequest;
import com.gentlehealthcare.mobilecare.net.HttpConnection;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.bean.CompleteExecuteBean;

/**
 * 完成任务请求
 * Created by ouyang on 2015/3/24.
 */
public class CompleteExecuteRequest extends ABRequest<CompleteExecuteBean> {
    private HttpConnection httpConnection=null;
    private CompleteExecuteBean completeExecuteBean=null;
    private Context context;

    /**
     * @param context        上下文，如果是在子线程中处理结果，此处可传null
     * @param respose        请求结果处理实现
     * @param id             请求的标识，需自己设置
     * @param isInMainThread
     */
    public CompleteExecuteRequest(Context context, IRespose<CompleteExecuteBean> respose, int id, Boolean isInMainThread,CompleteExecuteBean completeExecuteBean) {
        super(context, respose, id, isInMainThread);
        this.completeExecuteBean=completeExecuteBean;
        this.httpConnection=new HttpConnection();
        this.context=context;
    }

    @Override
    protected CompleteExecuteBean connection() throws Exception {
        return (CompleteExecuteBean) this.httpConnection.connection(UrlConstant.GetCompleteExecute(completeExecuteBean.getType()),completeExecuteBean,context);
    }

    @Override
    protected String conn() throws Exception {
        return httpConnection.getResposeContent();
    }
}
