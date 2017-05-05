package com.gentlehealthcare.mobilecare.model.impl;

import com.gentlehealthcare.mobilecare.bean.IntraDontExcuteBean;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.model.ICancelExecutionModel;
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
 * Created by Zyy on 2016/6/15.
 * 类说明：取消执行model
 */

public class CancelExceptionModel implements ICancelExecutionModel {
    @Override
    public void loadVariationDict(final loadVariationDictListener listener) {
        String url = UrlConstant.loadIntravenousException();
        CCLog.i("加载异常记录>>>" + url);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, url, new
                RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(responseInfo.result);
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<IntraDontExcuteBean>>() {
                            }.getType();
                            List<IntraDontExcuteBean> intraDontExcuteBeans = gson.fromJson(jsonArray.toString(),
                                    type);
                            listener.loadVariationDictSuccess(intraDontExcuteBeans);

                        } catch (JSONException e) {
                            listener.loadVariationDictFailure("faile", e);
                        }
                    }

                    @Override
                    public void onFailure(HttpException e, String msg) {
                        listener.loadVariationDictFailure("faile", e);
                    }
                });

    }

    @Override
    public void cancelExecution(String username, String warCode, String orderId, String planOrderNo, String performResult, String varianceCause, String varianceCauseDesc, String patId, final CancelExecutionListener listener) {
        String url = UrlConstant.cancelOrder() +UrlTool.transWord(username)  + "&orderId=" +UrlTool.transWord(orderId)  + "&wardCode=" +UrlTool.transWord(warCode)  + "&planOrderNo=" + UrlTool.transWord(planOrderNo) + "&performResult=" +UrlTool.transWord(performResult)  + "&varianceCause=" + UrlTool.transWord(varianceCause) + "&varianceCauseDesc=" + UrlTool.transWord(varianceCauseDesc) + "&patId=" + UrlTool.transWord(patId);
        CCLog.i("取消执行>>>" + url);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, url, new
                RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(responseInfo.result);
                            if(jsonObject.getBoolean("result")){
                                listener.CancelExecutionSuccess(GlobalConstant.SUCCEED);
                            }else{
                                listener.CancelExecutionFailure("failed",null);
                            }
                        } catch (JSONException e) {
                            listener.CancelExecutionFailure("failed", e);
                        }
                    }

                    @Override
                    public void onFailure(HttpException e, String msg) {
                        listener.CancelExecutionFailure("failed", e);
                    }
                });

    }

    public interface loadVariationDictListener {

        void loadVariationDictSuccess(List<IntraDontExcuteBean> list);

        void loadVariationDictFailure(String msg, Exception e);
    }

    public interface CancelExecutionListener {

        void CancelExecutionSuccess(String msg);

        void CancelExecutionFailure(String msg, Exception e);
    }
}
