package com.gentlehealthcare.mobilecare.net.impl;

import android.content.Context;

import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.ABRequest;
import com.gentlehealthcare.mobilecare.net.HttpConnection;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.bean.LoadIntravsSnapshotBean;

/**
 *
 * Created by ouyang on 15/8/27.
 */
public class LoadIntravsSnapshotRequest extends ABRequest<LoadIntravsSnapshotBean> {
    private HttpConnection httpConnection=null;
    private LoadIntravsSnapshotBean loadIntravsSnapshotBean=null;
    /**
     * @param context        上下文，如果是在子线程中处理结果，此处可传null
     * @param respose        请求结果处理实现
     * @param id             请求的标识，需自己设置
     * @param isInMainThread
     */
    public LoadIntravsSnapshotRequest(Context context, IRespose<LoadIntravsSnapshotBean> respose, int id, Boolean isInMainThread,LoadIntravsSnapshotBean loadIntravsSnapshotBean) {
        super(context, respose, id, isInMainThread);
        this.httpConnection=new HttpConnection();
        this.loadIntravsSnapshotBean=loadIntravsSnapshotBean;
    }



    @Override
    protected LoadIntravsSnapshotBean connection() throws Exception {
        return (LoadIntravsSnapshotBean) httpConnection.connection(UrlConstant.GetIntravsSnapshot(loadIntravsSnapshotBean.getType()),loadIntravsSnapshotBean,getContext());
    }

    @Override
    protected String conn() throws Exception {
        return httpConnection.getResposeContent();
    }
}
