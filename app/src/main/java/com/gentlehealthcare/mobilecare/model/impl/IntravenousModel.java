package com.gentlehealthcare.mobilecare.model.impl;

import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.bean.IntraDontExcuteBean;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.model.IIntravenousModel;
import com.gentlehealthcare.mobilecare.net.bean.RecInspectBean;
import com.gentlehealthcare.mobilecare.net.bean.StartInfusionBean;
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
 * Created by zhiwei on 2016/5/14.
 */
public class IntravenousModel implements IIntravenousModel {


    @Override
    public void startIntravenous(String patId, String speed, String startTime, String injectionTool, String
            planorderno, String nextPatrolTime, final startIntravenousListener listener) {
        StartInfusionBean bean = new StartInfusionBean();
        bean.setPatId(patId);
        bean.setInjectionTool(injectionTool);
        bean.setPlanOrderNo(planorderno);
        bean.setNextPatrolTime(nextPatrolTime);
        if (speed != null) {
            bean.setSpeed(speed.split("滴")[0]);
        }
        CCLog.i("开始给药>>> " + UrlConstant.StartInfusion2() + bean.toString());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlConstant.StartInfusion2() + bean.toString(), new
                RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        JSONObject mJSONObject = null;
                        boolean status = false;
                        try {
                            mJSONObject = new JSONObject(responseInfo.result);
                            status = mJSONObject.getBoolean("result");
                            if (status) {
                                listener.startSuccess("success");
                            } else {
                                listener.startFailure("faile", null);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.startFailure("faile", e);
                        }

                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        listener.startFailure("faile", error);
                    }
                });
    }

    @Override
    public void saveNextPatrolTime(String patId, String username, String planOrderNo, String inspectionTime,
                                   final saveNextPatrolTimeListener listener) {
        String url = UrlConstant.RecordInspectionTime(0) + "?username=" + username + "&patId=" + patId +
                "&planOrderNo=" + planOrderNo + "&inspectionTime=" + UrlTool.transWord(inspectionTime);
        CCLog.i("保存下次巡视时间>>> " + url);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, url, new
                RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        JSONObject mJSONObject = null;
                        boolean status = false;
                        try {
                            mJSONObject = new JSONObject(responseInfo.result);
                            status = mJSONObject.getBoolean("result");
                            if (status) {
                                listener.saveSuccess("success");
                            } else {
                                listener.saveFailure("faile", null);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.saveFailure("faile", e);
                        }

                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        listener.saveFailure("faile", error);
                    }
                });
    }

    @Override
    public void savePatrolInfo(String patId, String username, String planOrderNo, String msg, String dosage,
                               String skinTestResult, int type, final saveIntravenousPatrolListener listener) {
        RecInspectBean bean = new RecInspectBean();
        bean.setPatId(patId);
        bean.setPlanOrderNo(planOrderNo);
        bean.setPerformDesc(msg);
        bean.setUsername(UserInfo.getName());
        bean.setDosageActual(dosage);
        bean.setSkinTestResult(skinTestResult);
        bean.setType(type);
        String url = UrlConstant.GetPatrol(0) + bean.toString() ;
        CCLog.i("记录巡视内容>>>" + url);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                JSONObject mJSONObject = null;
                boolean result = false;
                try {
                    mJSONObject = new JSONObject(responseInfo.result);
                    result = mJSONObject.getBoolean("result");
                    if (result) {
                        listener.saveIntravenousPatrolSuccess("success");
                    } else {
                        listener.saveIntravenousPatrolFailure("faile", null);
                    }

                } catch (JSONException e) {
                    listener.saveIntravenousPatrolFailure("faile", e);
                }
            }

            @Override
            public void onFailure(HttpException e, String msg) {
                listener.saveIntravenousPatrolFailure("faile", e);
            }
        });
    }

    @Override
    public void saveExceptionInfo(String patId, String username, String planOrderNo, String status, String
            completeDosage, String varianceCause, String varianceCauseDesc, final
                                  saveIntravenousExceptionListener listener) {
        String url = null;
        try {
            url = UrlConstant.CompleteInfusion() + patId + "&username=" + username + "&planOrderNo=" +
                    planOrderNo + "&status=" + status + "&completeDosage=" + completeDosage + "&varianceCause="
                    + varianceCause + "&varianceCauseDesc=" + UrlTool.transWord(varianceCauseDesc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        CCLog.i("记录巡视内容>>>" + url);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, url, new
                RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        JSONObject mJSONObject = null;
                        boolean result = false;
                        try {
                            mJSONObject = new JSONObject(responseInfo.result);
                            result = mJSONObject.getBoolean("result");
                            if (result) {
                                listener.saveIntravenousExceptionSuccess("success");
                            } else {
                                listener.saveIntravenousExceptionFailure("faile", null);
                            }

                        } catch (JSONException e) {
                            listener.saveIntravenousExceptionFailure("faile", e);
                        }
                    }

                    @Override
                    public void onFailure(HttpException e, String msg) {
                        listener.saveIntravenousExceptionFailure("faile", e);
                    }
                });
    }

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

    public interface startIntravenousListener {

        void startSuccess(String msg);

        void startFailure(String msg, Exception e);
    }

    public interface saveNextPatrolTimeListener {

        void saveSuccess(String msg);

        void saveFailure(String msg, Exception e);
    }

    public interface saveIntravenousPatrolListener {

        void saveIntravenousPatrolSuccess(String msg);

        void saveIntravenousPatrolFailure(String msg, Exception e);
    }

    public interface saveIntravenousExceptionListener {

        void saveIntravenousExceptionSuccess(String msg);

        void saveIntravenousExceptionFailure(String msg, Exception e);
    }

    public interface loadVariationDictListener {

        void loadVariationDictSuccess(List<IntraDontExcuteBean> list);

        void loadVariationDictFailure(String msg, Exception e);
    }


}
