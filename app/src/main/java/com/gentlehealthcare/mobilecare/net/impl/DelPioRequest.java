package com.gentlehealthcare.mobilecare.net.impl;

import android.content.Context;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.ABRequest;
import com.gentlehealthcare.mobilecare.net.HttpConnection;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.bean.DeleteAndModPioBean;

/**
 * 记录PIO数据
 * Created by ouyang on 15/7/3.
 */
public class DelPioRequest extends ABRequest<DeleteAndModPioBean> {
    private HttpConnection httpConnection=null;
    private DeleteAndModPioBean dmPioBean;
    /**
     * @param context        上下文，如果是在子线程中处理结果，此处可传null
     * @param respose        请求结果处理实现
     * @param id             请求的标识，需自己设置
     * @param isInMainThread
     */
    public DelPioRequest(Context context, IRespose<DeleteAndModPioBean> respose, int id, Boolean isInMainThread,DeleteAndModPioBean dmPioBean) {
        super(context, respose, id, isInMainThread);
        this.dmPioBean=dmPioBean;
        this.httpConnection=new HttpConnection();
    }

    @Override
    protected DeleteAndModPioBean connection() throws Exception {
        return (DeleteAndModPioBean) this.httpConnection.connection(UrlConstant.delAndModPio(),dmPioBean,getContext());
    }

    @Override
    protected String conn() throws Exception {
        return this.httpConnection.getResposeContent();
    }
}
