package com.gentlehealthcare.mobilecare.net.impl;

import android.content.Context;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.ABRequest;
import com.gentlehealthcare.mobilecare.net.HttpConnection;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.bean.ExecuteMedicineInfo;

/**
 * 护理记录 请求
 * Created by ouyang on 15/7/7.
 */
public class LoadPlanNursingRec extends ABRequest<ExecuteMedicineInfo> {
    private ExecuteMedicineInfo executeMedicineInfo;
    private HttpConnection httpConnection;
    /**
     * @param context        上下文，如果是在子线程中处理结果，此处可传null
     * @param respose        请求结果处理实现
     * @param id             请求的标识，需自己设置
     * @param isInMainThread
     */
    public LoadPlanNursingRec(Context context, IRespose<ExecuteMedicineInfo> respose, int id, Boolean isInMainThread,ExecuteMedicineInfo executeMedicineInfo) {
        super(context, respose, id, isInMainThread);
        this.executeMedicineInfo=executeMedicineInfo;
        this.httpConnection=new HttpConnection();
    }

    @Override
    protected ExecuteMedicineInfo connection() throws Exception {
        return (ExecuteMedicineInfo) httpConnection.connection(UrlConstant.LoadPlanNursingRec(),executeMedicineInfo,getContext());
    }

    @Override
    protected String conn() throws Exception {
        return httpConnection.getResposeContent();
    }
}
