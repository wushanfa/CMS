package com.gentlehealthcare.mobilecare.net.impl;

import android.content.Context;

import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.ABRequest;
import com.gentlehealthcare.mobilecare.net.HttpConnection;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.bean.UpdateICUBBean;

/**
 * @author dzw
 */

public class UpdateICUBRequest extends ABRequest<UpdateICUBBean> {

    private UpdateICUBBean bean;
    private HttpConnection connection;
    private Context context;

    /**
     * @param context        上下文，如果是在子线程中处理结果，此处可传null
     * @param respose        请求结果处理实现
     * @param id             请求的标识，需自己设置
     * @param isInMainThread
     */
    public UpdateICUBRequest(Context context, IRespose<UpdateICUBBean> respose, int id, Boolean
            isInMainThread, UpdateICUBBean bean) {
        super(context, respose, id, isInMainThread);
        this.connection = new HttpConnection();
        this.context = context;
        this.bean = bean;
    }

    @Override
    protected UpdateICUBBean connection() throws Exception {
        return (UpdateICUBBean) connection.connection(UrlConstant.updateICUBRecord(), bean, context);
    }

    @Override
    protected String conn() throws Exception {
        return connection.getResposeContent();
    }

}
