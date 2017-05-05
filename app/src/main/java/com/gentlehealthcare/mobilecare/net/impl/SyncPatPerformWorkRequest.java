package com.gentlehealthcare.mobilecare.net.impl;

import android.content.Context;
import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.ABRequest;
import com.gentlehealthcare.mobilecare.net.HttpConnection;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatPerformWork;
import com.gentlehealthcare.mobilecare.tool.DesUtil;

/**
 * Created by ouyang on 2015-04-07.
 */
public class SyncPatPerformWorkRequest extends ABRequest<SyncPatPerformWork> {
    private HttpConnection httpConnection=null;
    private Context context=null;
    private SyncPatPerformWork syncPatPerformWork=null;
    /**
     * @param context        上下文，如果是在子线程中处理结果，此处可传null
     * @param respose        请求结果处理实现
     * @param id             请求的标识，需自己设置
     * @param isInMainThread
     */
    public SyncPatPerformWorkRequest(Context context, IRespose<SyncPatPerformWork> respose, int id, Boolean isInMainThread,SyncPatPerformWork syncPatPerformWork) {
        super(context, respose, id, isInMainThread);
        this.syncPatPerformWork=syncPatPerformWork;
        this.context=context;
        this.httpConnection=new HttpConnection();
    }

    @Override
    protected SyncPatPerformWork connection() throws Exception {
        return (SyncPatPerformWork) httpConnection.connection(UrlConstant.GetSyncPatperformWrokList(),syncPatPerformWork,context);
    }

    @Override
    protected String conn() throws Exception {
       String result=httpConnection.getResposeContent();
        if (UserInfo.getKey() != null && !"".equals(UserInfo.getKey())){
            try {
                result = DesUtil.decrypt(result, UserInfo.getKey());
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }else{
            return result;
        }


    }
}
