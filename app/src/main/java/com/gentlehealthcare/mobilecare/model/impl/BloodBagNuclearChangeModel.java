package com.gentlehealthcare.mobilecare.model.impl;

import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.model.IBloodBagNuclearChangeModel;
import com.gentlehealthcare.mobilecare.net.bean.BloodProductBean2;
import com.gentlehealthcare.mobilecare.tool.CCLog;
import com.gentlehealthcare.mobilecare.tool.UrlTool;
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
 * 类说明：
 */

public class BloodBagNuclearChangeModel implements IBloodBagNuclearChangeModel {


    @Override
    public void BloodBagNuclearChange(String code, String user,String username, String wardCode, final BloodBagNuclearChangeModelListener listener) {
        HttpUtils http=new HttpUtils();
        String url= UrlConstant.bloodBagNuclearChange()+username+"&user="+ UrlTool.transWord(user)+"&code="+ UrlTool.transWord(code)+"&wardCode="+UrlTool.transWord(wardCode);
        CCLog.i("核收血>>>",url);
        http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    boolean result=false;
                    JSONObject obj=new JSONObject(responseInfo.result);
                    result=obj.getBoolean("result");
                    if(result){
                        listener.bloodBagNuclearChangeModelSucessed(null);
                    }else{
                        listener.bloodBagNuclearChangeModelFailed(GlobalConstant.FAILED,null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.bloodBagNuclearChangeModelFailed(GlobalConstant.FAILED,e);
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                listener.bloodBagNuclearChangeModelFailed(GlobalConstant.FAILED,e);
            }
        });
    }

    @Override
    public void loaddatas(String wardCode, String status, final loadDatasSuccessedlListener listener) {
        HttpUtils http=new HttpUtils();
        String url= UrlConstant.collect()+UrlTool.transWord(wardCode)+"&status="+ status;
        CCLog.i("加载核收数据>>>",url);
        http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    boolean result=false;
                    JSONObject obj=new JSONObject(responseInfo.result);
                    result=obj.getBoolean("result");
                    if(result){
                        JSONArray jsonArry=obj.getJSONArray("orders");
                        Gson gson=new Gson();
                        Type type=new TypeToken<List<BloodProductBean2>>(){}.getType();
                        List<BloodProductBean2> bloodProductBean2s=gson.fromJson(jsonArry.toString()
                                ,type);
                        listener.loadDatasSucessed(bloodProductBean2s);
                    }else{
                        listener.loadDatasFailed(GlobalConstant.FAILED,null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.loadDatasFailed(GlobalConstant.EXCEPTION,e);
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                listener.loadDatasFailed(GlobalConstant.EXCEPTION,e);
            }
        });
    }

    @Override
    public void bloodBack(String wardCode, String username, String logId, final bloodBackListener listener) {
        HttpUtils http=new HttpUtils();
        String url= UrlConstant.bloodBack()+username+"&wardCode="+UrlTool.transWord(wardCode)+"&logId="+logId;
        CCLog.i("退回血库>>>",url);
        http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    boolean result=false;
                    JSONObject obj=new JSONObject(responseInfo.result);
                    result=obj.getBoolean("result");
                    if(result){
                        listener.bloodBackSucessed(true);
                    }else{
                        listener.bloodBackFailed(GlobalConstant.FAILED,null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.bloodBackFailed(GlobalConstant.FAILED,e);
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                listener.bloodBackFailed(GlobalConstant.FAILED,e);
            }
        });
    }

    public interface BloodBagNuclearChangeModelListener {
        void bloodBagNuclearChangeModelSucessed(List<BloodProductBean2> list);

        void bloodBagNuclearChangeModelFailed(String msg, Exception e);
    }

    public interface loadDatasSuccessedlListener {
        void loadDatasSucessed(List<BloodProductBean2> list);

        void loadDatasFailed(String msg, Exception e);
    }

    public interface bloodBackListener {
        void bloodBackSucessed(boolean flag);

        void bloodBackFailed(String msg, Exception e);
    }
}
