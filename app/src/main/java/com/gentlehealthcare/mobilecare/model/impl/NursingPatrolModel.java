package com.gentlehealthcare.mobilecare.model.impl;

import com.gentlehealthcare.mobilecare.bean.orders.DictCommonBean;
import com.gentlehealthcare.mobilecare.bean.orders.OrderListBean;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.model.INursingPatrolModel;
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
 * Created by zyy on 2016/5/6.
 */
public class NursingPatrolModel implements INursingPatrolModel {

    private static final String TAG = "PatientModel";

    /**
     * 同步部门病人
     */
    @Override
    public void getPatrolDict(String str, final OnLoadPatrolDictListener listListener) {
        String url = UrlConstant.loadNursingPatrolDict();
        CCLog.i("加载护理巡视字典>>>"+url);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                boolean result = false;
                try {
                    JSONObject jsonObject = new JSONObject(responseInfo.result);
                    result = jsonObject.getBoolean("result");
                    if (result) {
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<DictCommonBean>>() {
                        }.getType();
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        List<DictCommonBean> list = gson.fromJson(jsonArray.toString(), type);
                        listListener.onSuccess(list);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {

            }
        });

    }

    @Override
    public void DoLoadOrder(String username, String patId, String planTime, String orderType, String status,
                            String templateId, String orderClass, final OnLoadOrdersListListener listener) {
        String url = UrlConstant.loadNewOrders() + username + "&patId=" + patId + "&planTime=" + planTime +
                "&orderType=" + orderType + "&status=" + status + "&orderClass=" + orderClass + "&templateId=" +
                templateId;
        CCLog.i("医嘱加载>>>" + url);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                listener.onOrderFailure(arg1, arg0);
            }

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
                        jsonArray = mJsonObject.getJSONArray("order");
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<OrderListBean>>() {
                        }.getType();
                        List<OrderListBean> orderListBeans = gson.fromJson(jsonArray.toString(), type);
                        listener.onOrderSuccess(orderListBeans);
                    } else {
                        listener.onOrderFailure("数据解析异常", null);
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                    listener.onOrderFailure("数据解析异常", e1);
                }
            }

        });
    }

    @Override
    public void saveNursingPatrol(String patId,String wardCode, String performType, String nurseInOperate, final SaveNursingPatrolListener listener) {
        String url = UrlConstant.saveNursingPatrol() + patId +"&wardCode="+wardCode+ "&performType=" + UrlTool.transWord(performType) + "&nurseInOperate=" + UrlTool.transWord(nurseInOperate);
        CCLog.i("护理巡视保存>>>" + url);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    boolean result=false;
                    JSONObject jsonObject=new JSONObject(responseInfo.result);
                    result=jsonObject.getBoolean("result");
                    if(result){
                        listener.onSuccess(true);
                    }else{
                        listener.onFailure(GlobalConstant.FAILED, null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                listener.onFailure(GlobalConstant.FAILED, null);
            }
        });
    }

    public interface OnLoadPatrolDictListener {
        void onSuccess(List<DictCommonBean> list);

        void onFailure(String msg, Exception e);
    }

    public interface OnLoadOrdersListListener {
        void onOrderSuccess(List<OrderListBean> list);

        void onOrderFailure(String msg, Exception e);
    }

    public interface SaveNursingPatrolListener {
        void onSuccess(boolean msg);

        void onFailure(String msg, Exception e);
    }

}
