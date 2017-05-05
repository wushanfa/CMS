package com.gentlehealthcare.mobilecare.model.impl;


import com.gentlehealthcare.mobilecare.bean.orders.OrderListBean;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.model.IOrdersCheckModel;
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
 * Created by zyy on 2016/5/5.
 *
 * @desp 执行核对数据层
 */
public class OrdersCheckModel implements IOrdersCheckModel {

    @Override
    public void DoLoadOrder(String username, String patId, String planTime, String status, final OnLoadOrdersListListener listener) {
        String url = UrlConstant.loadCheckOrders() + username + "&patId=" + patId + "&planTime=" + planTime + "&status=" + status;
        CCLog.i("执行核对医嘱加载>>>" + url);
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
    public void findPatId(String barCode,String userName,String wardCode, final findPatIdListener listener) {
            String url = UrlConstant.findPatIdByBarcode() + UrlTool.transWord(barCode)+"&userName="+UrlTool.transWord(userName)+"&wardCode="+UrlTool.transWord(wardCode);
        CCLog.i("根据医嘱码查找病人>>>" + url);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    JSONObject jsonbject=new JSONObject(responseInfo.result);
                    JSONArray jsonArray;
                    boolean resuslt=jsonbject.getBoolean("result");
                    jsonArray=jsonbject.getJSONArray("data");
                    if (resuslt) {

                        Gson gson = new Gson();
                        Type type = new TypeToken<List<OrderListBean>>() {
                        }.getType();
                        List<OrderListBean> orderListBeans = gson.fromJson(jsonArray.toString(), type);
                        listener.findPatIdSuccess(orderListBeans);
                    }else{
                        listener.findPatIdFailure("no",null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                listener.findPatIdFailure("failed",null);
            }
        });
    }

    @Override
    public void checkOrder(String wardCode,String patId, String relatedBarcode,String relatedBarcode2, String checkBy, String planOrderNo, final checkOrderListener listener) {
        String url = UrlConstant.checkOrder() + patId+"&wardCode="+wardCode+"&relatedBarcode="+ UrlTool.transWord(relatedBarcode)+"&relatedBarcode2="+ UrlTool.transWord(relatedBarcode2)+"&checkBy="+UrlTool.transWord(checkBy)+"&planOrderNo="+UrlTool.transWord(planOrderNo);
        CCLog.i("医嘱核对>>>" + url);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    JSONObject jsonObject=new JSONObject(responseInfo.result);
                    boolean result=jsonObject.getBoolean("result");
                    if(result){
                        String flag=jsonObject.getString(GlobalConstant.FLAG);
                        if (GlobalConstant.HAVE_BEEN_IMPLEMENTED.equals(flag)) {//已执行
                            listener.checkOrderSuccess(GlobalConstant.HAVE_BEEN_IMPLEMENTED);
                        } else if (GlobalConstant.IMPLEMENTATION.equals(flag)) {//执行中
                            listener.checkOrderSuccess(GlobalConstant.IMPLEMENTATION);
                        } else if (GlobalConstant.HAVE_CHECKED.equals(flag)) {//已核对
                            listener.checkOrderSuccess(GlobalConstant.HAVE_CHECKED);
                        }else if (GlobalConstant.HAVE_BEEN_CANCEL.equals(flag)) {//已取消
                            listener.checkOrderSuccess(GlobalConstant.HAVE_BEEN_CANCEL);
                        } else if (GlobalConstant.SUCCEED.equals(flag)) {//核对成功
                            listener.checkOrderSuccess(GlobalConstant.SUCCEED);
                        }
                    }else{
                        listener.checkOrderFailure(GlobalConstant.FAILED,null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.checkOrderFailure(GlobalConstant.FAILED,e);
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                listener.checkOrderFailure(GlobalConstant.FAILED,e);
            }
        });
    }

    public interface OnLoadOrdersListListener {
        void onOrderSuccess(List<OrderListBean> list);

        void onOrderFailure(String msg, Exception e);
    }

    public interface findPatIdListener {
        void findPatIdSuccess(List<OrderListBean> list);

        void findPatIdFailure(String msg, Exception e);
    }

    public interface checkOrderListener {

        void checkOrderSuccess(String flag);

        void checkOrderFailure(String msg, Exception e);
    }
}
