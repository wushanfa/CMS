package com.gentlehealthcare.mobilecare.net.impl;

import android.content.Context;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.ABRequest;
import com.gentlehealthcare.mobilecare.net.HttpConnection;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.bean.RecordSecondPageSignBean;

/**
 * 体征二记录
 * Created by ouyang on 15/6/3.
 */
public class RecordSecondPageSignRequest extends ABRequest<RecordSecondPageSignBean> {
    private HttpConnection httpConnection=null;
    private Context context;
    private RecordSecondPageSignBean recordSecondPageSignBean;
    /**
     * @param context        上下文，如果是在子线程中处理结果，此处可传null
     * @param respose        请求结果处理实现
     * @param id             请求的标识，需自己设置
     * @param isInMainThread
     */
    public RecordSecondPageSignRequest(Context context, IRespose<RecordSecondPageSignBean> respose, int id, Boolean isInMainThread,RecordSecondPageSignBean recordSecondPageSignBean) {
        super(context, respose, id, isInMainThread);
        this.httpConnection=new HttpConnection();
        this.context=context;
        this.recordSecondPageSignBean=recordSecondPageSignBean;
    }

    @Override
    protected RecordSecondPageSignBean connection() throws Exception {
        return (RecordSecondPageSignBean) httpConnection.connection(UrlConstant.RecordSecondPageSigns(),recordSecondPageSignBean,context);
    }

    @Override
    protected String conn() throws Exception {
        return httpConnection.getResposeContent();
    }
}
