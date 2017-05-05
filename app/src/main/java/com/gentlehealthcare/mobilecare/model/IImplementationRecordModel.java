package com.gentlehealthcare.mobilecare.model;

import com.gentlehealthcare.mobilecare.model.impl.ImplementationRecordModel;

/**
 * Created by Zyy on 2016/5/9.
 * 类说明：
 */
public interface IImplementationRecordModel {
    void loadRecord(String patID, String planOrderNo, ImplementationRecordModel
            .OnLoadImplementationRecordListener listener);

    void loadVariationDict(ImplementationRecordModel.LoadVariationDict listener);

    void loadAppraislDict(ImplementationRecordModel.LoadAppraislListener listener);

    void saveRecord(String patID, String username, String planOrderNo, String status, String recTime, String
            skinResult, String exception, String exceptionCode, String appraise, String eventId,
                    ImplementationRecordModel.SaveRecordListener listener);
}
