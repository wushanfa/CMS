package com.gentlehealthcare.mobilecare.net.impl;

import android.content.Context;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.ABRequest;
import com.gentlehealthcare.mobilecare.net.HttpConnection;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.bean.CheckMedicineBean;

/**
 * Created by ouyang on 2015/3/20.
 */
public class CheckMedicineRequest extends ABRequest<CheckMedicineBean> {
    private Context context=null;
    private CheckMedicineBean checkMedicineBean=null;
    private HttpConnection httpConnection=null;

    /**
     * @param context        上下文，如果是在子线程中处理结果，此处可传null
     * @param respose        请求结果处理实现
     * @param id             请求的标识，需自己设置
     * @param isInMainThread
     */
    public CheckMedicineRequest(Context context, IRespose<CheckMedicineBean> respose, int id, Boolean isInMainThread,CheckMedicineBean checkMedicineBean) {
        super(context, respose, id, isInMainThread);
    this.context=context;
        this.checkMedicineBean=checkMedicineBean;
        this.httpConnection=new HttpConnection();
    }

    @Override
    protected CheckMedicineBean connection() throws Exception {
        return (CheckMedicineBean) this.httpConnection.connection(UrlConstant.GetCheckMedicine(checkMedicineBean.getType()),checkMedicineBean,context);
    }

    @Override
    protected String conn() throws Exception {
        return this.httpConnection.getResposeContent();
    }
}
