package com.gentlehealthcare.mobilecare.net.impl;

import android.content.Context;

import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.ABRequest;
import com.gentlehealthcare.mobilecare.net.HttpConnection;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.bean.SearchICUBBean;

/**
 * @author dzw
 */

public class SearchICUBRequest extends ABRequest<SearchICUBBean> {

    private SearchICUBBean bean;
    private HttpConnection connection;
    private Context context;

    /**
     * @param context        上下文，如果是在子线程中处理结果，此处可传null
     * @param respose        请求结果处理实现
     * @param id             请求的标识，需自己设置
     * @param isInMainThread
     */
    public SearchICUBRequest(Context context, IRespose<SearchICUBBean> respose, int id, Boolean
            isInMainThread, SearchICUBBean bean) {
        super(context, respose, id, isInMainThread);
        this.connection = new HttpConnection();
        this.context = context;
        this.bean = bean;
    }

    @Override
    protected SearchICUBBean connection() throws Exception {
        return (SearchICUBBean) connection.connection(UrlConstant.searchICUB(), bean, context);
    }

    @Override
    protected String conn() throws Exception {
        return connection.getResposeContent();
    }

}
