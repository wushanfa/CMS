package com.gentlehealthcare.mobilecare.model.impl;

import com.gentlehealthcare.mobilecare.bean.ImplementationRecordBean;
import com.gentlehealthcare.mobilecare.bean.IntraDontExcuteBean;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.model.IImplementationRecordModel;
import com.gentlehealthcare.mobilecare.net.bean.LoadAppraislRecordBean;
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

import static com.gentlehealthcare.mobilecare.tool.CCLog.TAG;

/**
 * Created by Zyy on 2016/5/9.
 * 类说明：
 */
public class ImplementationRecordModel implements IImplementationRecordModel {


    @Override
    public void loadRecord(String patId, String planOrderNo, final OnLoadImplementationRecordListener listener) {
        String url = UrlConstant.loadImplementationRecor() + patId + "&planOrderNo=" + planOrderNo;
        CCLog.i("巡视记录加载>>>" + url);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                listener.onFailure(arg1, arg0);
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

        });
    }

    @Override
    public void loadVariationDict(final LoadVariationDict listener) {
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
    public void loadAppraislDict(final LoadAppraislListener listener) {
        CCLog.i(TAG, "加载评论字典>" + UrlConstant.LoadAppriaslRecord());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlConstant.LoadAppriaslRecord(), new
                RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = responseInfo.result;
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<LoadAppraislRecordBean>>() {
                        }.getType();
                        List<LoadAppraislRecordBean> list = gson.fromJson(result, type);
                        listener.loadAppraislSuccess(list);
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        listener.loadAppraislFailure("failed", error);
                    }
                });
    }

    @Override
    public void saveRecord(String patID, String username, String planOrderNo, String status, String recTime,
                           String skinResult, String exceptionDesc, String exceptionCode, String appraise, String
                                   eventId, final SaveRecordListener listener) {
        String url = null;
        url = UrlConstant.addAllRecord() + username + "&patId=" + patID + "&planOrderNo=" + planOrderNo +
                "&status=" + status + "&recTime=" + UrlTool.transWord(recTime) + "&skinResult=" + UrlTool
                .transWord(skinResult) + "&exceptionDesc=" + UrlTool.transWord(exceptionDesc) +
                "&exceptionCode=" + exceptionCode + "&appraise=" + UrlTool.transWord(appraise)
                + "&eventId=" + UrlTool.transWord(eventId);
        CCLog.i("记录信息>>>" + url);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(responseInfo.result);
                    if (jsonObject.getBoolean("result")) {
                        String tempEventId = (String) jsonObject.get("eventId");
                        listener.saveRecordSuccess(tempEventId);
                    } else {
                        listener.saveRecordFailure("faile", null);
                    }
                } catch (JSONException e) {
                    listener.saveRecordFailure("faile", e);
                }
            }

            @Override
            public void onFailure(HttpException e, String msg) {
                listener.saveRecordFailure("faile", e);
            }
        });
    }

    public interface OnLoadImplementationRecordListener {
        void onSuccess(List<ImplementationRecordBean> list);

        void onFailure(String msg, Exception e);
    }

    public interface LoadVariationDict {
        void loadVariationDictSuccess(List<IntraDontExcuteBean> list);

        void loadVariationDictFailure(String msg, Exception e);
    }

    public interface SaveRecordListener {
        void saveRecordSuccess(String msg);

        void saveRecordFailure(String msg, Exception e);
    }

    public interface LoadAppraislListener {
        void loadAppraislSuccess(List<LoadAppraislRecordBean> list);

        void loadAppraislFailure(String msg, Exception e);
    }

}
