package com.gentlehealthcare.mobilecare.net.impl;

import android.content.Context;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.ABRequest;
import com.gentlehealthcare.mobilecare.net.HttpConnection;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.bean.LoadLastDinnerTimeBean;

/**
 * 获取最近一次用餐时间
 * Created by ouyang on 15/8/30.
 */
public class LoadLastDinnerTimeReq extends ABRequest<LoadLastDinnerTimeBean>{
    private HttpConnection httpConnection=null;
    private LoadLastDinnerTimeBean loadLastDinnerTimeBean=null;
    /**
     * @param context        上下文，如果是在子线程中处理结果，此处可传null
     * @param respose        请求结果处理实现
     * @param id             请求的标识，需自己设置
     * @param isInMainThread
     */
    public LoadLastDinnerTimeReq(Context context, IRespose<LoadLastDinnerTimeBean> respose, int id, Boolean isInMainThread,LoadLastDinnerTimeBean loadLastDinnerTimeBean) {
        super(context, respose, id, isInMainThread);
        this.httpConnection=new HttpConnection();
        this.loadLastDinnerTimeBean=loadLastDinnerTimeBean;
    }

    @Override
    protected LoadLastDinnerTimeBean connection() throws Exception {
        return (LoadLastDinnerTimeBean) httpConnection.connection(UrlConstant.LoadLastDinnerTime(),loadLastDinnerTimeBean,getContext());
    }

    @Override
    protected String conn() throws Exception {
        return httpConnection.getResposeContent();
    }
}
