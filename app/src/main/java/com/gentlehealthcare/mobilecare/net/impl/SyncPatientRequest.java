package com.gentlehealthcare.mobilecare.net.impl;


import android.content.Context;

import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.ABRequest;
import com.gentlehealthcare.mobilecare.net.HttpConnection;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;

import java.util.List;

/**
 * 
 * @ClassName: SyncPatientRequest 
 * @Description: 同步病人数据请求
 * @author ouyang
 * @date 2015年3月6日 下午5:50:08 
 *
 */
public class SyncPatientRequest extends ABRequest<SyncPatientBean> {

	private SyncPatientBean syncPatientBean;
	private HttpConnection connection;
	private Context context;
    private List<SyncPatientBean> syncPatientBeanList;
	public SyncPatientRequest(Context context,
			IRespose<SyncPatientBean> respose, int id, Boolean isInMainThread,SyncPatientBean syncPatientBean) {
		super(context, respose, id, isInMainThread);
		// TODO Auto-generated constructor stub
		this.context=context;
		this.connection=new HttpConnection();
		this.syncPatientBean=syncPatientBean;
	}

	@Override
	protected SyncPatientBean connection() throws Exception {
		// TODO Auto-generated method stub
        List<SyncPatientBean> list=(List<SyncPatientBean>) connection.connection(UrlConstant.GetSyncPatient(), syncPatientBean, context);
        setSyncPatientBeanList(list);

        if (getSyncPatientBeanList()==null||getSyncPatientBeanList().size()<=0)
            return null;
        else
        return getSyncPatientBeanList().get(0);
	}

    @Override
    protected String conn() throws Exception {
        return connection.getResposeContent();
    }

    public List<SyncPatientBean> getSyncPatientBeanList() {
        return syncPatientBeanList;
    }

    public void setSyncPatientBeanList(List<SyncPatientBean> syncPatientBeanList) {
        this.syncPatientBeanList = syncPatientBeanList;
    }
}
