package com.gentlehealthcare.mobilecare.net.impl;

import android.content.Context;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.ABRequest;
import com.gentlehealthcare.mobilecare.net.HttpConnection;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.bean.PatrolBean;

/**
 * 巡视请求
 * Created by ouyang on 2015/3/23.
 */
public class PatrolRequest extends ABRequest<PatrolBean> {
    private HttpConnection httpConnection=null;
    private Context context=null;
    private PatrolBean patrolBean=null;
    /**
     * @param context        上下文，如果是在子线程中处理结果，此处可传null
     * @param respose        请求结果处理实现
     * @param id             请求的标识，需自己设置
     * @param isInMainThread
     */
    public PatrolRequest(Context context, IRespose<PatrolBean> respose, int id, Boolean isInMainThread,PatrolBean patrolBean) {
        super(context, respose, id, isInMainThread);
        this.httpConnection=new HttpConnection();
        this.context=context;
        this.patrolBean=patrolBean;
    }

    @Override
    protected PatrolBean connection() throws Exception {
        return (PatrolBean) this.httpConnection.connection(UrlConstant.GetPatrol(patrolBean.getType()),this.patrolBean,this.context);
    }

    @Override
    protected String conn() throws Exception {
        return this.httpConnection.getResposeContent();
    }
}
