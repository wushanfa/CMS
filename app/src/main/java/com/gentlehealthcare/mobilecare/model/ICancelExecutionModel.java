package com.gentlehealthcare.mobilecare.model;

import com.gentlehealthcare.mobilecare.model.impl.CancelExceptionModel;

/**
 * Created by Zyy on 2016/6/15.
 * 类说明：
 */

public interface ICancelExecutionModel {
    void loadVariationDict(CancelExceptionModel.loadVariationDictListener listener);
    void cancelExecution(String username, String wardCode, String orderId, String planOrderNo, String performResult, String varianceCause, String varianceCauseDesc, String patId, CancelExceptionModel.CancelExecutionListener listener);
}
