package com.gentlehealthcare.mobilecare.model.impl;

import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.model.IPatientModel;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by zyy on 2016/5/6.
 */
public class PatientModel implements IPatientModel {

    private static final String TAG = "PatientModel";
    /**
     * 同步部门病人
     */
    @Override
    public void syncDeptPatientTable(String url,final OnLoadPatientListListener listListener) {

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                result.replaceAll("null", "\"\"");
                Gson gson = new Gson();
                Type type = new TypeToken<List<SyncPatientBean>>() {
                }.getType();
                List<SyncPatientBean> list = gson.fromJson(result, type);
                UserInfo.setDeptPatient(list);
                listListener.onSuccess(list);
            }

            @Override
            public void onFailure(HttpException error, String msg) {

            }
        });

    }

    public interface OnLoadPatientListListener {
        void onSuccess(List<SyncPatientBean> list);
        void onFailure(String msg, Exception e);
    }

}
