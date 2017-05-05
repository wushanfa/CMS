package com.gentlehealthcare.mobilecare.model.impl;

import com.gentlehealthcare.mobilecare.bean.ImplementationRecordBean;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.model.IInsulinOverviewModel;
import com.gentlehealthcare.mobilecare.tool.CCLog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by zhiwei on 2016/5/18.
 */
public class InsulinOverviewModel implements IInsulinOverviewModel {

    @Override
    public void loadRecord(String patId, String planOrderNo, final onLoadRecord listener) {
        String url = UrlConstant.loadImplementationRecor() + patId + "&planOrderNo=" + planOrderNo;
        CCLog.i("加载>巡视记录>>" + url);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                String result = arg0.result;
                JSONObject mJsonObject = null;
                JSONArray jsonArray = null;
                boolean state = false;
                try {
                    mJsonObject = new JSONObject(result);
                    state = mJsonObject.getBoolean("result");
                    if (state) {
                        jsonArray = mJsonObject.getJSONArray("data");
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<ImplementationRecordBean>>() {
                        }.getType();
                        List<ImplementationRecordBean> implementationRecordBeans = gson.fromJson(jsonArray
                                .toString(), type);
                        listener.onSuccess(implementationRecordBeans);
                    } else {
                        listener.onFailure("数据解析异常", null);
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                    listener.onFailure("数据解析异常", e1);
                }
            }

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                listener.onFailure(arg1, arg0);
            }
        });
    }

    public interface onLoadRecord {

        void onSuccess(List<ImplementationRecordBean> list);

        void onFailure(String msg, Exception e);
    }
}
