package com.gentlehealthcare.mobilecare.model;

import com.gentlehealthcare.mobilecare.model.impl.PatroyHistoryModel;

/**
 * Created by Zyy on 2016/9/5.
 * 类说明：巡视历史记录
 */

public interface IPatrolHistoryModel {

     void getHistory(String patId, String wardCode, PatroyHistoryModel.getPatrolHistoryListener listener);
}
