package com.gentlehealthcare.mobilecare.model.impl;

import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.model.IStartInsulinModel;
import com.gentlehealthcare.mobilecare.tool.CCLog;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhiwei on 2016/5/16.
 */
public class StartInsulinModel implements IStartInsulinModel {

    @Override
    public void startInsulin(String patId, String planOrderNo, String planId, int status, int itemNo, String
            siteId, final startInsulinInterface startInsulinInterface) {
        String url = UrlConstant.startInsulin() + UserInfo.getUserName() + "&patId=" +
                patId + "&planOrderNo=" + planOrderNo + "&planId=" + planId + "&status=" + status + "&itemNo=" +
                itemNo + "&siteId=" + siteId;
        CCLog.i("开始胰岛素>>>" + url);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                JSONObject mJsonObject = null;
                JSONArray jsonArray = null;
                boolean state = false;
                try {
                    mJsonObject = new JSONObject(result);
                    state = mJsonObject.getBoolean("result");
                    if (state) {
                        startInsulinInterface.startSuccess();
                    } else {
                        startInsulinInterface.startFail(mJsonObject.getString("msg"),null);
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                    startInsulinInterface.startFail("数据解析异常", e1);
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                startInsulinInterface.startFail("fail", null);
            }
        });
    }

    @Override
    public void getBlood(String patId, final getBloodInterface bloodInterface) {
        String url = UrlConstant.getBlood() + UserInfo.getUserName() + "&patId=" + patId;
        CCLog.i("获取最后一个血糖值>>>" + url);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                JSONObject mJsonObject = null;
                String blood = "";
                boolean state = false;
                try {
                    mJsonObject = new JSONObject(result);
                    state = mJsonObject.getBoolean("result");
                    if (state) {
                        blood = mJsonObject.getString("data");
                        bloodInterface.getSuccess(blood);
                    } else {
                        bloodInterface.getFail("数据解析异常", null);
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                    bloodInterface.getFail("数据解析异常", e1);
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                bloodInterface.getFail(msg, error);
            }
        });
    }

    public interface getBloodInterface {
        void getSuccess(String blood);

        void getFail(String msg, Exception e);
    }

    public interface startInsulinInterface {
        void startSuccess();

        void startFail(String msg, Exception e);
    }
}
