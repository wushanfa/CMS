package com.gentlehealthcare.mobilecare.model;

import com.gentlehealthcare.mobilecare.model.impl.StartInsulinModel;

/**
 * Created by zhiwei on 2016/5/16.
 */
public interface IStartInsulinModel {
    void startInsulin(String patId, String planOrderNo, String planId, int status, int itemNo, String siteId,
                      final StartInsulinModel.startInsulinInterface startInsulinInterface);

    void getBlood(String patId, StartInsulinModel.getBloodInterface bloodInterface);
}
