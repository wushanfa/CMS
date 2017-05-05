package com.gentlehealthcare.mobilecare.net.impl;

import android.content.Context;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.ABRequest;
import com.gentlehealthcare.mobilecare.net.HttpConnection;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.bean.CheckMedicineBean;

/**
 * Created by ouyang on 2015/3/23.
 */
public class ExecuteMedicineRequest extends ABRequest<CheckMedicineBean>{
    private HttpConnection httpConnection=null;
    private Context context=null;
    private CheckMedicineBean checkMedicineBean=null;
    /**
     * @param context        上下文，如果是在子线程中处理结果，此处可传null
     * @param respose        请求结果处理实现
     * @param id             请求的标识，需自己设置
     * @param isInMainThread
     */
    public ExecuteMedicineRequest(Context context, IRespose<CheckMedicineBean> respose, int id, Boolean isInMainThread,CheckMedicineBean checkMedicineBean) {
        super(context, respose, id, isInMainThread);
        this.checkMedicineBean=checkMedicineBean;
        this.httpConnection=new HttpConnection();
        this.context=context;
    }

    @Override
    protected CheckMedicineBean connection() throws Exception {
        String url="";
        if (checkMedicineBean.getType()==0) {
            url = UrlConstant.GetExecuteMedicine();
        }else if (checkMedicineBean.getType()==1){
            url=UrlConstant.GetInjectInsulin();
        }
        return (CheckMedicineBean) this.httpConnection.connection(url,checkMedicineBean,context);
    }

    @Override
    protected String conn() throws Exception {
        return this.httpConnection.getResposeContent();
    }
}
