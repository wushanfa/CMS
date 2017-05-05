package com.gentlehealthcare.mobilecare.net.impl;

import android.content.Context;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.ABRequest;
import com.gentlehealthcare.mobilecare.net.HttpConnection;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.bean.RecordFirstPageSignBean;

/**
 * Created by ouyang on 15/6/2.
 */
public class RecordFirstPageSignRequest extends ABRequest<RecordFirstPageSignBean> {
    private HttpConnection httpConnection;
    private Context context;
    private RecordFirstPageSignBean recordFirstPageSignBean;

    /**
     * @param context        上下文，如果是在子线程中处理结果，此处可传null
     * @param respose        请求结果处理实现
     * @param id             请求的标识，需自己设置
     * @param isInMainThread
     */
    public RecordFirstPageSignRequest(Context context, IRespose<RecordFirstPageSignBean> respose, int id, Boolean isInMainThread,RecordFirstPageSignBean recordFirstPageSignBean) {
        super(context, respose, id, isInMainThread);
        this.httpConnection=new HttpConnection();
        this.context=context;
        this.recordFirstPageSignBean=recordFirstPageSignBean;
    }

    @Override
    protected RecordFirstPageSignBean connection() throws Exception {
        return (RecordFirstPageSignBean) httpConnection.connection(UrlConstant.RecordFirstPageSigns(),recordFirstPageSignBean,context);
    }

    @Override
    protected String conn() throws Exception {
        return httpConnection.getResposeContent();
    }
}
