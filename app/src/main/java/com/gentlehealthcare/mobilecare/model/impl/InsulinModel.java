package com.gentlehealthcare.mobilecare.model.impl;

import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.bean.insulin.InjectionSiteBean;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.model.IInsulinModel;
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
 * Created by zhiwei on 2016/5/14.
 */
public class InsulinModel implements IInsulinModel {

    @Override
    public void getSite(String patId, final onLoadSite onLoadSite) {
        String url = UrlConstant.loadInjection() + UserInfo.getUserName() + "&patId=" + patId;
        CCLog.i("加载注射位置>>>" + url);
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
                        Type type = new TypeToken<List<InjectionSiteBean>>() {
                        }.getType();
                        List<InjectionSiteBean> injectionSiteBeans = gson.fromJson(jsonArray.toString(), type);
                        onLoadSite.onLoadSuccess(injectionSiteBeans);
                    } else {
                        onLoadSite.onLoadFailure("数据解析异常", null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    onLoadSite.onLoadFailure("数据解析异常", e);
                }
            }

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                onLoadSite.onLoadFailure(arg1, arg0);
            }

        });
    }

    @Override
    public void updateSite(String patId, String siteId, String status, String itemNo, String planId, final
    onUpdateSite onUpdateSite) {
        String url = UrlConstant.updateInjection() + UserInfo.getUserName() + "&patId=" + patId + "&siteId=" +
                siteId + "&status=" + status + "&itemNo=" + itemNo + "&planId=" + planId;
        CCLog.i("更新注射位置状态>>>" + url);
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
                        Type type = new TypeToken<List<InjectionSiteBean>>() {
                        }.getType();
                        List<InjectionSiteBean> injectionSiteBeans = gson.fromJson(jsonArray.toString(), type);
                        onUpdateSite.onUpdateSiteSuccess(injectionSiteBeans);
                    } else {
                        onUpdateSite.onUpdateSiteFailure("数据解析异常", null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    onUpdateSite.onUpdateSiteFailure("数据解析异常", e);
                }
            }

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                onUpdateSite.onUpdateSiteFailure(arg1, arg0);
            }

        });
    }

    @Override
    public void reloadInjection(String patId, final onReloadSite onReloadSite) {
        String url = UrlConstant.reloadInjection() + UserInfo.getUserName() + "&patId=" + patId;
        CCLog.i("重新加载注射模板>>>" + url);
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
                        Type type = new TypeToken<List<InjectionSiteBean>>() {
                        }.getType();
                        List<InjectionSiteBean> injectionSiteBeans = gson.fromJson(jsonArray.toString(), type);
                        onReloadSite.onReloadSiteSuccess(injectionSiteBeans);
                    } else {
                        onReloadSite.onReloadSiteFailure("数据解析异常", null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    onReloadSite.onReloadSiteFailure("数据解析异常", e);
                }
            }

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                onReloadSite.onReloadSiteFailure(arg1, arg0);
            }

        });
    }

    public interface onLoadSite {

        void onLoadSuccess(List<InjectionSiteBean> siteBeans);

        void onLoadFailure(String msg, Exception e);
    }

    public interface onUpdateSite {

        void onUpdateSiteSuccess(List<InjectionSiteBean> siteBeans);

        void onUpdateSiteFailure(String msg, Exception e);
    }

    public interface onReloadSite {

        void onReloadSiteSuccess(List<InjectionSiteBean> siteBeans);

        void onReloadSiteFailure(String msg, Exception e);
    }
}
