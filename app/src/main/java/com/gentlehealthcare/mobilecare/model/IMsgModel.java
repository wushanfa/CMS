package com.gentlehealthcare.mobilecare.model;

import com.gentlehealthcare.mobilecare.model.impl.MsgModel;

/**
 * Created by Zyy on 2016/9/5.
 * 类说明：消息提示
 */

public interface IMsgModel {

     void handleMsg(String confirmStatus, String username, String noticeId, MsgModel.handleMsgListener listener);
}
