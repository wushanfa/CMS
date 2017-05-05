package com.gentlehealthcare.mobilecare.model.impl;

import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.model.IMsgModel;
import com.gentlehealthcare.mobilecare.tool.CCLog;
import com.gentlehealthcare.mobilecare.tool.UrlTool;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Zyy on 2016/9/5.
 * 类说明：
 */

public class MsgModel implements IMsgModel {
    @Override
    public void handleMsg(String confirmStatus,String username,String noticeId, final handleMsgListener listener) {
        HttpUtils http = new HttpUtils();
        String url=UrlConstant.PostTipResult()+"username="+username+"&confirmStatus="+confirmStatus+"&noticeId="+ UrlTool.transWord(noticeId);
        CCLog.i("处理消息>>>"+url);
        http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    JSONObject obj=new JSONObject(responseInfo.result);
                    if(obj.getBoolean("result")){
                        listener.handlerMsgSucessed(GlobalConstant.SUCCEED);
                    }else{
                        listener.handlerMsgFailed(GlobalConstant.FAILED, null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.handlerMsgFailed(GlobalConstant.FAILED, e);
                }

            }

            @Override
            public void onFailure(HttpException e, String s) {
                listener.handlerMsgFailed(GlobalConstant.FAILED, e);
            }
        });
    }

    public interface handleMsgListener {
        void handlerMsgSucessed(String msg);

        void handlerMsgFailed(String msg, Exception e);
    }
}
