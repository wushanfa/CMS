package com.gentlehealthcare.mobilecare.model.impl;

import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.model.IInsulinPatrolModel;
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
 * Created by zhiwei on 2016/5/17.
 */
public class InsulinPatrolModel implements IInsulinPatrolModel {

    @Override
    public void uploadResult(String patId, String planOrderNo, String desc, final onUpLoadResult onUpLoadResult) {
        String url = UrlConstant.insulinPatrol() + UserInfo.getUserName() + "&patId=" + patId + "&planOrderNo="
                + planOrderNo + "&performDesc=" + desc;
        CCLog.i("胰岛素巡视>>>" + url);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                String result = arg0.result;
                JSONObject mJsonObject = null;
                JSONArray jsonArray = null;
                boolean state = false;
                String msg = "";
                try {
                    mJsonObject = new JSONObject(result);
                    state = mJsonObject.getBoolean("result");
                    msg = mJsonObject.getString("msg");
                    if (state) {
                        onUpLoadResult.onLoadSuccess();
                    } else {
                        onUpLoadResult.onLoadFail("数据解析异常\n" + msg, null);
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                    onUpLoadResult.onLoadFail("数据解析异常", e1);
                }
            }

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                onUpLoadResult.onLoadFail(arg1, arg0);
            }
        });

    }

    public interface onUpLoadResult {

        void onLoadSuccess();

        void onLoadFail(String msg, Exception e);
    }
}
