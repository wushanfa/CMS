package com.gentlehealthcare.mobilecare.net.bean;

import android.content.Context;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.ABRequest;
import com.gentlehealthcare.mobilecare.net.HttpConnection;
import com.gentlehealthcare.mobilecare.net.IRespose;

/**
 * 加载异常字典请求
 * Created by ouyang on 15/8/28.
 */
public class LoadVariationDictReq extends ABRequest<LoadVariationDictBean> {
    private HttpConnection httpConnection=null;
    private LoadVariationDictBean loadVariationDictBean=null;

    /**
     * @param context        上下文，如果是在子线程中处理结果，此处可传null
     * @param respose        请求结果处理实现
     * @param id             请求的标识，需自己设置
     * @param isInMainThread
     */
    public LoadVariationDictReq(Context context, IRespose<LoadVariationDictBean> respose, int id, Boolean isInMainThread,LoadVariationDictBean loadVariationDictBean) {
        super(context, respose, id, isInMainThread);
        this.loadVariationDictBean=loadVariationDictBean;
        this.httpConnection=new HttpConnection();
    }

    @Override
    protected LoadVariationDictBean connection() throws Exception {
        return (LoadVariationDictBean) httpConnection.connection(UrlConstant.LoadVariationDict(loadVariationDictBean.getType()),loadVariationDictBean,getContext());
    }

    @Override
    protected String conn() throws Exception {
        return httpConnection.getResposeContent();
    }
}
