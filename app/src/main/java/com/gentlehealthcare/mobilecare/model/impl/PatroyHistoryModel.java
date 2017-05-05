package com.gentlehealthcare.mobilecare.model.impl;

import com.gentlehealthcare.mobilecare.bean.NursingHistoryBean;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.model.IPatrolHistoryModel;
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
 * Created by Zyy on 2016/9/5.
 * 类说明：巡视历史记录model层
 */

public class PatroyHistoryModel implements IPatrolHistoryModel {

    @Override
    public void getHistory(String patId, String wardCode, final getPatrolHistoryListener listener) {
        HttpUtils http = new HttpUtils();
        String url=UrlConstant.getNursingPatrolHistroy()+patId+"&wardCode="+wardCode;
        CCLog.i("获取患者历史巡视记录>>>"+url);
        http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    boolean result=false;
                    JSONObject obj=new JSONObject(responseInfo.result);
                    result=obj.getBoolean("result");
                    if(result){
                        Type type=new TypeToken<List<NursingHistoryBean >>(){}.getType();
                        Gson gson=new Gson();
                        JSONArray jsonArray=obj.getJSONArray("data");
                        List<NursingHistoryBean> list=gson.fromJson(jsonArray.toString(),type);
                        listener.getPatrolHistorySucceed(list);
                    }else{
                        listener.getPatrolHistoryFailed(GlobalConstant.FAILED, null);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.getPatrolHistoryFailed(GlobalConstant.FAILED, e);
                }

            }

            @Override
            public void onFailure(HttpException e, String s) {
                listener.getPatrolHistoryFailed(GlobalConstant.FAILED, e);
            }
        });
    }

    public interface getPatrolHistoryListener {

        void getPatrolHistorySucceed(List<NursingHistoryBean> msg);

        void getPatrolHistoryFailed(String msg, Exception e);
    }
}
